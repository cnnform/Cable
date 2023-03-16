package cn.yhjz.biz.utils;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface  FileRepository extends MongoRepository<FileModel, String> {
}
