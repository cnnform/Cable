package cn.yhjz.biz.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.yhjz.common.annotation.Excel;
import cn.yhjz.common.core.domain.BaseEntity;

/**
 * 摄像头规则配置项的值对象 biz_camera_rule_option
 * 
 * @author maguoping
 * @date 2022-08-03
 */
public class BizCameraRuleOption extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 摄像头和规则对应关系id */
    private Long refId;

    /** 动作id */
    @Excel(name = "动作id")
    private Long actionId;

    /** 配置项编码 */
    private String attrCode;

    /** 动作编码 */
    @Excel(name = "动作编码")
    private String actionCode;

    /** 配置项值 */
    private String attrValue;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setRefId(Long refId) 
    {
        this.refId = refId;
    }

    public Long getRefId() 
    {
        return refId;
    }
    public void setActionId(Long actionId) 
    {
        this.actionId = actionId;
    }

    public Long getActionId() 
    {
        return actionId;
    }
    public void setAttrCode(String attrCode) 
    {
        this.attrCode = attrCode;
    }

    public String getAttrCode() 
    {
        return attrCode;
    }
    public void setActionCode(String actionCode) 
    {
        this.actionCode = actionCode;
    }

    public String getActionCode() 
    {
        return actionCode;
    }
    public void setAttrValue(String attrValue) 
    {
        this.attrValue = attrValue;
    }

    public String getAttrValue() 
    {
        return attrValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("refId", getRefId())
            .append("actionId", getActionId())
            .append("attrCode", getAttrCode())
            .append("actionCode", getActionCode())
            .append("attrValue", getAttrValue())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
