package cn.yhjz.camera.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yhjz
 */
public class EventConstant {

    /**
     * 上传报警信息(9000扩展)
     */
    public static final Map<Integer, String> dwAlarmType = new HashMap<>();

    /**
     * 行为分析信息
     */
    public static final Map<Integer, String> actionInfo = new HashMap<>();


    static {
        dwAlarmType.put(0, "信号量报警");
        dwAlarmType.put(1, "硬盘满");
        dwAlarmType.put(2, "信号丢失");
        dwAlarmType.put(3, "移动侦测");
        dwAlarmType.put(4, "硬盘未格式化");
        dwAlarmType.put(5, "读写硬盘出错");
        dwAlarmType.put(6, "遮挡报警");
        dwAlarmType.put(7, "制式不匹配");
        dwAlarmType.put(8, "非法访问, 0xa-GPS定位信息(车载定制)");

        actionInfo.put(1, "穿越警戒面报警");
        actionInfo.put(2, "目标进入区域报警");
        actionInfo.put(3, "目标离开区域报警");
        actionInfo.put(4, "周界入侵报警");
        actionInfo.put(5, "徘徊事件触发");
        actionInfo.put(8, "快速移动(奔跑)事件");
        actionInfo.put(15, "离岗事件触发");
        actionInfo.put(20, "倒地事件触发");



    }




}
