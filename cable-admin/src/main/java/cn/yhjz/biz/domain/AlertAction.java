package cn.yhjz.biz.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.yhjz.common.annotation.Excel;
import cn.yhjz.common.core.domain.BaseEntity;

/**
 * 报警动作对象 alert_action
 * 
 * @author maguoping
 * @date 2022-07-28
 */
public class AlertAction extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 动作名字 */
    @Excel(name = "动作名字")
    private String actionName;

    /** 动作编码 */
    @Excel(name = "动作编码")
    private String actionCode;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setActionName(String actionName) 
    {
        this.actionName = actionName;
    }

    public String getActionName() 
    {
        return actionName;
    }
    public void setActionCode(String actionCode) 
    {
        this.actionCode = actionCode;
    }

    public String getActionCode() 
    {
        return actionCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("actionName", getActionName())
            .append("actionCode", getActionCode())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
