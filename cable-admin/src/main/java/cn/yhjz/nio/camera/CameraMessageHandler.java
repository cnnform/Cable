package cn.yhjz.nio.camera;

import cn.yhjz.biz.domain.BizAlarm;
import cn.yhjz.biz.domain.BizCamera;
import cn.yhjz.biz.domain.BizStrategyTime;
import cn.yhjz.biz.service.IBizAlarmService;
import cn.yhjz.biz.service.IBizCameraService;
import cn.yhjz.biz.service.IBizStrategyTimeService;
import cn.yhjz.biz.utils.MongodbService;
import cn.yhjz.common.core.redis.RedisCache;
import cn.yhjz.common.utils.StringUtils;
import cn.yhjz.websocket.WsServerEndpoint;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * camera工程上报的消息处理类
 *
 * @author ldl
 */
@Component
@Slf4j
public class CameraMessageHandler {

    @Autowired
    private IBizCameraService iBizCameraService;

    @Autowired
    private IBizAlarmService iBizAlarmService;

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IBizStrategyTimeService strategyTimeService;
    public final static int MAX_ALERT_COUNT = 50;
    @Autowired
    private MongodbService mongodbService;
    private static String pythonUrl;

    @Value("${cable.python_url}")
    public void setConfig1(String pythonUrl) {
        CameraMessageHandler.pythonUrl = pythonUrl;
    }

    /**
     * 摄像头和websocket的关系，一个摄像头会对应多个websocket
     */
    private static ConcurrentHashMap<String, List<WsServerEndpoint>> wsMap = new ConcurrentHashMap<String, List<WsServerEndpoint>>(16);

    private static ConcurrentHashMap<String, CameraPreviewThread> CameraPreviewThreadMap = new ConcurrentHashMap<String, CameraPreviewThread>(16);

    //摄像头id的map
    public static ConcurrentHashMap<String, BizCamera> cameraDeviceIdMap = new ConcurrentHashMap<String, BizCamera>(16);


    /**
     * netty客户端
     */
    public static ConcurrentHashMap<String, String> nyChannelMap = new ConcurrentHashMap<String, String>(16);

    /**
     * 处理客户端发送过来的消息
     *
     * @param channelId > sessionId
     * @param data      > 数据
     */
    public void handle(String channelId, String data) {
        JSONObject jsonData = JSON.parseObject(data);
        String orderType = jsonData.getString("orderType");
        if ("region".equals(orderType)) {
            JSONObject body = jsonData.getJSONObject("body");
            String areaCode = body.getString("networkArea");
            nyChannelMap.put(areaCode, channelId);
        } else {
            handle0(data);
        }
    }

    public void handle0(String data) {
        JSONObject jsonData = JSON.parseObject(data);
        String orderType = jsonData.getString("orderType");
        if ("push_frame".equals(orderType)) {
            //接收摄像头推送过来的一帧图像
            try {
                JSONObject body = jsonData.getJSONObject("body");
                String cameraId = body.getString("cameraId");
                String deviceId = body.getString("deviceId");
                String imgBase64 = body.getString("imgBase64");
                CameraPreviewThread cameraThread = CameraPreviewThreadMap.get(deviceId);
                try {
                    if (StringUtils.isNotEmpty(imgBase64)) {
                        // 判断imgBase64是否符合格式
                        if ("__fail__".equals(imgBase64)) {
                            //__fail__代表截图失败（异常、图片格式错误）

                        }
                        //判断是否符合时间段策略
                        boolean strategyRes = this.filterTimeStrategy(deviceId);
                        sendMsg2WS(deviceId, jsonData.toJSONString());
                    }
                } catch (Exception expt) {
                    log.error("{}", expt);
                    if (cameraThread != null) {
                        cameraThread.isFree = true;
                    }
                }
                if (cameraThread != null) {
                    cameraThread.isFree = true;
                }
            } catch (Exception exp) {
                log.error("接收截图发生异常：{}", exp);
            }

        } else if ("get_ptz_control".equals(orderType)) {
            JSONObject body = jsonData.getJSONObject("body");
            String deviceId = body.getString("deviceId");
            // 海康威视
            // 垂直区间: [-1, 1], 度数: 90 ~ 0
            // 水平区间: [-1, 1], 度数: 180~ 270 ~ 0 ~ 180

            double ps = body.getDouble("wPanPos");
            Double wPanPos = ps;
            // 垂直 90 : (left) +1 ~ -1(right)
            double ts = body.getDouble("wTiltPos");
            double t = (1 - ts) * 45;
            long wTiltPos = Math.round(t);
            body.put("wPanPos", PtzUtil.convertPTZ2degreeP(wPanPos));
            body.put("wTiltPos", wTiltPos);
            BizCamera bizCamera = cameraDeviceIdMap.get(deviceId);
            if (null == bizCamera) {
                bizCamera = iBizCameraService.selectBizCameraByDeviceId(deviceId);
                cameraDeviceIdMap.put(deviceId, bizCamera);
            }
            bizCamera.setId(bizCamera.getId());
            bizCamera.setPtzPanpos(body.getLong("wPanPos"));
            bizCamera.setPtzTiltpos(body.getLong("wTiltPos"));
            bizCamera.setPtzZoompos(body.getLong("wZoomPos"));
            iBizCameraService.updateBizCameraPTZ(bizCamera);
            BizCamera NewBizCamera = iBizCameraService.selectBizCameraByDeviceId(deviceId);
            cameraDeviceIdMap.put(deviceId, NewBizCamera);
            // 存入缓存
//                redisCache.deleteObject("get_ptz_control");
//                List<BizCamera> bizCameraList = iBizCameraService.selectBizCameraList(null);
//                redisCache.setCacheList("get_ptz_control", bizCameraList);
//                redisCache.expire("get_ptz_control", 5, TimeUnit.MINUTES);

            // 发送ptz信息给页面
            sendMsg2WS(deviceId, jsonData.toJSONString());
        }
    }

    /**
     * 判断一个摄像头是否符合时间段策略，
     * 返回true代表符合，进而去调用目标识别服务
     * 返回false代表不需要目标识别服务
     *
     * @return
     */
    public boolean filterTimeStrategy(String deviceId) {
        BizCamera camera = this.iBizCameraService.selectBizCameraByDeviceId(deviceId);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("camera_id", camera.getId());

        BizStrategyTime strategyTime = this.strategyTimeService.getOne(queryWrapper);
        if (strategyTime == null) {
            //如果没有时间策略就不判断了，全天候目标识别
            return true;
        }
        //得到开始时间和结束时间
        String startTimeStr = strategyTime.getStartTime();
        String endTimeStr = strategyTime.getEndTime();
        if ("24:00".equals(endTimeStr)) {
            endTimeStr = "23:59:59";
        }
        if (StringUtils.isEmpty(startTimeStr) || StringUtils.isEmpty(endTimeStr)) {
            //数据错误，返回true相当于当前规则无效，按默认处理
            return true;
        }
        LocalTime startTime = LocalTime.parse(startTimeStr);
        LocalTime endTime = LocalTime.parse(endTimeStr);
        LocalTime nowTime = LocalTime.now();
        if (nowTime.isAfter(startTime) && nowTime.isBefore(endTime)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 开启预览功能，启动一个线程
     */
    public void startPreview(JSONObject jsonData, WsServerEndpoint wsServerEndpoint) {
        jsonData.put("orderType", "capture");
        JSONObject body = jsonData.getJSONObject("body");
        String areaCode = body.getString("networkArea");
        String cameraId = body.getString("cameraId");
        String deviceId = body.getString("deviceId");
        // 把websocket的实例跟摄像头id对应在一个map中，websocket保存的是一个列表，因为不同的用户可能会预览同一个摄像头
        if (CollectionUtils.isEmpty(wsMap.get(deviceId))) {
            List<WsServerEndpoint> wsServerEndpoints = new ArrayList<>();
            wsServerEndpoints.add(wsServerEndpoint);
            wsMap.put(deviceId, wsServerEndpoints);
        } else {
            List<WsServerEndpoint> wsServerEndpoints = wsMap.get(deviceId);
            if (!wsServerEndpoints.contains(wsServerEndpoint)) {
                wsServerEndpoints.add(wsServerEndpoint);
                wsMap.put(deviceId, wsServerEndpoints);
            }
        }
        //创建一个线程用于控制摄像头的预览过程
        /*String channelId = nyChannelMap.get(areaCode);
        if (!StringUtils.isEmpty(channelId)) {
            CameraPreviewThread cameraPreviewThread = new CameraPreviewThread(channelId, Long.valueOf(cameraId));
            CameraPreviewThreadMap.put(cameraId, cameraPreviewThread);
            cameraPreviewThread.start();
        }*/
    }

    /**
     * 刷新网络区域内的摄像头连接
     */
    public void refreshHttp(String networkArea) {
        JSONObject jsonObject = new JSONObject(2);
        jsonObject.put("orderType", "re_init_http");
        JSONObject body = new JSONObject(2);
        body.put("networkArea", networkArea);
        jsonObject.put("body", body);
        String channelId = nyChannelMap.get(networkArea);
        if (!StringUtils.isEmpty(channelId)) {
            CameraNettyServer.sendMessage(channelId, jsonObject.toJSONString());
        }
    }

    /**
     * 云台设置PTZ参数
     */
    public void setPtzControl(JSONObject jsonData) {
        JSONObject body = jsonData.getJSONObject("body");
        String networkArea = body.getString("networkArea");
        long wPanPos = body.getLong("wPanPos");
        long wTiltPos = body.getLong("wTiltPos");
        // 垂直区间: [-1, 1], 度数: 90 ~ 0
        // 水平区间: [-1, 1], 度数: 180~ 270 ~ 0 ~ 180
        double ps;
        if (wPanPos > 180) {
            ps = -(360.0 - wPanPos) / 180;
        } else {
            ps = wPanPos / 180.0;
        }
        log.info("目标角度P:{}", ps);
        double ts = wTiltPos * (1.0 / 45);
        ts = 1 - ts;
        body.put("wPanPos", ps);
        body.put("wTiltPos", ts);
        String channelId = nyChannelMap.get(networkArea);
        if (!StringUtils.isEmpty(channelId)) {
            CameraNettyServer.sendMessage(channelId, jsonData.toJSONString());
        }
    }

    /**
     * 主动发送消息
     */
    public static void sendNyByWs(JSONObject jsonData) {
        JSONObject body = jsonData.getJSONObject("body");
        String areaCode = body.getString("networkArea");
        String cameraId = body.getString("cameraId");
        List<WsServerEndpoint> wsServerEndpoints = wsMap.get(cameraId);
        if (wsServerEndpoints.size() != 0) {
            String channelId = nyChannelMap.get(areaCode);
            if (!StringUtils.isEmpty(channelId)) {
                CameraNettyServer.sendMessage(channelId, jsonData.toJSONString());
            }
        }
    }


    /**
     * 循环发送消息到websocket
     */
    public void sendMsg2WS(String deviceId, String data) {
        List<WsServerEndpoint> wsServerEndpoints = wsMap.get(deviceId);
        if (!CollectionUtils.isEmpty(wsServerEndpoints)) {
            for (WsServerEndpoint w : wsServerEndpoints) {
                if (WsServerEndpoint.getWebSocketSet().contains(w)) {
                    try {
                        log.info("传给websocket,{}", deviceId);
                        w.sendMessage(data);
                    } catch (IOException e) {
                        log.error("sendMessage fail!");
                    }
                } else {
                    wsServerEndpoints.remove(w);
                }
            }
        }
    }

    /**
     * 通过WS关闭预览
     */
    public void stopPreviewByWs(JSONObject jsonData, WsServerEndpoint wsServerEndpoint) {
        JSONObject body = jsonData.getJSONObject("body");
        String cameraId = body.getString("cameraId");
        String deviceId = body.getString("deviceId");
        List<WsServerEndpoint> wsServerEndpoints = wsMap.get(deviceId);
        wsServerEndpoints.remove(wsServerEndpoint);
        CameraPreviewThread cameraThread = CameraPreviewThreadMap.get(deviceId);
        if (cameraThread != null) {
            cameraThread.exit = true;
            CameraPreviewThreadMap.remove(deviceId);
        }
    }

    /**
     * 服务端主动关闭预览
     */
    public void stopPreview(String deviceId) {
        CameraPreviewThread cameraThread = CameraPreviewThreadMap.get(deviceId);
        if (cameraThread != null) {
            cameraThread.exit = true;
            CameraPreviewThreadMap.remove(deviceId);
        }
    }

    /**
     * 异常关闭预览
     */
    public static void stopPreviewByTry(WsServerEndpoint wsServerEndpoint) {
        for (String key : wsMap.keySet()) {
            List<WsServerEndpoint> wsServerEndpoints = wsMap.get(key);
            if (wsServerEndpoints.contains(wsServerEndpoint)) {
                wsServerEndpoints.remove(wsServerEndpoint);
            }
            CameraPreviewThread cameraThread = CameraPreviewThreadMap.get(key);
            if (cameraThread != null) {
                cameraThread.exit = true;
                CameraPreviewThreadMap.remove(key);
            }
        }
    }

    /**
     * 调用目标识别服务，获得检测结果
     *
     * @param base64Str
     * @return
     */
    public JSONObject prePython(String base64Str, Long cameraId) {
        return prePython(base64Str, cameraId, false);
    }

    public JSONObject prePython(String base64Str, Long cameraId, boolean action) {
        OkHttpClient httpClient = new OkHttpClient();
        JSONObject paramJson = new JSONObject();
        paramJson.put("img_str", base64Str);
        //同时传递报警区域
        //我们假设4个边，点的坐标用百分比表示，左上角为原点
        //从数据库中获得多边形的点
        if (!action) {
            JSONObject resJson = new JSONObject();
            resJson.put("imageBase64", base64Str);
            resJson.put("success", true);
            return resJson;
        }
        BizCamera bizCamera = iBizCameraService.selectBizCameraById(cameraId);
        if (StringUtils.isNotEmpty(bizCamera.getAlertRegions())) {
            JSONArray jsonArray = JSONArray.parseArray(bizCamera.getAlertRegions());
            paramJson.put("edge", jsonArray);
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                paramJson.toJSONString());
        Request request = new Request.Builder()
                .url(pythonUrl).post(body)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            JSONObject responseJson = JSONObject.parseObject(response.body().string());
            //把字段都取出来，转一下
            byte[] imageBase64 = responseJson.getBytes("imageBase64");
            Boolean success = responseJson.getBoolean("success");
            JSONObject eventJson = responseJson.getJSONObject("event");
            JSONObject resJson = new JSONObject();
            resJson.put("imageBase64", imageBase64);
            resJson.put("event", eventJson);
            resJson.put("success", success);
            return resJson;
        } catch (IOException e) {
            log.error("请求目标识别服务失败");
            return null;
        }
    }
}
