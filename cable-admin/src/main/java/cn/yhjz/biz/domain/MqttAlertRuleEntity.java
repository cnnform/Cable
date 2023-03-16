package cn.yhjz.biz.domain;

import lombok.Data;

import java.util.Date;

@Data
public class MqttAlertRuleEntity{

    private String deviceId;

    //当前时间，用于判断时间段的
    private Date currentTime;

    //目标与线缆的距离
    private Double distance;

    //目标类型
    private String targetType;

    //行为
    private String behavior;

    //图片的base64编码
    private String imgBase64;

    //事件发生的经度
    private Double lon;
    //事件发生的纬度
    private Double lat;

}
