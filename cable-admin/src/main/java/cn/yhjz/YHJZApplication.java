package cn.yhjz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

/**
 * 启动程序
 * 
 * @author yhjz
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
//@MapperScan("cn.yhjz.biz.mapper")
@ImportResource({"classpath:context.xml"})
public class YHJZApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(YHJZApplication.class, args);
    }
}
