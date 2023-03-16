package cn.yhjz.biz.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.tea.*;
import com.aliyun.dysmsapi20170525.*;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.*;
import com.aliyun.teaopenapi.models.*;
import com.aliyun.teautil.*;
import com.aliyun.teautil.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 阿里云的短信服务
 */
@Component
@Slf4j
public class MessageUtil {

    public static com.aliyun.dysmsapi20170525.Client client;

    @PostConstruct
    public void init() throws Exception {
        client = MessageUtil.createClient("LTAI3wpn6tZDGbLz", "mNKhpdYRuH4jrqPvIR36PgWXMAvtXX");
    }

    /**
     * 使用AK&SK初始化账号Client
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public static void sendMsg(String number, String content,JSONObject paramJson) {
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(number)
                .setTemplateCode("SMS_248890096")
                .setSignName("银河团汇")
                .setTemplateParam(paramJson.toJSONString());

        RuntimeOptions runtime = new RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            client.sendSmsWithOptions(sendSmsRequest, runtime);
        } catch (TeaException error) {
            // 如有需要，请打印 error
            log.error("{}", error);
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            log.error("{}", error);
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }
}
