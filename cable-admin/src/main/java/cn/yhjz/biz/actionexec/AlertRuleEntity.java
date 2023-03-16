package cn.yhjz.biz.actionexec;

import lombok.Data;

import java.util.Date;

@Data
public class AlertRuleEntity {

    //最终输出的结果，即是否满足此规则
    private Boolean result = false;

    //当前时间，用于判断时间段的
    private Date currentTime;

    //目标与线缆的距离
    private Double distance;

    //目标类型
    private String targetType;

    //行为
    private String behavior;
}
