package cn.yhjz.biz.actionexec;


import cn.yhjz.biz.utils.EmailUtil;
import cn.yhjz.biz.utils.MessageUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class ActionExecuter {

    public void dealAction(ExecAction execAction) {
        log.info("开始处理动作：{}", execAction.getActionCode());
        String actionCode = execAction.getActionCode();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if ("msg".equals(actionCode)) {
            String phoneNumber = execAction.getAttrValue("phone_number");
            String contentTemplate = execAction.getAttrValue("content_template");
            String now = simpleDateFormat.format(new Date());
            String content = String.format("摄像头%s发生报警：%s，时间：%s",
                    execAction.getCamera().getDeviceId(), execAction.getRule().getRuleName(), now);

            JSONObject paramJson = new JSONObject();
            paramJson.put("camera", execAction.getCamera().getDeviceId());
            paramJson.put("ruleName", execAction.getRule().getRuleName());
            this.sendMsg(phoneNumber, paramJson);
            return;
        }
        if ("email".equals(actionCode)) {
            String emailAddress = execAction.getAttrValue("email_address");
            String contentTemplate = execAction.getAttrValue("content_template");
            String now = simpleDateFormat.format(new Date());
            String content = String.format("摄像头%s发生报警：%s，时间：%s",
                    execAction.getCamera().getDeviceId(), execAction.getRule().getRuleName(), now);
            this.sendEMail(emailAddress, "线缆监控报警", content);
            return;
        }
        if ("system-notice".equals(actionCode)) {
            String account = execAction.getAttrValue("account");
            String contentTemplate = execAction.getAttrValue("content_template");
            String now = simpleDateFormat.format(new Date());
            String content = String.format("摄像头%s发生报警：%s，时间：%s",
                    execAction.getCamera().getDeviceId(), execAction.getRule().getRuleName(), now);
            this.sendNotice(account, content);
            return;
        }
    }

    public void dealActionUrule(ExecAction execAction,Date happenTime) {
        log.info("开始处理动作：{}", execAction.getActionCode());
        String ruleName = execAction.getRule().getRuleName();
        String actionCode = execAction.getActionCode();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String happenTimeString = simpleDateFormat.format(happenTime);
        if ("msg".equals(actionCode)) {
            String phoneNumber = execAction.getAttrValue("phone_number");
            String contentTemplate = execAction.getAttrValue("content_template");
            String content = String.format("摄像头%s发生报警：%s，时间：%s",
                    execAction.getCamera().getDeviceId(), ruleName, happenTimeString);

            JSONObject paramJson = new JSONObject();
            paramJson.put("camera", execAction.getCamera().getDeviceId());
            paramJson.put("ruleName", ruleName);
            this.sendMsg(phoneNumber, paramJson);
            return;
        }
        if ("email".equals(actionCode)) {
            String emailAddress = execAction.getAttrValue("email_address");
            String contentTemplate = execAction.getAttrValue("content_template");
            String content = String.format("摄像头%s发生报警：%s，时间：%s",
                    execAction.getCamera().getDeviceId(), ruleName, happenTimeString);
            this.sendEMail(emailAddress, "线缆监控报警", content);
            return;
        }
        if ("system-notice".equals(actionCode)) {
            String account = execAction.getAttrValue("account");
            String contentTemplate = execAction.getAttrValue("content_template");
            String content = String.format("摄像头%s发生报警：%s，时间：%s",
                    execAction.getCamera().getDeviceId(), ruleName, happenTimeString);
            this.sendNotice(account, content);
            return;
        }
    }

    /**
     * 发送短信
     */
    public void sendMsg(String number, JSONObject paramJson) {
        log.info("发送短信给{},短信内容：{}", number, paramJson);
        String templateCode = "SMS_248890096";

        MessageUtil.sendMsg(number, templateCode, paramJson);
    }

    /**
     * 发送电子邮件
     */
    public void sendEMail(String address, String subject, String content) {
        log.info("发送email给{},邮件内容：{}", address, content);
        try {
            EmailUtil.sendEmail(address, subject, content);
        } catch (Exception e) {
            log.error("发送邮件失败：{}", e);
        }
    }

    /**
     * 发送电子邮件
     */
    public void sendNotice(String user, String content) {
        log.info("发送系统通知给{},短信内容：{}", user, content);
    }
}
