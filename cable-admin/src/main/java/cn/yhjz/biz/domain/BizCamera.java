package cn.yhjz.biz.domain;

import cn.yhjz.common.annotation.Excel;
import cn.yhjz.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 摄像头管理对象 biz_camera
 *
 * @author yhjz
 * @date 2022-04-12
 */
@Data
public class BizCamera extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 设备码
     */
    @Excel(name = "设备码")
    private String deviceId;

    private Long cableId;

    /**
     * 型号
     */
    @Excel(name = "型号")
    private String deviceModel;

    /**
     * 网络区域
     */
    @Excel(name = "网络区域")
    private String networkArea;

    /**
     * 设备IP
     */
    @Excel(name = "设备IP")
    private String deviceIp;

    /**
     * 用户名
     */
    @Excel(name = "用户名")
    private String userName;

    /**
     * 密码
     */
    @Excel(name = "密码")
    private String password;

    /**
     * 端口号
     */
    @Excel(name = "端口号")
    private String port;

    /**
     * 通道
     */
    @Excel(name = "通道")
    private String channel;

    /**
     * 指向
     */
    @Excel(name = "指向")
    private Long arrow;

    /**
     * 垂直方位
     */
    @Excel(name = "垂直方位")
    private Long vertical;

    /**
     * 水平方位
     */
    @Excel(name = "水平方位")
    private Long level;

    /**
     * 可视角度
     */
    @Excel(name = "可视角度")
    private Long viewAngle;

    /**
     * 可视范围
     */
    @Excel(name = "可视范围")
    private Long viewRange;

    /**
     * 垂直参数
     */
    @Excel(name = "垂直参数")
    private Long ptzTiltpos;

    /**
     * 水平参数
     */
    @Excel(name = "水平参数")
    private Long ptzPanpos;

    /**
     * 变倍参数
     */
    @Excel(name = "变倍参数")
    private Long ptzZoompos;

    /**
     * 补角
     */
    @Excel(name = "补角")
    private Long ptzAngle;

    /**
     * 经度
     */
    @Excel(name = "经度")
    private Double lon;

    /**
     * 纬度
     */
    @Excel(name = "纬度")
    private Double lat;

    /**
     * 高度
     */
    @Excel(name = "高度")
    private Long height;

    /**
     * 海拔
     */
    @Excel(name = "海拔")
    private Long altitude;

    /**
     * 描述
     */
    @Excel(name = "描述")
    private String description;

    /**
     * 注释
     */
    @Excel(name = "注释")
    private String notes;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private Long status;

    /**
     * json数组格式
     */
    @Excel(name = "检测区域")
    private String alertRegions;
    /**
     * 摄像头是否在线
     */
    private Boolean isOnline;

    /** 金三立guid */
    @Excel(name = "金三立guid")
    private String jslGuid;

    /** 金三立client_secret */
    @Excel(name = "金三立client_secret")
    private String jslClientSecret;

    /** 金三立clientid */
    @Excel(name = "金三立clientid")
    private String jslClientId;

    /** 金三立username */
    @Excel(name = "金三立username")
    private String jslUsername;

    /** 金三立password */
    @Excel(name = "金三立password")
    private String jslPassword;

    /** 金三立password */
    @Excel(name = "金三立rtsp地址")
    private String jslRtsp;

    public void setAlertRegions(String alertRegions) {
        this.alertRegions = alertRegions;
    }

    public String getAlertRegions() {
        return alertRegions;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setNetworkArea(String networkArea) {
        this.networkArea = networkArea;
    }

    public String getNetworkArea() {
        return networkArea;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public void setArrow(Long arrow) {
        this.arrow = arrow;
    }

    public Long getArrow() {
        return arrow;
    }

    public void setVertical(Long vertical) {
        this.vertical = vertical;
    }

    public Long getVertical() {
        return vertical;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public Long getLevel() {
        return level;
    }

    public void setViewAngle(Long viewAngle) {
        this.viewAngle = viewAngle;
    }

    public Long getViewAngle() {
        return viewAngle;
    }

    public void setViewRange(Long viewRange) {
        this.viewRange = viewRange;
    }

    public Long getViewRange() {
        return viewRange;
    }

    public void setPtzTiltpos(Long ptzTiltpos) {
        this.ptzTiltpos = ptzTiltpos;
    }

    public Long getPtzTiltpos() {
        return ptzTiltpos;
    }

    public void setPtzPanpos(Long ptzPanpos) {
        this.ptzPanpos = ptzPanpos;
    }

    public Long getPtzPanpos() {
        return ptzPanpos;
    }

    public void setPtzZoompos(Long ptzZoompos) {
        this.ptzZoompos = ptzZoompos;
    }

    public Long getPtzZoompos() {
        return ptzZoompos;
    }

    public void setPtzAngle(Long ptzAngle) {
        this.ptzAngle = ptzAngle;
    }

    public Long getPtzAngle() {
        return ptzAngle;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLon() {
        return lon;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLat() {
        return lat;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Long getHeight() {
        return height;
    }

    public void setAltitude(Long altitude) {
        this.altitude = altitude;
    }

    public Long getAltitude() {
        return altitude;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("deviceId", getDeviceId())
                .append("deviceModel", getDeviceModel())
                .append("networkArea", getNetworkArea())
                .append("deviceIp", getDeviceIp())
                .append("userName", getUserName())
                .append("password", getPassword())
                .append("port", getPort())
                .append("channel", getChannel())
                .append("arrow", getArrow())
                .append("vertical", getVertical())
                .append("level", getLevel())
                .append("viewAngle", getViewAngle())
                .append("viewRange", getViewRange())
                .append("ptzTiltpos", getPtzTiltpos())
                .append("ptzPanpos", getPtzPanpos())
                .append("ptzZoompos", getPtzZoompos())
                .append("ptzAngle", getPtzAngle())
                .append("lon", getLon())
                .append("lat", getLat())
                .append("height", getHeight())
                .append("altitude", getAltitude())
                .append("description", getDescription())
                .append("notes", getNotes())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
