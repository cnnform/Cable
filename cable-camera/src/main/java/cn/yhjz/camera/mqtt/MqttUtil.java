package cn.yhjz.camera.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MqttUtil {

    public static MqttGateway mqttGateway;

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void init() {
        mqttGateway = context.getBean(MqttGateway.class);
    }

}
