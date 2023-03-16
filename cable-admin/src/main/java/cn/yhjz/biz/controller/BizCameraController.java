package cn.yhjz.biz.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import cn.yhjz.biz.config.CameraMqttCommander;
import cn.yhjz.biz.domain.BizCable;
import cn.yhjz.biz.domain.BizCamera;
import cn.yhjz.biz.domain.BizStrategyTime;
import cn.yhjz.biz.jinsanli.JslCamera;
import cn.yhjz.biz.jinsanli.JslConfig;
import cn.yhjz.biz.service.IBizCableService;
import cn.yhjz.biz.service.IBizCameraService;
import cn.yhjz.biz.service.IBizStrategyTimeService;
import cn.yhjz.biz.utils.LonAndLatUtil;
import cn.yhjz.biz.vo.BizCableVo;
import cn.yhjz.biz.vo.BizCameraVo;
import cn.yhjz.common.annotation.Log;
import cn.yhjz.common.core.controller.BaseController;
import cn.yhjz.common.core.domain.AjaxResult;
import cn.yhjz.common.core.page.TableDataInfo;
import cn.yhjz.common.core.redis.RedisCache;
import cn.yhjz.common.enums.BusinessType;
import cn.yhjz.common.utils.StringUtils;
import cn.yhjz.common.utils.poi.ExcelUtil;
import cn.yhjz.nio.camera.CameraMessageHandler;
import cn.yhjz.nio.camera.CameraNettyServer;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 摄像头管理Controller
 *
 * @author yhjz
 * @date 2022-04-12
 */
@RestController
@RequestMapping("/biz/camera")
public class BizCameraController extends BaseController {

    @Autowired
    private IBizCameraService bizCameraService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IBizStrategyTimeService bizStrategyTimeService;

    @Autowired
    private IBizCableService cableService;

    @Autowired
    private CameraMqttCommander mqttCommander;

    @Autowired
    private JslConfig jslConfig;

    /**
     * 查询摄像头管理列表
     */
    @PreAuthorize("@ss.hasPermi('biz:camera:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizCamera bizCamera) {
        startPage();
        List<BizCameraVo> list = bizCameraService.selectBizCameraList(bizCamera);
        return getDataTable(list);
    }

    /**
     * 导出摄像头管理列表
     */
    @PreAuthorize("@ss.hasPermi('biz:camera:export')")
    @Log(title = "摄像头管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizCamera bizCamera) {
        List<BizCameraVo> list = bizCameraService.selectBizCameraList(bizCamera);
        ExcelUtil<BizCameraVo> util = new ExcelUtil<>(BizCameraVo.class);
        util.exportExcel(response, list, "摄像头管理数据");
    }

    /**
     * 获取摄像头管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:camera:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(bizCameraService.selectBizCameraById(id));
    }

    /**
     * 新增摄像头管理
     */
    @PreAuthorize("@ss.hasPermi('biz:camera:add')")
    @Log(title = "摄像头管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizCamera bizCamera) {
        bizCamera.setCreateBy(getUsername());
        return toAjax(bizCameraService.insertBizCamera(bizCamera));
    }

    /**
     * 修改摄像头管理
     */
    @PreAuthorize("@ss.hasPermi('biz:camera:edit')")
    @Log(title = "摄像头管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizCamera bizCamera) {
        return toAjax(bizCameraService.updateBizCamera(bizCamera));
    }

    /**
     * 删除摄像头管理
     */
    @PreAuthorize("@ss.hasPermi('biz:camera:remove')")
    @Log(title = "摄像头管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bizCameraService.deleteBizCameraByIds(ids));
    }

    @GetMapping("/preview")
    public AjaxResult preview(String deviceId) {
        String imgStr = BizCameraStreamController.cameraImgMap.get(deviceId);
        return AjaxResult.success(imgStr);
    }

    /**
     * 设置摄像头发现目标位置
     */
    @PostMapping("/targetNotice")
    public AjaxResult targetNotice(@RequestBody BizCameraVo bizCameraVo) {

        JSONObject jsonData = new JSONObject();
        jsonData.put("orderType", "set_ptc_control");
        JSONObject body = new JSONObject();
        body.put("cameraId", bizCameraVo.getId());
        body.put("networkArea", bizCameraVo.getNetworkArea());

        double targetDegrees = LonAndLatUtil.bearing(bizCameraVo.getLon(), bizCameraVo.getLat(), bizCameraVo.getTargetLon(), bizCameraVo.getTargetLat()) + bizCameraVo.getPtzAngle();

        body.put("wPanPos", targetDegrees);
        body.put("wTiltPos", bizCameraVo.getPtzTiltpos());
        body.put("wZoomPos", bizCameraVo.getPtzZoompos());
        jsonData.put("body", body);

        String channelId = CameraMessageHandler.nyChannelMap.get(bizCameraVo.getNetworkArea());
        if (!StringUtils.isEmpty(channelId)) {
            CameraNettyServer.sendMessage(channelId, jsonData.toJSONString());
        }

//        bizCameraService.updateBizCamera(bizCamera);

        return AjaxResult.success();
    }

    /**
     * 设置目标点
     */
    @PostMapping("/smartGloble")
    public AjaxResult smartGloble(@RequestBody BizCameraVo bizCameraVo) {
        BizCamera bizCamera = new BizCamera();
        List<BizCameraVo> bizCameraList = bizCameraService.selectBizCameraList(bizCamera);

        JSONObject jsonData = new JSONObject();
        jsonData.put("orderType", "set_ptz_control");
        JSONObject body = new JSONObject();

        for (BizCameraVo b : bizCameraList) {
            if (b.getLon() != null && b.getLat() != null && b.getPtzTiltpos() != null && b.getPtzZoompos() != null) {
                body.put("cameraId", b.getId());
                body.put("deviceId", b.getDeviceId());
                body.put("networkArea", b.getNetworkArea());
                logger.info("{},{},{},{},{}", b.getId(), b.getLon(), b.getLat(), bizCameraVo.getTargetLon(), bizCameraVo.getTargetLat());
                double targetDegrees = LonAndLatUtil.bearing(b.getLon(), b.getLat(), bizCameraVo.getTargetLon(), bizCameraVo.getTargetLat());
                targetDegrees %= 360;
                logger.info("BizCamera Id: {}, targetLon: {}, targetLat: {}, targetDegrees: {}", b.getId(), bizCameraVo.getTargetLon(), bizCameraVo.getTargetLat(), targetDegrees);
                body.put("wPanPos", targetDegrees - b.getPtzAngle());
                body.put("wTiltPos", b.getPtzTiltpos());
                body.put("wZoomPos", b.getPtzZoompos());
                jsonData.put("body", body);
                mqttCommander.setPtzControl(jsonData);
//                CameraMessageHandler cameraAnticipation = new CameraMessageHandler();
//                cameraAnticipation.setPtzControl(jsonData);
                b.setPtzPanpos(Math.round(targetDegrees - b.getPtzAngle()));
            }
        }
        return AjaxResult.success(bizCameraList);
    }

    /**
     * 获取设备的PTZ转向
     */
    @GetMapping("/getPtzControl")
    public AjaxResult getPtcControl(@RequestParam(required = false) String deviceId) {
        // 缓存中取出
        if (StringUtils.isNotEmpty(deviceId)) {
            BizCamera camera = CameraMessageHandler.cameraDeviceIdMap.get(deviceId);
            return AjaxResult.success(camera);
        }
        Set<String> keySet = CameraMessageHandler.cameraDeviceIdMap.keySet();
        List<BizCamera> bizCameraList = new ArrayList<>();
        for (String key : keySet) {
            bizCameraList.add(CameraMessageHandler.cameraDeviceIdMap.get(key));
        }
        return AjaxResult.success(bizCameraList);
    }

    /**
     * 设置一个摄像头的报警时间段
     *
     * @return
     */
    @PostMapping("/setStrategyTime")
    @PreAuthorize("@ss.hasPermi('biz:camera:edit')")
    public AjaxResult setStrategyTime(Long cameraId, String startTime, String endTime) {
        //先查询看有没有
        BizStrategyTime strategyTime = new BizStrategyTime();
        strategyTime.setCameraId(cameraId);
        QueryWrapper<BizStrategyTime> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("camera_id", cameraId);
        BizStrategyTime old = bizStrategyTimeService.getOne(queryWrapper);
        if (old != null) {
            strategyTime.setId(old.getId());
        }
        strategyTime.setStartTime(startTime);
        strategyTime.setEndTime(endTime);
        strategyTime.setUpdateTime(new Date());
        bizStrategyTimeService.saveOrUpdate(strategyTime);
        return AjaxResult.success();
    }

    @PostMapping("/loadStrategyTime")
    @PreAuthorize("@ss.hasPermi('biz:camera:query')")
    public AjaxResult loadStrategyTime(Long cameraId) {
        //先查询看有没有
        BizStrategyTime strategyTime = new BizStrategyTime();
        strategyTime.setCameraId(cameraId);
        QueryWrapper<BizStrategyTime> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("camera_id", cameraId);
        BizStrategyTime old = bizStrategyTimeService.getOne(queryWrapper);
        return AjaxResult.success(old);
    }

    @PostMapping("/setRegions")
    @PreAuthorize("@ss.hasPermi('biz:camera:edit')")
    public AjaxResult setRegions(@RequestBody BizCamera camera) {
        this.bizCameraService.updateBizCamera(camera);
        return AjaxResult.success();
    }

    /**
     * 根据摄像头id获得摄像头的详细信息
     *
     * @return
     */
    @RequestMapping("/loadCameraDetail")
    @PreAuthorize("@ss.hasPermi('biz:camera:query')")
    public AjaxResult loadCameraDetail(Long cameraId) {
        BizCamera camera = this.bizCameraService.selectBizCameraById(cameraId);
        BizCameraVo vo = new BizCameraVo();
        BeanUtils.copyProperties(camera, vo);
        //获得摄像头的线路信息
        Long cableId = vo.getCableId();
        BizCable cableParam = new BizCable();
        cableParam.setId(cableId);
        List<BizCableVo> cableList = cableService.selectBizCableList(cableParam);
        if (CollectionUtils.isNotEmpty(cableList)) {
            vo.setCable(cableList.get(0));
        }
        return AjaxResult.success(vo);
    }


    /**
     * 控制金三立摄像头
     * @param direct 方向 up down left right
     * @param stop
     * @return
     */
    @RequestMapping("/jsl-turn")
    public AjaxResult jslTurn(String deviceId,String direct,String stop){
        JslCamera jslCamera = JslCamera.jslCameraMap.get(deviceId);
        if (null == jslCamera) {
            //如果此摄像头没有加载，就从数据库中加载
            BizCamera bizCamera = this.bizCameraService.selectBizCameraByDeviceId(deviceId);
            if (bizCamera != null) {
                jslCamera = new JslCamera();
                jslCamera.setUsername(bizCamera.getJslUsername());
                jslCamera.setPassword(bizCamera.getJslPassword());
                jslCamera.setDeviceId(bizCamera.getDeviceId());
                jslCamera.init(jslConfig,bizCamera.getJslGuid(), bizCamera.getJslClientId(), bizCamera.getJslClientSecret());
                JslCamera.jslCameraMap.put(deviceId, jslCamera);
            }
        }
        if (null != jslCamera) {
            jslCamera.turn(direct, stop);
            return AjaxResult.success();
        }
        return AjaxResult.error("未找到摄像头");
    }


    /**
     * 系统初始化的时候把所有摄像头的信息放到缓存中
     */
    @PostConstruct
    public void cacheAllCameras(){
        logger.info("初始化摄像头缓存");
        List<BizCameraVo> bizCameraVoList = this.bizCameraService.selectBizCameraList(null);
        List<BizCamera> bizCameraList = bizCameraVoList.stream().map((vo)->{
            BizCamera entity = new BizCamera();
            BeanUtils.copyProperties(vo, entity);
            return entity;
        }).collect(Collectors.toList());
        for (BizCamera camera : bizCameraList) {
            CameraMessageHandler.cameraDeviceIdMap.put(camera.getDeviceId(), camera);
        }
        logger.info("初始化摄像头缓存完毕");
    }
}
