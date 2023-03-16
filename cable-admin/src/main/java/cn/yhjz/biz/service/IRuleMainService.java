package cn.yhjz.biz.service;

import java.util.List;
import cn.yhjz.biz.domain.RuleMain;

/**
 * 报警规则Service接口
 * 
 * @author maguoping
 * @date 2022-08-01
 */
public interface IRuleMainService 
{
    /**
     * 查询报警规则
     * 
     * @param id 报警规则主键
     * @return 报警规则
     */
    public RuleMain selectRuleMainById(Long id);

    /**
     * 查询报警规则列表
     * 
     * @param ruleMain 报警规则
     * @return 报警规则集合
     */
    public List<RuleMain> selectRuleMainList(RuleMain ruleMain);

    /**
     * 新增报警规则
     * 
     * @param ruleMain 报警规则
     * @return 结果
     */
    public int insertRuleMain(RuleMain ruleMain);

    /**
     * 修改报警规则
     * 
     * @param ruleMain 报警规则
     * @return 结果
     */
    public int updateRuleMain(RuleMain ruleMain);

    /**
     * 批量删除报警规则
     * 
     * @param ids 需要删除的报警规则主键集合
     * @return 结果
     */
    public int deleteRuleMainByIds(Long[] ids);

    /**
     * 删除报警规则信息
     * 
     * @param id 报警规则主键
     * @return 结果
     */
    public int deleteRuleMainById(Long id);
}
