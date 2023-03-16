package cn.yhjz.biz.utils;

import cn.yhjz.common.utils.sign.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Component
@Slf4j
public class MongodbService {

    /**
     * 设置集合名称
     */
    private static final String COLLECTION_NAME = "cable_file";
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private FileRepository fileRepository;

    /**
     * 创建【集合】，对应关系数据库中的表
     * <p>
     * 创建一个大小没有限制的集合（默认集合创建方式）
     *
     * @return 创建集合的结果
     */
    public Object createCollection() {
        // 创建集合并返回集合信息
        mongoTemplate.createCollection(COLLECTION_NAME);
        // 检测新的集合是否存在，返回创建结果
        return mongoTemplate.collectionExists(COLLECTION_NAME) ? "创建表成功" : "创建表失败";
    }

    public static String file2md5(byte[] fileContent) {
        MessageDigest md = null;
        try {
            StringBuffer md5 = new StringBuffer();
            md = MessageDigest.getInstance("MD5");
            md.update(fileContent, 0, fileContent.length);
            byte[] mdbytes = md.digest();
            // convert the byte to hex format
            for (int i = 0; i < mdbytes.length; i++) {
                md5.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return md5.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String saveFile(byte[] fileContent) {
        String md5 = file2md5(fileContent);
        FileModel fileModel = new FileModel(md5, fileContent);
        FileModel newFileModel = fileRepository.save(fileModel);
        return newFileModel.getId();
    }


    /**
     * 根据【文档ID】查询集合中文档数据
     *
     * @return 文档信息
     */
    public byte[] getFileById(String id) {
        Optional<FileModel> optionalFileModel = fileRepository.findById(id);
        if (optionalFileModel.isPresent()) {
            return optionalFileModel.get().getContent().getData();
        }
        return null;
    }
    /**
     * 插入【多条】文档数据，如果文档信息已经【存在就抛出异常】
     *
     * @return 插入的多个文档信息
     *
     */
//    public Object insertMany(){
//        // 设置两个用户信息
//        User user1 = new User()
//                .setId("11")
//                .setAge(22)
//                .setSex("男")
//                .setRemake("无")
//                .setSalary(1500)
//                .setName("shiyi")
//                .setBirthday(new Date())
//                .setStatus(new Status().setHeight(180).setWeight(150));
//        User user2 = new User()
//                .setId("12")
//                .setAge(22)
//                .setSex("男")
//                .setRemake("无")
//                .setSalary(1500)
//                .setName("shier")
//                .setBirthday(new Date())
//                .setStatus(new Status().setHeight(180).setWeight(150));
//        // 使用户信息加入结合
//        List<User> userList = new ArrayList<>();
//        userList.add(user1);
//        userList.add(user2);
//        // 插入一条用户数据，如果某个文档信息已经存在就抛出异常
//        Collection<User> newUserList = mongoTemplate.insert(userList, COLLECTION_NAME);
//        // 输出存储结果
//        for (User user : newUserList) {
//            log.info("存储的用户信息为：{}", user);
//        }
//        return newUserList;
//    }
}
