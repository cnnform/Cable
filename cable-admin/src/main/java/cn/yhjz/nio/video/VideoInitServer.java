package cn.yhjz.nio.video;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VideoInitServer implements CommandLineRunner {
    @Autowired
    private VideoNettyServer nettyServer;
    @Override
    public void run(String... args) throws Exception {
        log.info("启动视频直播netty服务器");
        new Thread(()->{
            nettyServer.start();
        }).start();

    }
}
