package cn.yhjz.biz.service;

import java.util.List;
import cn.yhjz.biz.domain.AlertLevelAndAction;

/**
 * 报警的动作配置Service接口
 * 
 * @author maguoping
 * @date 2022-07-28
 */
public interface IAlertLevelAndActionService 
{
    /**
     * 查询报警的动作配置
     * 
     * @param id 报警的动作配置主键
     * @return 报警的动作配置
     */
    public AlertLevelAndAction selectAlertLevelAndActionById(Long id);

    /**
     * 查询报警的动作配置列表
     * 
     * @param alertLevelAndAction 报警的动作配置
     * @return 报警的动作配置集合
     */
    public List<AlertLevelAndAction> selectAlertLevelAndActionList(AlertLevelAndAction alertLevelAndAction);

    /**
     * 新增报警的动作配置
     * 
     * @param alertLevelAndAction 报警的动作配置
     * @return 结果
     */
    public int insertAlertLevelAndAction(AlertLevelAndAction alertLevelAndAction);

    /**
     * 修改报警的动作配置
     * 
     * @param alertLevelAndAction 报警的动作配置
     * @return 结果
     */
    public int updateAlertLevelAndAction(AlertLevelAndAction alertLevelAndAction);

    /**
     * 批量删除报警的动作配置
     * 
     * @param ids 需要删除的报警的动作配置主键集合
     * @return 结果
     */
    public int deleteAlertLevelAndActionByIds(Long[] ids);

    /**
     * 删除报警的动作配置信息
     * 
     * @param id 报警的动作配置主键
     * @return 结果
     */
    public int deleteAlertLevelAndActionById(Long id);
    public int saveLevelAndActions(Long levelId,List<AlertLevelAndAction> actions);
}
