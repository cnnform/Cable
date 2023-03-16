package cn.yhjz.biz.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.yhjz.common.annotation.Excel;
import cn.yhjz.common.core.domain.BaseEntity;

/**
 * 线路对象 biz_cable
 * 
 * @author yhjz
 * @date 2022-09-01
 */
public class BizCable extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 线缆名字 */
    @Excel(name = "线缆名字")
    private String cableName;

    /** 线缆描述 */
    @Excel(name = "线缆描述")
    private String cableDesc;

    /** 线缆的每一条线，格式如下[[[lon,lat,depth],[lon,lat,depth]],[[lon,lat,depth],[lon,lat,depth]]]] */
    @Excel(name = "线缆的每一条线，格式如下[[[lon,lat,depth],[lon,lat,depth]],[[lon,lat,depth],[lon,lat,depth]]]]")
    private String cableLines;

    /** 输电局id */
    @Excel(name = "输电局id")
    private Long directId;

    /** 输电所id */
    @Excel(name = "输电所id")
    private Long stationId;

    /** 班id */
    @Excel(name = "班id")
    private Long classId;

    /** 负责人id */
    @Excel(name = "负责人id")
    private Long chargeManId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCableName(String cableName) 
    {
        this.cableName = cableName;
    }

    public String getCableName() 
    {
        return cableName;
    }
    public void setCableDesc(String cableDesc) 
    {
        this.cableDesc = cableDesc;
    }

    public String getCableDesc() 
    {
        return cableDesc;
    }
    public void setCableLines(String cableLines) 
    {
        this.cableLines = cableLines;
    }

    public String getCableLines() 
    {
        return cableLines;
    }
    public void setDirectId(Long directId) 
    {
        this.directId = directId;
    }

    public Long getDirectId() 
    {
        return directId;
    }
    public void setStationId(Long stationId) 
    {
        this.stationId = stationId;
    }

    public Long getStationId() 
    {
        return stationId;
    }
    public void setClassId(Long classId) 
    {
        this.classId = classId;
    }

    public Long getClassId() 
    {
        return classId;
    }
    public void setChargeManId(Long chargeManId) 
    {
        this.chargeManId = chargeManId;
    }

    public Long getChargeManId() 
    {
        return chargeManId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("cableName", getCableName())
            .append("cableDesc", getCableDesc())
            .append("cableLines", getCableLines())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("directId", getDirectId())
            .append("stationId", getStationId())
            .append("classId", getClassId())
            .append("chargeManId", getChargeManId())
            .toString();
    }
}
