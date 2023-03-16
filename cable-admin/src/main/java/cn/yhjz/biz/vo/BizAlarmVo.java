package cn.yhjz.biz.vo;

import cn.yhjz.common.annotation.Excel;
import cn.yhjz.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 报警管理对象 biz_alarm
 * 
 * @author yhjz
 * @date 2022-05-07
 */
@Data
public class BizAlarmVo extends BaseEntity {
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

    private String alertLevelName;

    private String cableName;
}
