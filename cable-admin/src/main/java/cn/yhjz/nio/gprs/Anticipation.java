package cn.yhjz.nio.gprs;

import cn.yhjz.biz.domain.BizDevice;
import cn.yhjz.biz.service.IBizDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 预处理工具类
 *
 * @author ldl
 */
@Component
@Slf4j
public class Anticipation {

    @Autowired
    private IBizDeviceService bizDeviceService;

    /**
     * Biz预处理
     *
     * @param data > 数据
     */
    public void init(Object data) {
        addBizDecice(String.valueOf(data));
    }

    /**
     * 预处理新增的定位设备
     *
     * @param data > 数据
     */
    private void addBizDecice(String data) {
        //$GNGGA
        if (data.length() > 128) {
            return;
        }
        int startIndex = data.indexOf("$GNGGA");
        if (startIndex >= 0) {
            //分出设备id和gngga数据包
            String deviceId = data.substring(0, startIndex);
            String gagga = data.substring(startIndex);

            String[] strs = gagga.split(",");
            BizDevice bizDevice = new BizDevice();
            bizDevice.setDeviceId(deviceId);
            String lonStr = (strs[4]);
            String latStr = (strs[2]);
            String altitudeStr = strs[9];
            //如果没有经纬度不用处理
            if (StringUtils.isEmpty(lonStr) || StringUtils.isEmpty(latStr)) {
                return;
            }
            try {
                //把字符串转成double
                Double lon = Double.valueOf(lonStr);
                Double lat = Double.valueOf(latStr);
                Double altitude = Double.valueOf(altitudeStr);
                //单位换算
                Double lonMinute = lon % 100;
                Double latMinute = lat % 100;
                Double lonInteger = lon - lonMinute;
                Double latInteger = lat - latMinute;
                lon = lonInteger / 100 + lonMinute / 60;
                lat = latInteger / 100 + latMinute / 60;
                bizDevice.setLon(lon);
                bizDevice.setLat(lat);
            } catch (NumberFormatException exception) {
                log.error("获得经纬度，解析成数字失败");
            }
            bizDevice.setCreateBy("_sys_");
            bizDeviceService.saveDeviceLocation(bizDevice);

        } else {
            //可能是心跳包
        }
        if (data.startsWith("data:")) {

        }
    }

}
