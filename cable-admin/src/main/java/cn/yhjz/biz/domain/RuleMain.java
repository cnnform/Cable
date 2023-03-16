package cn.yhjz.biz.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.yhjz.common.annotation.Excel;
import cn.yhjz.common.core.domain.BaseEntity;

/**
 * 报警规则对象 rule_main
 * 
 * @author maguoping
 * @date 2022-08-01
 */
public class RuleMain extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 规则编码 */
    @Excel(name = "规则编码")
    private String ruleCode;

    /** 规则名 */
    @Excel(name = "规则名")
    private String ruleName;

    /** 规则内容 */
    @Excel(name = "规则内容")
    private String ruleContent;

    /** 规则描述 */
    @Excel(name = "规则描述")
    private String ruleDesc;

    /** 更新时间 */
    private Date upateTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setRuleCode(String ruleCode) 
    {
        this.ruleCode = ruleCode;
    }

    public String getRuleCode() 
    {
        return ruleCode;
    }
    public void setRuleName(String ruleName) 
    {
        this.ruleName = ruleName;
    }

    public String getRuleName() 
    {
        return ruleName;
    }
    public void setRuleContent(String ruleContent) 
    {
        this.ruleContent = ruleContent;
    }

    public String getRuleContent() 
    {
        return ruleContent;
    }
    public void setRuleDesc(String ruleDesc) 
    {
        this.ruleDesc = ruleDesc;
    }

    public String getRuleDesc() 
    {
        return ruleDesc;
    }
    public void setUpateTime(Date upateTime) 
    {
        this.upateTime = upateTime;
    }

    public Date getUpateTime() 
    {
        return upateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ruleCode", getRuleCode())
            .append("ruleName", getRuleName())
            .append("ruleContent", getRuleContent())
            .append("ruleDesc", getRuleDesc())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("createTime", getCreateTime())
            .append("upateTime", getUpateTime())
            .toString();
    }
}
