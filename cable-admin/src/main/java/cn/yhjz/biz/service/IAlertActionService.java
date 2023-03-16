package cn.yhjz.biz.service;

import java.util.List;
import cn.yhjz.biz.domain.AlertAction;

/**
 * 报警动作Service接口
 * 
 * @author maguoping
 * @date 2022-07-28
 */
public interface IAlertActionService 
{
    /**
     * 查询报警动作
     * 
     * @param id 报警动作主键
     * @return 报警动作
     */
    public AlertAction selectAlertActionById(Long id);

    /**
     * 查询报警动作列表
     * 
     * @param alertAction 报警动作
     * @return 报警动作集合
     */
    public List<AlertAction> selectAlertActionList(AlertAction alertAction);

    /**
     * 新增报警动作
     * 
     * @param alertAction 报警动作
     * @return 结果
     */
    public int insertAlertAction(AlertAction alertAction);

    /**
     * 修改报警动作
     * 
     * @param alertAction 报警动作
     * @return 结果
     */
    public int updateAlertAction(AlertAction alertAction);

    /**
     * 批量删除报警动作
     * 
     * @param ids 需要删除的报警动作主键集合
     * @return 结果
     */
    public int deleteAlertActionByIds(Long[] ids);

    /**
     * 删除报警动作信息
     * 
     * @param id 报警动作主键
     * @return 结果
     */
    public int deleteAlertActionById(Long id);
}
