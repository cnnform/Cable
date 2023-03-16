package cn.yhjz.camera.mqtt;


import cn.yhjz.camera.NettyClientHandler;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
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
     * mqtt配置
     */
    @Resource
    private MqttConfig mqttConfig;

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
    @Bean(name = "mqttOutboundChannel")
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * mqtt出站handler
     *
     * @return {@link MessageHandler}
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
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
    @Bean(name = "mqttInputChannel")
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
                        mqttConfig.getClientId() + +Math.random(), clientFactory(), mqttConfig.getSubTopic()+"/"+ NettyClientHandler.NETWORK_AREA_CODE);
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
    @ServiceActivator(inputChannel = "mqttInputChannel")//这里inputChannel的值是bean的名字
    public MessageHandler messageHandler() {
        return message -> {
            String topic = Objects.requireNonNull(message.getHeaders().get("mqtt_receivedTopic")).toString();
            log.info("topic:{}", topic);
            String bodyMsg = message.getPayload().toString();
            log.info("{}", bodyMsg);
        };
    }


}
