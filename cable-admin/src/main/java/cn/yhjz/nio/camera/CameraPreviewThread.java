package cn.yhjz.nio.camera;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * @author maguoping
 * 摄像头预览线程，用于控制预览过程
 */
@Slf4j
public class CameraPreviewThread extends Thread {

    /**
     * volatile修饰符用来保证其它线程读取的总是该变量的最新的值
     */
    public volatile boolean exit = false;

    /**
     * 上一次的预览请求是否处理完
     */
    public volatile boolean isFree = true;

    private String channelId;
    private Long cameraId;

    public CameraPreviewThread(String channelId, Long cameraId) {
        this.channelId = channelId;
        this.cameraId = cameraId;
    }

    @Override
    public void run() {


//        JSONObject jsonObject = new JSONObject(2);
//        jsonObject.put("orderType", "get_ptz_control");
//        jsonObject.put("body", jsonData.getJSONObject("body"));
//        CameraNettyServer.sendMessage(channelId, jsonObject.toJSONString());

        while (!exit) {
            if (isFree) {
                try {
                    JSONObject jsonData = new JSONObject();
                    JSONObject body = new JSONObject();
                    jsonData.put("orderType", "capture");
                    jsonData.put("body", body);
                    body.put("cameraId", cameraId);
                    CameraNettyServer.sendMessage(channelId, jsonData.toJSONString());
                    isFree = false;
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    log.info("等待超时异常,  {}", e.getMessage());
                }
            }
        }
    }

}
