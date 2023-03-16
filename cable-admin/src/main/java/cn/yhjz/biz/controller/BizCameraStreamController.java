package cn.yhjz.biz.controller;

import cn.yhjz.biz.domain.BizCamera;
import cn.yhjz.biz.domain.BizCameraStream;
import cn.yhjz.biz.service.IBizCameraService;
import cn.yhjz.biz.vo.BizCameraVo;
import cn.yhjz.common.core.domain.AjaxResult;
import cn.yhjz.nio.camera.CameraMessageHandler;
import cn.yhjz.nio.camera.CameraNettyServer;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author maguoping
 */
@RestController
@RequestMapping("/biz/stream")
@Slf4j
public class BizCameraStreamController {

    public static ConcurrentHashMap<String, String> cameraImgMap = new ConcurrentHashMap<>();
    @Autowired
    private IBizCameraService cameraService;


    @RequestMapping("/push")
    public AjaxResult push(@RequestBody BizCameraStream body) {
        //图片的base64
        String imgStr = body.getImgStr();
        String cameraId = body.getCameraId();
        cameraImgMap.put(cameraId, imgStr);
        BizCamera param = new BizCamera();
        param.setDeviceId(cameraId);
        List<BizCameraVo> oldList = cameraService.selectBizCameraList(param);
        if (CollectionUtils.isEmpty(oldList)) {
            BizCamera newCamera = new BizCamera();
            newCamera.setDeviceId(cameraId);
            cameraService.insertBizCamera(newCamera);
        }
        return AjaxResult.success();
    }

    @GetMapping("/preview")
    public AjaxResult preview(String deviceId) {
        String imgStr = cameraImgMap.get(deviceId);
        return AjaxResult.success(imgStr);
    }

    @GetMapping("/getCameraListByAreaCode")
    public AjaxResult getCameraListByAreaCode(String code) {
        BizCamera param = new BizCamera();
        param.setNetworkArea(code);
        List<BizCameraVo> cameraList = cameraService.selectBizCameraList(param);
        return AjaxResult.success(cameraList);
    }

    @RequestMapping("/lailai")
    public AjaxResult lailai(Long cameraId) {
        //发送指令给camera，让它返回一帧
        //根据cameraId获得网络区域信息
        BizCamera camera = cameraService.selectBizCameraById(cameraId);
        String areaCode = camera.getNetworkArea();
        JSONObject msgJson = new JSONObject();
        JSONObject bodyJson = new JSONObject();
        msgJson.put("orderType", "capture");
        msgJson.put("body", bodyJson);
        bodyJson.put("cameraId", cameraId);
        String channelId = CameraMessageHandler.nyChannelMap.get(areaCode);
        CameraNettyServer.sendMessage(channelId, bodyJson.toJSONString());
        return AjaxResult.success();
    }
}
