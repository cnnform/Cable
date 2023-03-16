package cn.yhjz.biz.service.impl;

import java.util.List;
import cn.yhjz.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.yhjz.biz.mapper.RuleMainMapper;
import cn.yhjz.biz.domain.RuleMain;
import cn.yhjz.biz.service.IRuleMainService;

/**
 * 报警规则Service业务层处理
 * 
 * @author maguoping
 * @date 2022-08-01
 */
@Service
public class RuleMainServiceImpl implements IRuleMainService 
{
    @Autowired
    private RuleMainMapper ruleMainMapper;

    /**
     * 查询报警规则
     * 
     * @param id 报警规则主键
     * @return 报警规则
     */
    @Override
    public RuleMain selectRuleMainById(Long id)
    {
        return ruleMainMapper.selectRuleMainById(id);
    }

    /**
     * 查询报警规则列表
     * 
     * @param ruleMain 报警规则
     * @return 报警规则
     */
    @Override
    public List<RuleMain> selectRuleMainList(RuleMain ruleMain)
    {
        return ruleMainMapper.selectRuleMainList(ruleMain);
    }

    /**
     * 新增报警规则
     * 
     * @param ruleMain 报警规则
     * @return 结果
     */
    @Override
    public int insertRuleMain(RuleMain ruleMain)
    {
        ruleMain.setCreateTime(DateUtils.getNowDate());
        return ruleMainMapper.insertRuleMain(ruleMain);
    }

    /**
     * 修改报警规则
     * 
     * @param ruleMain 报警规则
     * @return 结果
     */
    @Override
    public int updateRuleMain(RuleMain ruleMain)
    {
        return ruleMainMapper.updateRuleMain(ruleMain);
    }

    /**
     * 批量删除报警规则
     * 
     * @param ids 需要删除的报警规则主键
     * @return 结果
     */
    @Override
    public int deleteRuleMainByIds(Long[] ids)
    {
        return ruleMainMapper.deleteRuleMainByIds(ids);
    }

    /**
     * 删除报警规则信息
     * 
     * @param id 报警规则主键
     * @return 结果
     */
    @Override
    public int deleteRuleMainById(Long id)
    {
        return ruleMainMapper.deleteRuleMainById(id);
    }
}
