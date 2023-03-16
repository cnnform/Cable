package cn.yhjz.biz.rule;

/**
 * 规则项的配置
 */
public enum RuleItemEnum {
    time("time"),//时间段
    distance("distance"),//到线缆的距离
    behavior("behavior"),//目标行为
    targetClass("targetClass"),//目标类型，比如人、车、动物
    duration("duration");//持续时间
    private String code;

    RuleItemEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
