package cn.yhjz;

import cn.yhjz.camera.NettyClientHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CableCameraApplication {
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        String networkAreaCode=System.getProperty("network_area_code");
        NettyClientHandler.NETWORK_AREA_CODE = networkAreaCode;
        SpringApplication.run(CableCameraApplication.class, args);
    }
}
