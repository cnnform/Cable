package cn.yhjz.biz;

import cn.yhjz.biz.utils.MongodbService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestMongodb {

    @Autowired
    private MongodbService mongodbService;

    @Test
    public void test() {
        byte[] fileContent = {0, 0, 0, 0,};
        String fileId = mongodbService.saveFile(fileContent);
    }
}
