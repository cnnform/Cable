package cn.yhjz.biz.vo;

import cn.yhjz.biz.domain.BizDevice;
import lombok.Data;

import java.util.List;

@Data
public class BizCableVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 线缆名字
     */
    private String cableName;

    /**
     * 线缆描述
     */
    private String cableDesc;

    /**
     * 线缆的每一条线，格式如下[[[lon,lat,depth],[lon,lat,depth]],[[lon,lat,depth],[lon,lat,depth]]]]
     */
    private String cableLines;

    /**
     * 输电局id
     */
    private Long directId;

    /**
     * 输电所id
     */
    private Long stationId;

    /**
     * 班id
     */
    private Long classId;

    /**
     * 负责人id
     */
    private Long chargeManId;

    //供电局名字
    private String directName;
    //输电所名字
    private String stationName;
    //班组名
    private String className;
    //负责人名字
    private String inchargeName;

    //定位设备的列表
    private List<BizDeviceVo> deviceList;
}
