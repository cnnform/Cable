package cn.yhjz.biz.controller;

import cn.yhjz.biz.domain.BizCamera;
import cn.yhjz.biz.service.IBizCameraService;
import cn.yhjz.common.core.domain.AjaxResult;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
@RequestMapping("/video")
public class BizVideoCamera {

    public static ConcurrentHashMap<String, Process> processMap = new ConcurrentHashMap<>();

    @Autowired
    private IBizCameraService bizCameraService;
    @RequestMapping("stop_video")
    public AjaxResult stopVideo(String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return AjaxResult.error("参数错误");
        }
        Process process = BizVideoCamera.processMap.get(deviceId);
        if(process!=null){
            process.destroy();
            processMap.remove(deviceId);
            log.info("停止ffmpeg进程,{}",deviceId);
        }
        return AjaxResult.success();
    }
    /**
     * 开始拉流
     *
     * @return
     */
    @RequestMapping("startPush_cv")
    public AjaxResult startPushCv(String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return AjaxResult.error("参数错误");
        }

        return AjaxResult.success();
    }

    /**
     * 开始拉流
     *
     * @return
     */
    @RequestMapping("startPushCmd")
    public AjaxResult startPushCmd(String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return AjaxResult.error("参数错误");
        }
        Process oldProcess = processMap.get(deviceId);
        if (oldProcess != null) {
            return AjaxResult.success();
        }
        // 从数据库中查找摄像头的信息
        BizCamera camera = bizCameraService.selectBizCameraByDeviceId(deviceId);
        //找到camera的rtsp地址
        String rtspAddress = camera.getJslRtsp();
        if (StringUtils.isEmpty(rtspAddress)) {
            return AjaxResult.error("此摄像头未配置rtsp地址");
        }
        new Thread(()->{
            try {
                Process process = new ProcessBuilder(
                        "ffmpeg", "-rtsp_transport", "tcp", "-thread_queue_size", "128", "-i", rtspAddress, "-q", "0", "-f", "mpegts", "-codec:v",
                        "mpeg1video", "-s", "1280x720", "http://localhost:8888/push/"+deviceId,"-loglevel","quiet").start();
                log.info("开启ffmpeg进程");
                processMap.put(deviceId, process);
                log.info("ffmpeg进程自动结束,{}");
            } catch (IOException e) {
                log.error("启动直播进程失败,{}", e);
            }
        }).start();


//
        return AjaxResult.success();


    }
}
