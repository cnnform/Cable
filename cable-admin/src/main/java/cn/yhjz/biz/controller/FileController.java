package cn.yhjz.biz.controller;


import cn.yhjz.biz.utils.MongodbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/preview")
public class FileController {

    @Autowired
    private MongodbService mongodbService;

    @RequestMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] image(String fileId) {
        byte[] bytes = mongodbService.getFileById(fileId);
        return bytes;
    }
    @ResponseBody
    @RequestMapping("aaaa")
    public String aaaa(){
        return "hello";
    }
}
