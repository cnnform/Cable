package cn.yhjz.biz.controller;

import cn.yhjz.biz.domain.BizAlarm;
import cn.yhjz.biz.service.impl.BizAlarmServiceImpl;
import cn.yhjz.biz.utils.MongodbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/_t_")
@Slf4j
public class TestTestController {

    @Autowired
    private MongodbService mongodbService;

    @Autowired
    private BizAlarmServiceImpl alarmService;

    @RequestMapping("/mongo")
    public void mongo() {
        byte[] fileContent = {0, 0, 0, 0,};
        String fileId = mongodbService.saveFile(fileContent);
        log.info(fileId);
    }

    @RequestMapping("/getFile")
    public void getFile() {
        byte[] fileContent;
        String fileId = "f1d3ff8443297732862df21dc4e57262";
        fileContent = mongodbService.getFileById(fileId);
        log.info("{}", fileContent);
    }

    @RequestMapping("/heihei")
    public String heihei() {
        List<BizAlarm> alarmList = alarmService.selectBizAlarmList(null);
        for (BizAlarm alarm : alarmList) {
            byte[] fileContent = Base64.getDecoder().decode(alarm.getImgBase64());
            String fileId = mongodbService.saveFile(fileContent);
            alarm.setImgBase64(fileId);
            alarmService.updateBizAlarm(alarm);
        }
        return "ok";
    }

    @RequestMapping("/testupload")
    public String testupload() throws IOException {
        //读取文件上传到mongodb
        String path = "d:/wjj.jpg";  //创建存储图片文件的路径
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes);
        log.info("{}", bytes.length);
        String fileId = mongodbService.saveFile(bytes);
        return fileId;
    }
}
