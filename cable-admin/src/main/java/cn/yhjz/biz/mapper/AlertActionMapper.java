package cn.yhjz.biz.mapper;

import java.util.List;
import cn.yhjz.biz.domain.AlertAction;

/**
 * 报警动作Mapper接口
 * 
 * @author maguoping
 * @date 2022-07-28
 */
public interface AlertActionMapper 
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
     * 删除报警动作
     * 
     * @param id 报警动作主键
     * @return 结果
     */
    public int deleteAlertActionById(Long id);

    /**
     * 批量删除报警动作
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAlertActionByIds(Long[] ids);
}
