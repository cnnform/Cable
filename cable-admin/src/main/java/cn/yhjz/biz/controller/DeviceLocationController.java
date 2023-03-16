package cn.yhjz.biz.controller;

import cn.yhjz.biz.domain.BizDevice;
import cn.yhjz.biz.service.IBizDeviceService;
import cn.yhjz.common.core.domain.AjaxResult;
import cn.yhjz.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/devicelocation")
@Slf4j
public class DeviceLocationController {

    @Autowired
    private IBizDeviceService bizDeviceService;

    /**
     * 设备上报
     *
     * @return
     */
    @RequestMapping("/push")
    public AjaxResult push(@RequestBody BizDevice bizDevice) {
        if (StringUtils.isEmpty(bizDevice.getDeviceId())) {
            return AjaxResult.error("缺少参数deviceId");
        }
        if (null == bizDevice.getLat() || null == bizDevice.getLon()) {
            return AjaxResult.error("经纬度参数错误");
        }
        if (null == bizDevice.getDepth()) {
            bizDevice.setDepth(0.0d);
        }
        bizDeviceService.saveDeviceLocation(bizDevice);
        return AjaxResult.success();
    }
}
