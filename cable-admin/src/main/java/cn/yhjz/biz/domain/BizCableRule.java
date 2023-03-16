package cn.yhjz.biz.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.yhjz.common.annotation.Excel;
import cn.yhjz.common.core.domain.BaseEntity;

/**
 * 线路和规则关联对象 biz_cable_rule
 * 
 * @author maguoping
 * @date 2022-09-07
 */
public class BizCableRule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 线路id */
    @Excel(name = "线路id")
    private Long cableId;

    /** 规则编码 */
    @Excel(name = "规则编码")
    private String ruleCode;

    /** 报警等级id */
    @Excel(name = "报警等级id")
    private Long levelId;

    /** 报警等级名字 */
    @Excel(name = "报警等级名字")
    private String levelName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCableId(Long cableId) 
    {
        this.cableId = cableId;
    }

    public Long getCableId() 
    {
        return cableId;
    }
    public void setRuleCode(String ruleCode) 
    {
        this.ruleCode = ruleCode;
    }

    public String getRuleCode() 
    {
        return ruleCode;
    }
    public void setLevelId(Long levelId) 
    {
        this.levelId = levelId;
    }

    public Long getLevelId() 
    {
        return levelId;
    }
    public void setLevelName(String levelName) 
    {
        this.levelName = levelName;
    }

    public String getLevelName() 
    {
        return levelName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("cableId", getCableId())
            .append("ruleCode", getRuleCode())
            .append("levelId", getLevelId())
            .append("levelName", getLevelName())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
