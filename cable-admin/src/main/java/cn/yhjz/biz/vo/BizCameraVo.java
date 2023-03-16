package cn.yhjz.biz.vo;

import cn.yhjz.biz.domain.BizCable;
import cn.yhjz.common.annotation.Excel;
import cn.yhjz.framework.web.domain.server.Sys;
import lombok.Data;

/**
 * @author yhjz
 */
@Data
public class BizCameraVo {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 设备码 */
    private String deviceId;
    private Long cableId;
    private String cableName;

    private String deviceIp;

    private String userName;

    private String password;

    /** 型号 */
    private String deviceModel;

    /** 网络区域 */
    private String networkArea;

    /** 指向 */
    private Long arrow;
    /**
     * 可视角度
     */
    private Long viewAngle;

    /**
     * 可视范围
     */
    private Long viewRange;
    /** 垂直参数 */
    private Long ptzTiltpos;

    /** 水平参数 */
    private Long ptzPanpos;

    /** 变倍参数 */
    private Long ptzZoompos;

    /** 补角 */
    private Long ptzAngle;

    /** 经度 */
    private Double lon;

    /** 纬度 */
    private Double lat;

    /** 高度 */
    private Long height;

    /** 目标经度 */
    private Double targetLon;

    /** 目标纬度 */
    private Double targetLat;

    private BizCableVo cable;

    /** 金三立guid */
    private String jslGuid;

    /** 金三立client_secret */
    private String jslClientSecret;

    /** 金三立clientid */
    private String jslClientId;

    /** 金三立username */
    private String jslUsername;

    /** 金三立password */
    private String jslPassword;

    private String jslRtsp;
}
