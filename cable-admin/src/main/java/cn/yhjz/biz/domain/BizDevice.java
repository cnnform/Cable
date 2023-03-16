package cn.yhjz.biz.domain;

import cn.yhjz.common.annotation.Excel;
import cn.yhjz.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 定位设备管理 biz_device
 *
 * @author yhjz
 * @date 2022-02-15
 */
public class BizDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 设备码 */
    @Excel(name = "设备码")
    private String deviceId;

    private Long cableId;
    /** 经度 */
    @Excel(name = "经度")
    private Double lon;

    /** 纬度 */
    @Excel(name = "纬度")
    private Double lat;

    /** RFID */
    @Excel(name = "RFID")
    private String rfid;

    private Double depth;

    /**
     * 设备的排序序号
     */
    private Integer serialNo;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getDeviceId()
    {
        return deviceId;
    }
    public void setLon(Double lon)
    {
        this.lon = lon;
    }

    public Double getLon()
    {
        return lon;
    }
    public void setLat(Double lat)
    {
        this.lat = lat;
    }

    public Double getLat()
    {
        return lat;
    }
    public void setRfid(String rfid)
    {
        this.rfid = rfid;
    }

    public String getRfid()
    {
        return rfid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("deviceId", getDeviceId())
                .append("lon", getLon())
                .append("lat", getLat())
                .append("rfid", getRfid())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }

    public Long getCableId() {
        return cableId;
    }

    public void setCableId(Long cableId) {
        this.cableId = cableId;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public Double getDepth() {
        return depth;
    }

    public void setDepth(Double depth) {
        this.depth = depth;
    }
}
