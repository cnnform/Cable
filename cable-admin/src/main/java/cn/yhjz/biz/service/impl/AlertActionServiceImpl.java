package cn.yhjz.biz.service.impl;

import java.util.List;
import cn.yhjz.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.yhjz.biz.mapper.AlertActionMapper;
import cn.yhjz.biz.domain.AlertAction;
import cn.yhjz.biz.service.IAlertActionService;

/**
 * 报警动作Service业务层处理
 * 
 * @author maguoping
 * @date 2022-07-28
 */
@Service
public class AlertActionServiceImpl implements IAlertActionService 
{
    @Autowired
    private AlertActionMapper alertActionMapper;

    /**
     * 查询报警动作
     * 
     * @param id 报警动作主键
     * @return 报警动作
     */
    @Override
    public AlertAction selectAlertActionById(Long id)
    {
        return alertActionMapper.selectAlertActionById(id);
    }

    /**
     * 查询报警动作列表
     * 
     * @param alertAction 报警动作
     * @return 报警动作
     */
    @Override
    public List<AlertAction> selectAlertActionList(AlertAction alertAction)
    {
        return alertActionMapper.selectAlertActionList(alertAction);
    }

    /**
     * 新增报警动作
     * 
     * @param alertAction 报警动作
     * @return 结果
     */
    @Override
    public int insertAlertAction(AlertAction alertAction)
    {
        alertAction.setCreateTime(DateUtils.getNowDate());
        return alertActionMapper.insertAlertAction(alertAction);
    }

    /**
     * 修改报警动作
     * 
     * @param alertAction 报警动作
     * @return 结果
     */
    @Override
    public int updateAlertAction(AlertAction alertAction)
    {
        alertAction.setUpdateTime(DateUtils.getNowDate());
        return alertActionMapper.updateAlertAction(alertAction);
    }

    /**
     * 批量删除报警动作
     * 
     * @param ids 需要删除的报警动作主键
     * @return 结果
     */
    @Override
    public int deleteAlertActionByIds(Long[] ids)
    {
        return alertActionMapper.deleteAlertActionByIds(ids);
    }

    /**
     * 删除报警动作信息
     * 
     * @param id 报警动作主键
     * @return 结果
     */
    @Override
    public int deleteAlertActionById(Long id)
    {
        return alertActionMapper.deleteAlertActionById(id);
    }
}
