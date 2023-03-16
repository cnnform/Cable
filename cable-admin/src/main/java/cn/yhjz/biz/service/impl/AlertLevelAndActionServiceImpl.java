package cn.yhjz.biz.service.impl;

import java.util.List;

import cn.yhjz.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.yhjz.biz.mapper.AlertLevelAndActionMapper;
import cn.yhjz.biz.domain.AlertLevelAndAction;
import cn.yhjz.biz.service.IAlertLevelAndActionService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 报警的动作配置Service业务层处理
 *
 * @author maguoping
 * @date 2022-07-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AlertLevelAndActionServiceImpl implements IAlertLevelAndActionService {
    @Autowired
    private AlertLevelAndActionMapper alertLevelAndActionMapper;

    /**
     * 查询报警的动作配置
     *
     * @param id 报警的动作配置主键
     * @return 报警的动作配置
     */
    @Override
    public AlertLevelAndAction selectAlertLevelAndActionById(Long id) {
        return alertLevelAndActionMapper.selectAlertLevelAndActionById(id);
    }

    /**
     * 查询报警的动作配置列表
     *
     * @param alertLevelAndAction 报警的动作配置
     * @return 报警的动作配置
     */
    @Override
    public List<AlertLevelAndAction> selectAlertLevelAndActionList(AlertLevelAndAction alertLevelAndAction) {
        return alertLevelAndActionMapper.selectAlertLevelAndActionList(alertLevelAndAction);
    }

    /**
     * 新增报警的动作配置
     *
     * @param alertLevelAndAction 报警的动作配置
     * @return 结果
     */
    @Override
    public int insertAlertLevelAndAction(AlertLevelAndAction alertLevelAndAction) {
        alertLevelAndAction.setCreateTime(DateUtils.getNowDate());
        return alertLevelAndActionMapper.insertAlertLevelAndAction(alertLevelAndAction);
    }

    /**
     * 修改报警的动作配置
     *
     * @param alertLevelAndAction 报警的动作配置
     * @return 结果
     */
    @Override
    public int updateAlertLevelAndAction(AlertLevelAndAction alertLevelAndAction) {
        alertLevelAndAction.setUpdateTime(DateUtils.getNowDate());
        return alertLevelAndActionMapper.updateAlertLevelAndAction(alertLevelAndAction);
    }

    /**
     * 批量删除报警的动作配置
     *
     * @param ids 需要删除的报警的动作配置主键
     * @return 结果
     */
    @Override
    public int deleteAlertLevelAndActionByIds(Long[] ids) {
        return alertLevelAndActionMapper.deleteAlertLevelAndActionByIds(ids);
    }

    /**
     * 删除报警的动作配置信息
     *
     * @param id 报警的动作配置主键
     * @return 结果
     */
    @Override
    public int deleteAlertLevelAndActionById(Long id) {
        return alertLevelAndActionMapper.deleteAlertLevelAndActionById(id);
    }

    //保存一个
    @Override
    public int saveLevelAndActions(Long levelId, List<AlertLevelAndAction> actions) {

        alertLevelAndActionMapper.deleteByLevelId(levelId);
        for (AlertLevelAndAction action : actions) {
            action.setId(null);
            alertLevelAndActionMapper.insertAlertLevelAndAction(action);
        }
        return 1;
    }
}
