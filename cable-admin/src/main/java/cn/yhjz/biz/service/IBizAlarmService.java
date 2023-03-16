package cn.yhjz.biz.service;

import cn.yhjz.biz.domain.BizAlarm;
import cn.yhjz.biz.vo.BizAlarmVo;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 报警管理Service接口
 * 
 * @author yhjz
 * @date 2022-05-07
 */
public interface IBizAlarmService {
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
     * 批量删除报警管理
     * 
     * @param alarmIds 需要删除的报警管理主键集合
     * @return 结果
     */
    public int deleteBizAlarmByAlarmIds(Long[] alarmIds);

    /**
     * 删除报警管理信息
     * 
     * @param alarmId 报警管理主键
     * @return 结果
     */
    public int deleteBizAlarmByAlarmId(Long alarmId);

    List<Map> statistics(DateTime now,int count);

    List<BizAlarmVo> listNear(Long count, Date time);
}
