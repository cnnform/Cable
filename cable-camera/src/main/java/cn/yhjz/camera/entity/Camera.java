package cn.yhjz.camera.entity;

import cn.yhjz.camera.NettyClientHandler;
import cn.yhjz.camera.preview.*;
import com.alibaba.fastjson.JSONObject;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import java.nio.ByteBuffer;
import java.util.Base64;

@Data
@Slf4j
public class Camera {

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
     * 海康威视sdk实例,dll接口
     */
    private HCNetSDK hcNetSDK;

    /**
     * 图像处理相关的（海康威视的dll接口）
     */
    private PlayCtrl playControl;
    /**
     * 是否已经登录
     */
    private Boolean isLogin;

    /**
     * 摄像头是否在线
     */
    private Boolean isOnline;

    /**
     * 准备开始预览，设置环境参数
     */
    public void prePreview() {
        int lDChannel = Integer.parseInt(this.channel);
        log.info("{},使用的通道号: {}", this.id, lDChannel);
        HCNetSDK.NET_DVR_PREVIEWINFO strClientInfo = new HCNetSDK.NET_DVR_PREVIEWINFO();
        strClientInfo.read();
        // 窗口句柄，从回调取流不显示一般设置为空
        strClientInfo.hPlayWnd = null;
        strClientInfo.lChannel = Integer.parseInt(this.channel);  //通道号
        strClientInfo.dwStreamType = 0; //0-主码流，1-子码流，2-三码流，3-虚拟码流，以此类推
        strClientInfo.dwLinkMode = 0; //连接方式：0- TCP方式，1- UDP方式，2- 多播方式，3- RTP方式，4- RTP/RTSP，5- RTP/HTTP，6- HRUDP（可靠传输） ，7- RTSP/HTTPS，8- NPQ
        strClientInfo.bBlocked = 1;
        strClientInfo.write();
        // 回调函数定义必须是全局的
        if (fRealDataCallBack == null) {
            fRealDataCallBack = new FRealDataCallBack(this.playControl, this.m_lPort);
        }

        //开启预览
        lPlay = hcNetSDK.NET_DVR_RealPlay_V40(this.lUserID, strClientInfo, fRealDataCallBack, null);
        if (lPlay == -1) {
            int iErr = hcNetSDK.NET_DVR_GetLastError();
            throw new RuntimeException("取流失败" + iErr);

        }
        log.info("取流成功");
    }

    public void stopPreview(){
        log.info("请求停止预览");
        if (this.lPlay > 0) {
            hcNetSDK.NET_DVR_StopRealPlay(this.lPlay);
        }
    }
    private FRealDataCallBack fRealDataCallBack;//预览回调函数实现
    private int lPlay = -1;

    /**
     * 预览，返回图像的base64字符串
     *
     * @return
     */
    public String preview() throws InterruptedException {
//        this.capture();
        IntByReference pWidth = new IntByReference(0);
        IntByReference pHieght = new IntByReference(0);
        boolean bFlag = playControl.PlayM4_GetPictureSize(m_lPort.getValue(), pWidth, pHieght);
        if (!bFlag) {
            throw new RuntimeException("获取图像分辨率失败：" + playControl.PlayM4_GetLastError(m_lPort.getValue()));
        }
        IntByReference RealPicSize = new IntByReference(0);
        int picsize = pWidth.getValue() * pHieght.getValue() * 5;
        HCNetSDK.BYTE_ARRAY picByte = new HCNetSDK.BYTE_ARRAY(picsize);
        picByte.write();
        Pointer pByte = picByte.getPointer();
        boolean b_GetPic = playControl.PlayM4_GetJPEG(m_lPort.getValue(), pByte, picsize, RealPicSize);
        if (!b_GetPic) {
            throw new RuntimeException("抓图失败：" + playControl.PlayM4_GetLastError(m_lPort.getValue()));
        }
        picByte.read();
        ByteBuffer buffers = pByte.getByteBuffer(0, RealPicSize.getValue());
        byte[] bytes = new byte[RealPicSize.getValue()];
        buffers.rewind();
        buffers.get(bytes);

        String base64Str = Base64.getEncoder().encodeToString(bytes);
        return base64Str;
    }

    /**
     * 获得截图。
     * 单帧数据捕获并保存成 JPEG 存放在指定的内存空间中
     */
    public void capture() throws InterruptedException {
        HCNetSDK.NET_DVR_JPEGPARA jpegPara = new HCNetSDK.NET_DVR_JPEGPARA();
        jpegPara.wPicQuality = 0;
        jpegPara.wPicSize = 0;
        int bufferSize = 1024 * 1024;
        Memory sJpegPicBuffer = new Memory(bufferSize);
        IntByReference lpSizeReturned = new IntByReference();
        int lChannel = Integer.parseInt(this.channel);
        boolean res = this.hcNetSDK.NET_DVR_CaptureJPEGPicture_NEW(this.lUserID, lChannel, jpegPara, sJpegPicBuffer, bufferSize, lpSizeReturned);
        if (res) {
            ByteBuffer imgBuffer = sJpegPicBuffer.getByteBuffer(0, lpSizeReturned.getValue());
            log.info("{},{}",lpSizeReturned.getValue());
           boolean hasArray = imgBuffer.hasArray();
            log.info("获得截图成功");
        } else {
            log.error("获得截图失败");
        }
    }


    public static final String WINDOWS_PATH_SPLITER = "\\";

    private FExceptionCallBack_Imp fExceptionCallBack;

    private int lUserID;

    private int lAlarmHandle = -1;

    private Pointer pUser;

    private FMSGCallBack_V31 fMSFCallBack_V31 = null;

    private HCNetSDK.NET_DVR_DEVICEINFO_V40 m_strDeviceInfo;

    /**
     * 开关设置云台方位
     */
    private boolean flag;

    public static class FExceptionCallBack_Imp implements HCNetSDK.FExceptionCallBack {
        @Override
        public void invoke(int dwType, int lUserID, int lHandle, Pointer pUser) {
            System.out.println("异常事件类型:" + dwType);
            return;
        }
    }

    public void connect() {
        if (hcNetSDK == null) {
            this.createSDKInstance();
        }
        if (playControl == null) {
            this.createPlayInstance();
        }
        //linux系统建议调用以下接口加载组件库
//        String basePath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String basePath = "D:";
        if (OSSelect.isLinux()) {
            HCNetSDK.BYTE_ARRAY ptrByteArray1 = new HCNetSDK.BYTE_ARRAY(256);
            HCNetSDK.BYTE_ARRAY ptrByteArray2 = new HCNetSDK.BYTE_ARRAY(256);
            //这里是库的绝对路径，请根据实际情况修改，注意改路径必须有访问权限
            String strPath1 = basePath + "/HKLIB/libcrypto.so.1.1";
            String strPath2 = basePath + "/HKLIB/libssl.so.1.1";

            System.arraycopy(strPath1.getBytes(), 0, ptrByteArray1.byValue, 0, strPath1.length());

            ptrByteArray1.write();
            hcNetSDK.NET_DVR_SetSDKInitCfg(3, ptrByteArray1.getPointer());

            System.arraycopy(strPath2.getBytes(), 0, ptrByteArray2.byValue, 0, strPath2.length());
            ptrByteArray2.write();
            hcNetSDK.NET_DVR_SetSDKInitCfg(4, ptrByteArray2.getPointer());

            String strPathCom = basePath + "/HKLIB";
            HCNetSDK.NET_DVR_LOCAL_SDK_PATH struComPath = new HCNetSDK.NET_DVR_LOCAL_SDK_PATH();
            System.arraycopy(strPathCom.getBytes(), 0, struComPath.sPath, 0, strPathCom.length());
            struComPath.write();
            hcNetSDK.NET_DVR_SetSDKInitCfg(2, struComPath.getPointer());
        }

        //SDK初始化，一个程序只需要调用一次
        boolean initSuc = hcNetSDK.NET_DVR_Init();

        if (!initSuc) {
            throw new RuntimeException("初始化摄像头失败:" + this.getId());
        }

        //异常消息回调
        if (fExceptionCallBack == null) {
            fExceptionCallBack = new FExceptionCallBack_Imp();
        }
        pUser = null;
        if (!hcNetSDK.NET_DVR_SetExceptionCallBack_V30(0, 0, fExceptionCallBack, pUser)) {
            return;
        }
        log.info("设置异常消息回调成功:{}", this.id);

        // 登录
        login(this.deviceIp, this.port, this.userName, this.password);
        // 布防
        setAlarm();
    }


    /**
     * 设备登录
     *
     * @param ip   设备IP
     * @param port SDK端口，默认设备的8000端口
     * @param user 设备用户名
     * @param psw  设备密码
     */
    public void login(String ip, String port, String user, String psw) {

        // 注册
        // 设备登录信息
        HCNetSDK.NET_DVR_USER_LOGIN_INFO m_strLoginInfo = new HCNetSDK.NET_DVR_USER_LOGIN_INFO();
        // 设备信息
        HCNetSDK.NET_DVR_DEVICEINFO_V40 m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V40();
        // 设备ip地址
        String m_sDeviceIP = ip;
        m_strLoginInfo.sDeviceAddress = new byte[HCNetSDK.NET_DVR_DEV_ADDRESS_MAX_LEN];
        System.arraycopy(m_sDeviceIP.getBytes(), 0, m_strLoginInfo.sDeviceAddress, 0, m_sDeviceIP.length());
        // 设备用户名
        String m_sUsername = user;
        m_strLoginInfo.sUserName = new byte[HCNetSDK.NET_DVR_LOGIN_USERNAME_MAX_LEN];
        System.arraycopy(m_sUsername.getBytes(), 0, m_strLoginInfo.sUserName, 0, m_sUsername.length());
        // 设备密码
        String m_sPassword = psw;
        m_strLoginInfo.sPassword = new byte[HCNetSDK.NET_DVR_LOGIN_PASSWD_MAX_LEN];
        System.arraycopy(m_sPassword.getBytes(), 0, m_strLoginInfo.sPassword, 0, m_sPassword.length());
        // SDK端口
        m_strLoginInfo.wPort = Short.parseShort(port);
        // 是否异步登录：0- 否，1- 是
        m_strLoginInfo.bUseAsynLogin = false;
        m_strLoginInfo.write();
        lUserID = hcNetSDK.NET_DVR_Login_V40(m_strLoginInfo, m_strDeviceInfo);
        if (lUserID == -1) {
            throw new RuntimeException("登录IP: " + m_sDeviceIP + "失败, 错误码为" + hcNetSDK.NET_DVR_GetLastError());
        } else {
            log.info(m_sDeviceIP + ":设备登录成功! " + "设备序列号:" + new String(m_strDeviceInfo.struDeviceV30.sSerialNumber).trim());
            m_strDeviceInfo.read();
            this.isLogin = true;
        }
    }

    /**
     * 注销登录
     */
    public void logout() {
        //退出程序时调用，每一台设备分别注销
        if (hcNetSDK.NET_DVR_Logout(lUserID)) {
            log.info("注销成功:{}", this.id);
        }

        //SDK反初始化，释放资源，只需要退出时调用一次
        hcNetSDK.NET_DVR_Cleanup();
        this.isLogin = false;
    }

    /**
     *  获取PTZ参数
     */
    public HCNetSDK.NET_DVR_PTZPOS getPTZControl() {
        HCNetSDK.NET_DVR_PTZPOS m_ptzPosCurrent = new  HCNetSDK.NET_DVR_PTZPOS();
        IntByReference ibrBytesReturned = new IntByReference(0);
        Pointer pioint  = m_ptzPosCurrent.getPointer();
        hcNetSDK.NET_DVR_GetDVRConfig(lUserID, HCNetSDK.NET_DVR_GET_PTZPOS, Integer.parseInt(channel), pioint, m_ptzPosCurrent.size(), ibrBytesReturned);
        m_ptzPosCurrent.read();
        JSONObject msgJson = new JSONObject();
        msgJson.put("orderType", "get_ptc_control");
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("cameraId", id);
        bodyJson.put("wPanPos", Integer.parseInt(Integer.toHexString(m_ptzPosCurrent.wPanPos)) / 10);
        bodyJson.put("wTiltPos", Integer.parseInt(Integer.toHexString(m_ptzPosCurrent.wTiltPos)) / 10);
        bodyJson.put("wZoomPos", Integer.parseInt(Integer.toHexString(m_ptzPosCurrent.wZoomPos)) / 10);
        msgJson.put("body", bodyJson);
        NettyClientHandler.ctx.writeAndFlush(Unpooled.copiedBuffer(msgJson.toJSONString(), CharsetUtil.UTF_8));
        return m_ptzPosCurrent;
    }

    /**
     *  设置PTZ参数
     */
    public void setPTZControl(int type, int num, int dwPTZCommand) {
        log.info("type: {}, target: {}, dwPTZCommand: {}", type, num, dwPTZCommand);
        // 设置是否转动
        flag = true;
        // 当前位置参数
        int currentLocation = 0;
        // 旋转速率 取值范围 1 - 7 注: 由慢变快
        int rate = 4;
        // 速率区间 对应速率
        int rateValueA = 3;
        int rateValueB = 15;
        int rateValueC = 30;

        while (flag) {
            // 获取当前摄像头PTZ
            HCNetSDK.NET_DVR_PTZPOS m_ptzPosCurrent = getPTZControl();
            if (type == 1) {
                currentLocation = Integer.parseInt(Integer.toHexString(m_ptzPosCurrent.wPanPos))/10;
            } else if (type == 2) {
                currentLocation = Integer.parseInt(Integer.toHexString(m_ptzPosCurrent.wTiltPos))/10;
            } else if (type == 3) {
                currentLocation = Integer.parseInt(Integer.toHexString(m_ptzPosCurrent.wZoomPos))/10;
            } else {
                break;
            }
            // 计算速率
            if (Math.abs(currentLocation - num) <= rateValueB) {
                rate = 1;
            } else if (Math.abs(currentLocation - num) <= rateValueC) {
                rate = 2;
            }
            // 开启转动
            boolean ndpo = hcNetSDK.NET_DVR_PTZControlWithSpeed_Other(lUserID, Integer.parseInt(channel), dwPTZCommand, 0, rate);
            if (!ndpo) {
                log.info("hcNetSDK.NET_DVR_GetLastError(): {}", hcNetSDK.NET_DVR_GetLastError());
                break;
            }
            // 停止旋转
            if (Math.abs(currentLocation - num) <= rateValueA) {
                // 关闭
                hcNetSDK.NET_DVR_PTZControlWithSpeed_Other(lUserID, Integer.parseInt(channel), dwPTZCommand, 1, rate);
                break;
            }
        }
    }


    /**
     * NET_DVR_PTZControl_Other
     */
    public void ptzControl(int wPanPos, int wTiltPos, int wZoomPos) {
        if (flag) {
            flag = false;
            try {
                log.info("停止云台转动,设置新位置!");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // 控制参数 21 上仰、22 下俯、23 左转、24 右转
        int dwPTZCommand = 0;

        // 获取当前摄像头的PTZ参数
        HCNetSDK.NET_DVR_PTZPOS m_ptzPosCurrent = getPTZControl();
//        IntByReference ibrBytesReturned = new IntByReference(0);
//        Pointer pioint  = m_ptzPosCurrent.getPointer();
//        hcNetSDK.NET_DVR_GetDVRConfig(lUserID, HCNetSDK.NET_DVR_GET_PTZPOS, Integer.parseInt(channel), pioint, m_ptzPosCurrent.size(), ibrBytesReturned);
//        m_ptzPosCurrent.read();

        System.out.println("PTZ垂直参数为: " + m_ptzPosCurrent.wTiltPos);
        System.out.println("PTZ垂直参数为: " + Integer.parseInt(Integer.toHexString(m_ptzPosCurrent.wTiltPos))/10);
        System.out.println("PTZ水平参数为: " + m_ptzPosCurrent.wPanPos);
        System.out.println("PTZ水平参数为: " + Integer.parseInt(Integer.toHexString(m_ptzPosCurrent.wPanPos))/10);
        System.out.println("PTZ变倍参数为: " + m_ptzPosCurrent.wZoomPos);

        // PTZ 水平参数
        int getWpanPos = Integer.parseInt(Integer.toHexString(m_ptzPosCurrent.wPanPos))/10;
        // PTZ 垂直参数
        int getWtiltPos = Integer.parseInt(Integer.toHexString(m_ptzPosCurrent.wTiltPos))/10;
        // PTZ 变焦参数
        int getWzoomPos = Integer.parseInt(Integer.toHexString(m_ptzPosCurrent.wZoomPos))/10;

        // 取余
        wPanPos %= 360;
        wTiltPos %= 90;
        wZoomPos %= 40;

        // P 水平
        if (wPanPos != getWpanPos) {
            // 度数
            int d = 180;
            int i = wPanPos - getWpanPos;
            int j = (i < 0) ? -i : i;
            if (i > 0 && j <= d) {
                dwPTZCommand = 24;
            } else if (i > 0 && j >= d) {
                dwPTZCommand = 23;
            } else if (i < 0 && j <= d) {
                dwPTZCommand = 23;
            } else if (i < 0 && j >= d) {
                dwPTZCommand = 24;
            }
            setPTZControl(1, wPanPos, dwPTZCommand);
        }
        // T 垂直
        if (wTiltPos != getWtiltPos) {
            if (wTiltPos > getWtiltPos) {
                dwPTZCommand = 22;
            } else {
                dwPTZCommand = 21;
            }
            setPTZControl(2, wTiltPos, dwPTZCommand);
        }

    }


    /**
     * 动态库加载
     *
     * @return
     */
    public boolean createSDKInstance() {
        if (hcNetSDK == null) {
            synchronized (HCNetSDK.class) {
                String strDllPath = "";
                try {
//                    String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
                    String path = "D:/";
                    if (OSSelect.isWindows()) {
                        //win系统加载库路径
                        strDllPath = path + "HKLIB/HCNetSDK.dll";
                        strDllPath = strDllPath.replace("/", "\\");
                        if (strDllPath.startsWith(WINDOWS_PATH_SPLITER)) {
                            strDllPath = strDllPath.substring(1);
                        }
                    } else if (OSSelect.isLinux()) {
                        //Linux系统加载库路径
                        strDllPath = path + "HKLIB/libhcnetsdk.so";
                    }

                    hcNetSDK = (HCNetSDK) Native.loadLibrary(strDllPath, HCNetSDK.class);
                } catch (Exception ex) {
                    log.error("loadLibrary: " + strDllPath + " Error: " + ex.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 播放库加载
     *
     * @return
     */
    public boolean createPlayInstance() {
        if (playControl == null) {
            synchronized (PlayCtrl.class) {
                String strPlayPath = "";
//                String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
                String path = "D:/";
                try {
                    if (OSSelect.isWindows()) {
                        //win系统加载库路径
                        strPlayPath = path + "HKLIB/PlayCtrl.dll";
                        strPlayPath = strPlayPath.replace("/", "\\");
                        if (strPlayPath.startsWith(WINDOWS_PATH_SPLITER)) {
                            strPlayPath = strPlayPath.substring(1);
                        }
                    } else if (OSSelect.isLinux()) {
                        //Linux系统加载库路径
                        strPlayPath = path + "HKLIB/libPlayCtrl.so";
                    }
                    playControl = (PlayCtrl) Native.loadLibrary(strPlayPath, PlayCtrl.class);
                } catch (Exception ex) {
                    log.error("loadLibrary: " + strPlayPath + " Error: " + ex.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 报警布防接口
     */
    public void setAlarm() {
        // 尚未布防,需要布防
        if (lAlarmHandle < 0) {
            // 报警布防参数设置
            HCNetSDK.NET_DVR_SETUPALARM_PARAM m_strAlarmInfo = new HCNetSDK.NET_DVR_SETUPALARM_PARAM();
            m_strAlarmInfo.dwSize = m_strAlarmInfo.size();
            m_strAlarmInfo.byLevel = 0;  //布防等级
            m_strAlarmInfo.byAlarmInfoType = 1;   // 智能交通报警信息上传类型：0- 老报警信息（NET_DVR_PLATE_RESULT），1- 新报警信息(NET_ITS_PLATE_RESULT)
            m_strAlarmInfo.byDeployType = 0;   //布防类型：0-客户端布防，1-实时布防
            m_strAlarmInfo.write();

            lAlarmHandle = hcNetSDK.NET_DVR_SetupAlarmChan_V41(lUserID, m_strAlarmInfo);
            log.info("lAlarmHandle: " + lAlarmHandle);
            if (lAlarmHandle == -1) {
                log.error("布防失败，错误码为" + hcNetSDK.NET_DVR_GetLastError());
            } else {
                log.info("布防成功");
            }
        } else {
            log.error("设备已经布防，请先撤防！");
        }
        // 设置报警回调函数
        if (fMSFCallBack_V31 == null) {
            fMSFCallBack_V31 = new FMSGCallBack_V31(this);
            if (!hcNetSDK.NET_DVR_SetDVRMessageCallBack_V31(fMSFCallBack_V31, pUser)) {
                log.error("设置报警回调失败");
                return;
            } else {
                log.info("设置报警回调成功!");
            }
        }
    }


    private IntByReference m_lPort = new IntByReference(-1);

    private static class FRealDataCallBack implements HCNetSDK.FRealDataCallBack_V30 {
        private PlayCtrl playControl;
        private IntByReference m_lPort;

        public FRealDataCallBack(PlayCtrl playControl, IntByReference m_lPort) {
            this.playControl = playControl;
            this.m_lPort = m_lPort;
        }

        //预览回调
        @Override
        public void invoke(int lRealHandle, int dwDataType, ByteByReference pBuffer, int dwBufSize, Pointer pUser) {
//            System.out.println("码流数据回调, 数据类型: " + dwDataType + ", 数据长度:" + dwBufSize);
            //播放库解码
            switch (dwDataType) {
                case HCNetSDK.NET_DVR_SYSHEAD: //系统头
                    if (!playControl.PlayM4_GetPort(m_lPort)) //获取播放库未使用的通道号
                    {
                        break;
                    }
                    if (dwBufSize > 0) {
                        if (!playControl.PlayM4_SetStreamOpenMode(m_lPort.getValue(), PlayCtrl.STREAME_REALTIME))  //设置实时流播放模式
                        {
                            break;
                        }
                        if (!playControl.PlayM4_OpenStream(m_lPort.getValue(), pBuffer, dwBufSize, 1024 * 1024)) //打开流接口
                        {
                            break;
                        }
                        if (!playControl.PlayM4_Play(m_lPort.getValue(), null)) //播放开始
                        {
                            break;
                        }

                    }
                case HCNetSDK.NET_DVR_STREAMDATA:   //码流数据
                    if ((dwBufSize > 0) && (m_lPort.getValue() != -1)) {
                        if (!playControl.PlayM4_InputData(m_lPort.getValue(), pBuffer, dwBufSize))  //输入流数据
                        {
                            break;
                        }
                    }
            }
        }
    }
}
