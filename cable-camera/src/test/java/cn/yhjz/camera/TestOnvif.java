package cn.yhjz.camera;

import be.teletask.onvif.DiscoveryManager;
import be.teletask.onvif.OnvifManager;
import be.teletask.onvif.listeners.*;
import be.teletask.onvif.models.*;
import be.teletask.onvif.responses.OnvifResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.util.List;

@SpringBootApplication
@Slf4j
public class TestOnvif {

    /**
     * 测试发现设备
     */
    @Test
    public void testDiscovery() {
        DiscoveryManager manager = new DiscoveryManager();
        manager.setDiscoveryTimeout(10000);
        manager.discover(new DiscoveryListener() {
            @Override
            public void onDiscoveryStarted() {
                log.info("Discovery started");
            }

            @Override
            public void onDevicesFound(List<Device> devices) {
                for (Device device : devices)
                    log.info("Devices found: " + device.getHostName());
            }
        });
    }

    @Test
    public void testDevice() {
        OnvifManager onvifManager = new OnvifManager(new OnvifResponseListener() {
            public void onResponse(OnvifDevice onvifDevice, OnvifResponse onvifResponse) {
//                log.debug("PTZ response: " + onvifResponse.getXml());
//                lastFailures.remove(onvifDevice.getHostName());
            }

            public void onError(OnvifDevice onvifDevice, int errorCode, String errorMsg) {

                log.info("错误了，{},{}", errorCode, errorMsg);
//                if (shouldLogFailure(onvifDevice)) {
//                    log.warn("PTZ response err [{}]: {} ", errorCode, errorMsg);
//                    lastFailures.put(onvifDevice.getHostName(), Instant.now());
//                }
            }
        });

        OnvifDevice device = new OnvifDevice("192.168.1.14", "admin", "Admin123456");

        //获得所有的service列表
        onvifManager.getServices(device, new OnvifServicesListener() {
            @Override
            public void onServicesReceived(OnvifDevice onvifDevice, OnvifServices services) {
                log.info("onServicesReceived");
                //获得media的配置，里面包含着token
                onvifManager.getMediaProfiles(device, new OnvifMediaProfilesListener() {
                    @Override
                    public void onMediaProfilesReceived(OnvifDevice device,
                                                        List<OnvifMediaProfile> mediaProfiles) {
                        log.info("onMediaProfilesReceived");
                        onvifManager.getMediaStreamURI(device, mediaProfiles.get(0), new OnvifMediaStreamURIListener() {
                            @Override
                            public void onMediaStreamURIReceived(OnvifDevice device,
                                                                 OnvifMediaProfile profile, String uri) {
                                log.info("onMediaStreamURIReceived,{}", profile);
                                log.info(uri);
                            }
                        });
                        onvifManager.getSnapshotURI(device, mediaProfiles.get(0), new OnvifSnapshotURIListener() {
                            @Override
                            public void onSnapshotURIReceived(OnvifDevice device,
                                                                 OnvifMediaProfile profile, String uri) {
                                log.info("onSnapshotURIReceived,{}", profile);
                                log.info(uri);
                            }
                        });
                    }
                });
            }
        });
        onvifManager.getDeviceInformation(device, new OnvifDeviceInformationListener() {
            @Override
            public void onDeviceInformationReceived(OnvifDevice device,
                                                    OnvifDeviceInformation deviceInformation) {
                log.info("onDeviceInformationReceived");
            }
        });


        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
