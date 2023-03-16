package cn.yhjz.biz.jinsanli;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jsl.platform")
@Data
public class JslConfig {
    private String ip;
    private String port;
    private String protocol;//https还是http
}
