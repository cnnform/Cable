package cn.yhjz.biz.service.impl;

import java.util.*;

import cn.yhjz.biz.domain.BizAlarm;
import cn.yhjz.biz.mapper.BizAlarmMapper;
import cn.yhjz.biz.service.IBizAlarmService;
import cn.yhjz.biz.vo.BizAlarmVo;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 报警管理Service业务层处理
 *
 * @author yhjz
 * @date 2022-05-07
 */
@Service
public class BizAlarmServiceImpl implements IBizAlarmService {

    @Autowired
    private BizAlarmMapper bizAlarmMapper;

    /**
     * 查询报警管理
     *
     * @param alarmId 报警管理主键
     * @return 报警管理
     */
    @Override
    public BizAlarm selectBizAlarmByAlarmId(Long alarmId) {
        return bizAlarmMapper.selectBizAlarmByAlarmId(alarmId);
    }

    /**
     * 查询报警管理列表
     *
     * @param bizAlarm 报警管理
     * @return 报警管理
     */
    @Override
    public List<BizAlarm> selectBizAlarmList(BizAlarm bizAlarm) {
        return bizAlarmMapper.selectBizAlarmList(bizAlarm);
    }


    /**
     * 查询报警管理主键列表
     *
     * @return 报警管理集合
     */
    @Override
    public List<Long> selectAlarmIdListByCameraId(Long cameraId) {
        return bizAlarmMapper.selectAlarmIdListByCameraId(cameraId);
    }

    /**
     * 新增报警管理
     *
     * @param bizAlarm 报警管理
     * @return 结果
     */
    @Override
    public int insertBizAlarm(BizAlarm bizAlarm) {
        return bizAlarmMapper.insertBizAlarm(bizAlarm);
    }

    /**
     * 修改报警管理
     *
     * @param bizAlarm 报警管理
     * @return 结果
     */
    @Override
    public int updateBizAlarm(BizAlarm bizAlarm) {
        return bizAlarmMapper.updateBizAlarm(bizAlarm);
    }

    /**
     * 批量删除报警管理
     *
     * @param alarmIds 需要删除的报警管理主键
     * @return 结果
     */
    @Override
    public int deleteBizAlarmByAlarmIds(Long[] alarmIds) {
        return bizAlarmMapper.deleteBizAlarmByAlarmIds(alarmIds);
    }

    /**
     * 删除报警管理信息
     *
     * @param alarmId 报警管理主键
     * @return 结果
     */
    @Override
    public int deleteBizAlarmByAlarmId(Long alarmId) {
        return bizAlarmMapper.deleteBizAlarmByAlarmId(alarmId);
    }

    @Override
    public List<Map> statistics(DateTime now, int count) {
        List<Map> resultMap = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MM-dd");
        for (int i = count; i >=0; i--) {
            DateTime startTime = now.minusDays(i);
            DateTime endTime = now.minusDays(i - 1);
            List<Map> eachList=this.bizAlarmMapper.statistics(startTime.toDate(),endTime.toDate());
            if (CollectionUtils.isEmpty(eachList)) {
                Map<String, Object> eachMap = new HashMap<>();
                eachMap.put("all_count", 0);
                eachMap.put("time_stamp", fmt.print(startTime));
                resultMap.add(eachMap);
            }else{
                resultMap.addAll(this.bizAlarmMapper.statistics(startTime.toDate(),endTime.toDate()));
            }

        }
        return resultMap;
    }

    /**
     * 做的最近50个报警
     * @return
     */
    @Override
    public List<BizAlarmVo> listNear(Long count,Date time){
        return this.bizAlarmMapper.listNear(count);
//        return this.bizAlarmMapper.listAfter(time);
    }
}
