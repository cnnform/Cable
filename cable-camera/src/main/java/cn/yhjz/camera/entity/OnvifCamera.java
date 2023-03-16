package cn.yhjz.camera.entity;

import be.teletask.onvif.OnvifManager;
import be.teletask.onvif.listeners.OnvifResponseListener;
import be.teletask.onvif.models.OnvifDevice;
import be.teletask.onvif.models.OnvifMediaProfile;
import be.teletask.onvif.responses.OnvifResponse;
import cn.yhjz.camera.util.PtzUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class OnvifCamera extends AbstractCamera {
    private OnvifManager onvifManager;

    private String snapshotURI;
    private OnvifMediaProfile mediaProfile;

    private OnvifDevice onvifDevice;
    //创建静态的httpclient，供全局使用
    private final static OkHttpClient httpClient = new OkHttpClient();

    @Override
    public void init() {
        log.info("onvif 开始连接摄像头{}", this.getDeviceIp());
        this.onvifManager = new OnvifManager(new OnvifResponseListener() {
            @Override
            public void onResponse(OnvifDevice onvifDevice, OnvifResponse onvifResponse) {
//                log.debug("PTZ response: " + onvifResponse.getXml());
//                lastFailures.remove(onvifDevice.getHostName());
            }

            @Override
            public void onError(OnvifDevice onvifDevice, int errorCode, String errorMsg) {

                log.info("错误了，{},{}", errorCode, errorMsg);
//                if (shouldLogFailure(onvifDevice)) {
//                    log.warn("PTZ response err [{}]: {} ", errorCode, errorMsg);
//                    lastFailures.put(onvifDevice.getHostName(), Instant.now());
//                }
            }
        });

        OnvifDevice device = new OnvifDevice(this.getDeviceIp(), this.getUserName(), this.getPassword());
        this.onvifDevice = device;
        //获得所有的service列表
        onvifManager.getServices(device, (onvifDevice, services) -> {
            //获得media的配置，里面包含着token
            onvifManager.getMediaProfiles(device, (device1, mediaProfiles) -> {
                this.mediaProfile = mediaProfiles.get(0);
                onvifManager.getMediaStreamURI(device1, mediaProfiles.get(0), (device112, profile, uri) -> {
                    log.info("onvif 获得摄像头的拉流地址：{}", uri);
                });
                onvifManager.getSnapshotURI(device1, mediaProfiles.get(0), (device11, profile, uri) -> {
                    log.info("onvif 获得摄像头的截图地址：{}", uri);
                    this.snapshotURI = uri;
                });

                this.setIsLogin(true);
                this.setIsOnline(true);

            });
        });
        onvifManager.getDeviceInformation(device, (device12, deviceInformation) ->
                log.info("onDeviceInformationReceived：{}", deviceInformation)
        );
    }

    public void destroy(){
        try {
            this.onvifManager.destroy();
        } catch (Exception exception) {
            log.error("destroy出现异常");
        }
    }
    /**
     * 从摄像头截图
     *
     * @return
     */
    @Override
    public String capture() {
        String c = Credentials.basic(this.getUserName(), this.getPassword());
        Request request = new Request.Builder()
                .url(this.snapshotURI).get().header("Authorization", c)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            byte[] responseBody = response.body().bytes();
            String base64 = Base64.getEncoder().encodeToString(responseBody);
            return base64;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        this.setIsLogin(false);
        this.setIsOnline(false);
        onvifManager.destroy();
    }

    @Override
    public void setPTZ(Double pan, Double tilt, Double zoom) {
        onvifManager.absoluteMove(this.onvifDevice, this.mediaProfile, pan, tilt, zoom);
    }

    @Override
    public JSONObject getPTZ() {
        CompletableFuture<JSONObject> completableFuture = new CompletableFuture<>();
        Runnable runnable = () ->
                onvifManager.getStatus(this.onvifDevice, this.mediaProfile, (onvifDevice, profile, status) -> {
//                    log.info("onStatusReceived: pan: {}, tilt: {}, zoom: {}.", status.getPan(), status.getTilt(), status.getZoom());
                    JSONObject bodyJson = new JSONObject();
                    //转换成角度，p换成
                    bodyJson.put("wPanPos", PtzUtil.convertPTZ2degreeP(status.getPan()));
                    bodyJson.put("wTiltPos", PtzUtil.convertPTZ2degreeT(status.getTilt()));
                    bodyJson.put("wZoomPos", status.getZoom());
                    completableFuture.complete(bodyJson);
                });
        Thread t1 = new Thread(runnable);
        t1.start();//启动子线程
        try {
            JSONObject result = completableFuture.get();//主线程阻塞，等待完成
//            log.info("从摄像头获得的坐标：{}", result);
            return result;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}
