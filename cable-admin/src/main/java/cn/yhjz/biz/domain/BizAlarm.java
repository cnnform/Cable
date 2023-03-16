package cn.yhjz.biz.domain;

import java.util.Date;

import cn.yhjz.common.annotation.Excel;
import cn.yhjz.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 报警管理对象 biz_alarm
 * 
 * @author yhjz
 * @date 2022-05-07
 */
public class BizAlarm extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 报警主键 */
    private Long alarmId;

    /** 摄像头主键 */
    @Excel(name = "摄像头主键")
    private Long cameraId;

    /** 设备码 */
    @Excel(name = "设备码")
    private String deviceId;

    /** 经度 */
    @Excel(name = "经度")
    private Double lon;

    /** 维度 */
    @Excel(name = "纬度")
    private Double lat;

    /** 事件类型 */
    @Excel(name = "事件类型")
    private String eventType;

    /** 事件目标 */
    @Excel(name = "事件目标")
    private String eventTarget;

    /** 图片详情 */
    @Excel(name = "图片详情")
    private String imgBase64;

    /** 是否有效，1有效，0无效 */
    @Excel(name = "状态")
    private Long status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date timeStamp;

    private String alertDesc;

    /**
     * 报警等级的名字
     */
    private String alertLevelName;

    public void setAlarmId(Long alarmId) 
    {
        this.alarmId = alarmId;
    }

    public Long getAlarmId() 
    {
        return alarmId;
    }
    public void setCameraId(Long cameraId) 
    {
        this.cameraId = cameraId;
    }

    public Long getCameraId() 
    {
        return cameraId;
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
    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLat()
    {
        return lat;
    }
    public void setEventType(String eventType) 
    {
        this.eventType = eventType;
    }

    public String getEventType() 
    {
        return eventType;
    }
    public void setEventTarget(String eventTarget) 
    {
        this.eventTarget = eventTarget;
    }

    public String getEventTarget() 
    {
        return eventTarget;
    }
    public void setImgBase64(String imgBase64) 
    {
        this.imgBase64 = imgBase64;
    }

    public String getImgBase64() 
    {
        return imgBase64;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }
    public void setTimeStamp(Date timeStamp) 
    {
        this.timeStamp = timeStamp;
    }

    public Date getTimeStamp() 
    {
        return timeStamp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("alarmId", getAlarmId())
            .append("cameraId", getCameraId())
            .append("deviceId", getDeviceId())
            .append("lon", getLon())
            .append("lat", getLat())
            .append("eventType", getEventType())
            .append("eventTarget", getEventTarget())
            .append("imgBase64", getImgBase64())
            .append("status", getStatus())
            .append("timeStamp", getTimeStamp())
            .toString();
    }

    public String getAlertDesc() {
        return alertDesc;
    }

    public void setAlertDesc(String alertDesc) {
        this.alertDesc = alertDesc;
    }

    public String getAlertLevelName() {
        return alertLevelName;
    }

    public void setAlertLevelName(String alertLevelName) {
        this.alertLevelName = alertLevelName;
    }
}
