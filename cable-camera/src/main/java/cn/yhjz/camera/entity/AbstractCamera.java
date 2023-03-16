package cn.yhjz.camera.entity;

import cn.yhjz.camera.NettyClientHandler;
import cn.yhjz.camera.thread.PtzThread;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 摄像头接口，定义公共的摄像头方法
 */
@Data
@Slf4j
public abstract class AbstractCamera {


    private Long id;

    /**
     * 设备码
     */
    private String deviceId;

    /**
     * 型号
     */
    private String deviceModel;

    /**
     * ip地址
     */
    private String deviceIp;

    /**
     * 网络区域
     */
    private String networkArea;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 端口号
     */
    private String port;

    /**
     * 通道
     */
    private String channel;

    /**
     * 指向
     */
    private Long arrow;

    /**
     * 垂直方位
     */
    private Long vertical;

    /**
     * 水平方位
     */
    private Long level;

    /**
     * 可视角度
     */
    private Long viewAngle;

    /**
     * 可视范围
     */
    private Long viewRange;

    /**
     * 经度
     */
    private Long lon;

    /**
     * 纬度
     */
    private Long lat;

    /**
     * 高度
     */
    private Long height;

    /**
     * 海拔
     */
    private Long altitude;

    /**
     * 状态
     */
    private Long status;
    /**
     * 是否已经登录
     */
    private Boolean isLogin;

    /**
     * 摄像头是否在线
     */
    private Boolean isOnline;

    public void startPtzThread() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new PtzThread(this).start();
    }

    /**
     * 初始化工作，比如连接和登录之类的操作
     */
    public abstract void init();

    /**
     * 截图
     */
    public abstract String capture();

    /**
     * 关闭
     */
    public abstract void close();

    public abstract void setPTZ(Double pan, Double tilt, Double zoom);

    public abstract JSONObject getPTZ();
}
