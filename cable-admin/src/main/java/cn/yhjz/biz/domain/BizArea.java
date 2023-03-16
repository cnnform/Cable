package cn.yhjz.biz.domain;

import cn.yhjz.common.annotation.Excel;
import cn.yhjz.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 网络区域对象 biz_area
 *
 * @author yhjz
 * @date 2022-04-27
 */
public class BizArea extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 区域码 */
    @Excel(name = "区域码")
    private String areaCode;

    private Long cableId;

    /** 区域名称 */
    @Excel(name = "区域名称")
    private String areaName;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setAreaCode(String areaCode)
    {
        this.areaCode = areaCode;
    }

    public String getAreaCode()
    {
        return areaCode;
    }
    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }

    public String getAreaName()
    {
        return areaName;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("areaCode", getAreaCode())
                .append("areaName", getAreaName())
                .append("description", getDescription())
                .toString();
    }

    public Long getCableId() {
        return cableId;
    }

    public void setCableId(Long cableId) {
        this.cableId = cableId;
    }
}
