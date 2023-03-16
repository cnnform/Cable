package cn.yhjz.biz.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.yhjz.common.annotation.Excel;
import cn.yhjz.common.core.domain.BaseEntity;

/**
 * 动作的属性对象 alert_action_attr
 *
 * @author maguoping
 * @date 2022-07-28
 */
public class AlertActionAttr extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 动作id */
    @Excel(name = "动作id")
    private Long actionId;

    /** 属性编码 */
    @Excel(name = "属性编码")
    private String attrCode;

    /** 动作code */
    @Excel(name = "动作code")
    private String actionCode;

    /** 属性名字 */
    @Excel(name = "属性名字")
    private String attrName;

    /** 属性值类型，暂时无用 */
    @Excel(name = "属性值类型，暂时无用")
    private String attrValueType;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
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
    public void setAttrName(String attrName)
    {
        this.attrName = attrName;
    }

    public String getAttrName()
    {
        return attrName;
    }
    public void setAttrValueType(String attrValueType)
    {
        this.attrValueType = attrValueType;
    }

    public String getAttrValueType()
    {
        return attrValueType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("actionId", getActionId())
                .append("attrCode", getAttrCode())
                .append("actionCode", getActionCode())
                .append("attrName", getAttrName())
                .append("attrValueType", getAttrValueType())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
