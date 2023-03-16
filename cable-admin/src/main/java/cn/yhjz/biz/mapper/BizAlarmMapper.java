package cn.yhjz.biz.mapper;

import cn.yhjz.biz.domain.BizAlarm;
import cn.yhjz.biz.vo.BizAlarmVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 报警管理Mapper接口
 * 
 * @author yhjz
 * @date 2022-05-07
 */
public interface BizAlarmMapper {
    /**
     * 查询报警管理
     * 
     * @param alarmId 报警管理主键
     * @return 报警管理
     */
    public BizAlarm selectBizAlarmByAlarmId(Long alarmId);

    /**
     * 查询报警管理列表
     * 
     * @param bizAlarm 报警管理
     * @return 报警管理集合
     */
    public List<BizAlarm> selectBizAlarmList(BizAlarm bizAlarm);

    /**
     * 查询报警管理主键列表
     *
     * @return 报警管理集合
     */
    public List<Long> selectAlarmIdListByCameraId(Long cameraId);

    /**
     * 新增报警管理
     * 
     * @param bizAlarm 报警管理
     * @return 结果
     */
    public int insertBizAlarm(BizAlarm bizAlarm);

    /**
     * 修改报警管理
     * 
     * @param bizAlarm 报警管理
     * @return 结果
     */
    public int updateBizAlarm(BizAlarm bizAlarm);

    /**
     * 删除报警管理
     * 
     * @param alarmId 报警管理主键
     * @return 结果
     */
    public int deleteBizAlarmByAlarmId(Long alarmId);

    /**
     * 批量删除报警管理
     * 
     * @param alarmIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizAlarmByAlarmIds(Long[] alarmIds);

    List<Map> statistics(@Param("startTime") Date startTime, @Param("endTime")Date endTime);
    List<BizAlarmVo> listNear(@Param("count") Long count);
    List<BizAlarmVo> listAfter(@Param("time") Date time);
}
