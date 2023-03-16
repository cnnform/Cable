package cn.yhjz.biz.mapper;

import java.util.List;
import cn.yhjz.biz.domain.AlertLevel;

/**
 * 报警等级Mapper接口
 * 
 * @author maguoping
 * @date 2022-07-27
 */
public interface AlertLevelMapper 
{
    /**
     * 查询报警等级
     * 
     * @param id 报警等级主键
     * @return 报警等级
     */
    public AlertLevel selectAlertLevelById(Long id);

    /**
     * 查询报警等级列表
     * 
     * @param alertLevel 报警等级
     * @return 报警等级集合
     */
    public List<AlertLevel> selectAlertLevelList(AlertLevel alertLevel);

    /**
     * 新增报警等级
     * 
     * @param alertLevel 报警等级
     * @return 结果
     */
    public int insertAlertLevel(AlertLevel alertLevel);

    /**
     * 修改报警等级
     * 
     * @param alertLevel 报警等级
     * @return 结果
     */
    public int updateAlertLevel(AlertLevel alertLevel);

    /**
     * 删除报警等级
     * 
     * @param id 报警等级主键
     * @return 结果
     */
    public int deleteAlertLevelById(Long id);

    /**
     * 批量删除报警等级
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAlertLevelByIds(Long[] ids);
}
