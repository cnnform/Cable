package cn.yhjz.biz.domain;

import cn.yhjz.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("biz_strategy_time")
public class BizStrategyTime  {

    //主键
    @TableId(type = IdType.AUTO)
    private Long id;
    //摄像头id
    private Long cameraId;
    //策略类型，保留字段
    private String type;
    //时间段开始
    private String startTime;
    //时间段结束
    private String endTime;

    private Long createBy;
    private Long updateBy;
    private Date createTime;
    private Date updateTime;

}
