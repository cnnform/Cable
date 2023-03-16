package cn.yhjz.biz.config;


import cn.yhjz.biz.domain.MqttAlertRuleEntity;
import cn.yhjz.biz.rule.CameraRuleExecutor;
import cn.yhjz.nio.camera.CameraMessageHandler;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;

/**
 * mqtt服务类
 */
@Slf4j
@Configuration
public class MqttServer {

    /**
     * 出站通道
     */
    public static final String OUTBOUND_CHANNEL = "mqttOutboundChannel";

    /**
     * 输入通道
     */
    public static final String INPUT_CHANNEL = "mqttInputChannel";

    /**
     * mqtt配置
     */
    @Resource
    private MqttConfig mqttConfig;
    @Autowired
    private CameraRuleExecutor cameraRuleExecutor;

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        log.info(mqttConfig.toString());
    }

    /**
     * mqtt客户端工厂
     *
     * @return {@link MqttPahoClientFactory}
     */
    @Bean
    public MqttPahoClientFactory clientFactory() {

        MqttConnectOptions options = new MqttConnectOptions();
        //配置MqttConnectOptions
        options.setServerURIs(new String[]{mqttConfig.getHostUrl()});
        options.setUserName(mqttConfig.getUsername());
        options.setPassword(mqttConfig.getPassword().toCharArray());
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(options);
        return factory;
    }

    /**
     * mqtt出站通道
     *
     * @return {@link MessageChannel}
     */
    @Bean(value = OUTBOUND_CHANNEL)
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * mqtt出站handler
     *
     * @return {@link MessageHandler}
     */
    @Bean
    @ServiceActivator(inputChannel = OUTBOUND_CHANNEL)
    public MessageHandler mqttOutboundHandler() {
        //MqttPahoMessageHandler初始化
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(mqttConfig.getClientId() + Math.random(), clientFactory());
        //设置默认的qos级别
        handler.setDefaultQos(1);
        //保留标志的默认值。如果没有mqtt_retained找到标题，则使用它。如果提供了自定义，则不使用它converter。这里不启用
        handler.setDefaultRetained(false);
        //设置发布的主题
//        handler.setDefaultTopic(mqttConfig.getPubTopic());
        //当 时true，调用者不会阻塞。相反，它在发送消息时等待传递确认。默认值为false（在确认交付之前发送阻止）。
        handler.setAsync(false);
        //当 async 和 async-events 都为 true 时，会发出 MqttMessageSentEvent（请参阅事件）。它包含消息、主题、客户端库生成的messageId、clientId和clientInstance（每次连接客户端时递增）。当客户端库确认交付时，会发出 MqttMessageDeliveredEvent。它包含 messageId、clientId 和 clientInstance，使传递与发送相关联。任何 ApplicationListener 或事件入站通道适配器都可以接收这些事件。请注意，有可能在 MqttMessageSentEvent 之前接收到 MqttMessageDeliveredEvent。默认值为false。
        handler.setAsyncEvents(false);
        return handler;
    }

    /**
     * mqtt输入通道
     *
     * @return {@link MessageChannel}
     */
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * 入站。
     * 接收摄像头的智能事件
     *
     * @return {@link MessageProducer}
     */
    @Bean
    public MessageProducer inbound() {
        //配置订阅端MqttPahoMessageDrivenChannelAdapter
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        mqttConfig.getClientId() + +Math.random(), clientFactory(), "cable_smart/#");
        //设置超时时间
        adapter.setCompletionTimeout(3000);
        //设置默认的消息转换类
        adapter.setConverter(new DefaultPahoMessageConverter());
        //设置qos级别
        adapter.setQos(2);
        //设置入站管道
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }


    /**
     * 消息处理程序
     *
     * @return {@link MessageHandler}
     */
    @Bean
    @ServiceActivator(inputChannel = INPUT_CHANNEL)//这里inputChannel的值是bean的名字
    public MessageHandler messageHandler() {
        return message -> {
            String topic = Objects.requireNonNull(message.getHeaders().get("mqtt_receivedTopic")).toString();
            log.info("topic:{}", topic);
            String bodyMsg = message.getPayload().toString();
            MqttAlertRuleEntity alertRuleEntity = JSON.parseObject(bodyMsg, MqttAlertRuleEntity.class);
            log.info("{},{},{}", alertRuleEntity.getTargetType(),alertRuleEntity.getBehavior(),alertRuleEntity.getDistance());
            //触发规则判断
            cameraRuleExecutor.execute(alertRuleEntity);
        };
    }

    /**
     * mqtt输入通道
     *
     * @return {@link MessageChannel}
     */
    @Bean
    public MessageChannel mqttInputChannel2() {
        return new DirectChannel();
    }

    /**
     * 入站。
     * 接收摄像头ptz状态
     *
     * @return {@link MessageProducer}
     */
    @Bean
    public MessageProducer inbound2() {
        //配置订阅端MqttPahoMessageDrivenChannelAdapter
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        mqttConfig.getClientId() + +Math.random(), clientFactory(), "cable_push_up_ptz/#");
        //设置超时时间
        adapter.setCompletionTimeout(3000);
        //设置默认的消息转换类
        adapter.setConverter(new DefaultPahoMessageConverter());
        //设置qos级别
        adapter.setQos(0);
        //设置入站管道
        adapter.setOutputChannel(mqttInputChannel2());
        return adapter;
    }

    @Autowired
    private CameraMessageHandler cameraMessageHandler;

    /**
     * 消息处理程序
     *
     * @return {@link MessageHandler}
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel2")//这里inputChannel的值是bean的名字
    public MessageHandler messageHandler2() {
        return message -> {
            String topic = Objects.requireNonNull(message.getHeaders().get("mqtt_receivedTopic")).toString();
//            log.info("topic:{}", topic);
            String bodyMsg = message.getPayload().toString();
//            log.info("{}", bodyMsg);
            cameraMessageHandler.handle0(bodyMsg);
        };
    }

    /**
     * mqtt输入通道
     *
     * @return {@link MessageChannel}
     */
    @Bean
    public MessageChannel mqttInputChannelPreview() {
        return new DirectChannel();
    }

    /**
     * 入站。
     * 接收摄像头ptz状态
     *
     * @return {@link MessageProducer}
     */
    @Bean
    public MessageProducer inboundPreview() {
        //配置订阅端MqttPahoMessageDrivenChannelAdapter
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        mqttConfig.getClientId() + +Math.random(), clientFactory(), "cable_push_frame/#");
        //设置超时时间
        adapter.setCompletionTimeout(3000);
        //设置默认的消息转换类
        adapter.setConverter(new DefaultPahoMessageConverter());
        //设置qos级别
        adapter.setQos(0);
        //设置入站管道
        adapter.setOutputChannel(mqttInputChannelPreview());
        return adapter;
    }

    /**
     * 消息处理程序
     *
     * @return {@link MessageHandler}
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannelPreview")//这里inputChannel的值是bean的名字
    public MessageHandler messageHandlerPreview() {
        return message -> {
            String topic = Objects.requireNonNull(message.getHeaders().get("mqtt_receivedTopic")).toString();
//            log.info("topic:{}", topic);
            String bodyMsg = message.getPayload().toString();
            JSONObject bodyJson = JSONObject.parseObject(bodyMsg);
            String cameraId = bodyJson.getString("cameraId");
            String deviceId = bodyJson.getString("deviceId");
            String imgBase64 = bodyJson.getString("imgBase64");
            byte[] imgBytes = bodyJson.getBytes("imgBytes");
            JSONObject paramJson = new JSONObject();
            paramJson.put("body", bodyJson);
            paramJson.put("orderType", "push_frame");
//            log.info("{}", bodyMsg);
            cameraMessageHandler.handle0(paramJson.toJSONString());
        };
    }

    /**
     * webrtc answer通道
     *
     * @return {@link MessageChannel}
     */
    @Bean
    public MessageChannel mqttInputChannelWebrtcAnswer() {
        return new DirectChannel();
    }

    /**
     * @return {@link MessageProducer}
     */
    @Bean
    public MessageProducer inboundWebrtcAnswer() {
        //配置订阅端MqttPahoMessageDrivenChannelAdapter
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        mqttConfig.getClientId() + +Math.random(), clientFactory(), "cable_webrtc_answer/#");
        //设置超时时间
        adapter.setCompletionTimeout(3000);
        //设置默认的消息转换类
        adapter.setConverter(new DefaultPahoMessageConverter());
        //设置qos级别
        adapter.setQos(2);
        //设置入站管道
        adapter.setOutputChannel(mqttInputChannelWebrtcAnswer());
        return adapter;
    }

    /**
     * 处理webrtc的answer
     *
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannelWebrtcAnswer")//这里inputChannel的值是bean的名字
    public MessageHandler messageHandlerWebrtcAnswer() {
        return message -> {
            String topic = Objects.requireNonNull(message.getHeaders().get("mqtt_receivedTopic")).toString();
//            log.info("topic:{}", topic);
            String bodyMsg = message.getPayload().toString();
            JSONObject bodyJson = JSONObject.parseObject(bodyMsg);
            String deviceId = bodyJson.getString("deviceId");
            String sdp = bodyJson.getString("sdp");
            String type = bodyJson.getString("type");
            JSONObject answerJson = new JSONObject();
            answerJson.put("orderType", "cable_webrtc_answer");
            answerJson.put("sdp", sdp);
            answerJson.put("type", type);
//            log.info("{}", bodyMsg);
            cameraMessageHandler.sendMsg2WS(deviceId, answerJson.toJSONString());
        };
    }
}
