package cn.yhjz.biz.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.yhjz.common.annotation.Excel;
import cn.yhjz.common.core.domain.BaseEntity;

/**
 * 摄像头和规则关联对象 biz_camera_rule
 * 
 * @author maguoping
 * @date 2022-08-01
 */
public class BizCameraRule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 摄像头id */
    @Excel(name = "摄像头id")
    private Long cameraId;

    /** 规则id */
    @Excel(name = "规则id")
    private Long ruleId;

    /** 规则编码 */
    private String ruleCode;

    /** 报警等级idid */
    @Excel(name = "报警等级idid")
    private Long levelId;

    /** 报警等级名字 */
    private String levelName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCameraId(Long cameraId) 
    {
        this.cameraId = cameraId;
    }

    public Long getCameraId() 
    {
        return cameraId;
    }
    public void setRuleId(Long ruleId) 
    {
        this.ruleId = ruleId;
    }

    public Long getRuleId() 
    {
        return ruleId;
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
            .append("cameraId", getCameraId())
            .append("ruleId", getRuleId())
            .append("ruleCode", getRuleCode())
            .append("levelId", getLevelId())
            .append("createBy", getCreateBy())
            .append("levelName", getLevelName())
            .append("updateBy", getUpdateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
