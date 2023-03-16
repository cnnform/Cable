package cn.yhjz.biz.vo;

import lombok.Data;

@Data
public class BizDeviceVo {
    private Long id;

    /** 设备码 */
    private String deviceId;

    private Long cableId;

    private String cableName;

    /** 经度 */
    private Double lon;

    /** 纬度 */
    private Double lat;

    /** RFID */
    private String rfid;

    /**
     * 在线路中的排序序号
     */
    private Integer serialNo;
}
