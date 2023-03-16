package cn.yhjz.biz.service;

import java.util.List;
import cn.yhjz.biz.domain.BizCableRule;
import cn.yhjz.biz.domain.BizCameraRule;
import cn.yhjz.biz.vo.CameraRuleVo;

/**
 * 线路和规则关联Service接口
 * 
 * @author maguoping
 * @date 2022-09-07
 */
public interface IBizCableRuleService 
{
    /**
     * 查询线路和规则关联
     * 
     * @param id 线路和规则关联主键
     * @return 线路和规则关联
     */
    public BizCableRule selectBizCableRuleById(Long id);

    List<BizCableRule> selectBizCableRuleByCableId(Long cableId);

    /**
     * 查询线路和规则关联列表
     * 
     * @param bizCableRule 线路和规则关联
     * @return 线路和规则关联集合
     */
    public List<BizCableRule> selectBizCableRuleList(BizCableRule bizCableRule);

    /**
     * 新增线路和规则关联
     * 
     * @param bizCableRule 线路和规则关联
     * @return 结果
     */
    public int insertBizCableRule(BizCableRule bizCableRule);

    /**
     * 修改线路和规则关联
     * 
     * @param bizCableRule 线路和规则关联
     * @return 结果
     */
    public int updateBizCableRule(BizCableRule bizCableRule);

    /**
     * 批量删除线路和规则关联
     * 
     * @param ids 需要删除的线路和规则关联主键集合
     * @return 结果
     */
    public int deleteBizCableRuleByIds(Long[] ids);

    /**
     * 删除线路和规则关联信息
     * 
     * @param id 线路和规则关联主键
     * @return 结果
     */
    public int deleteBizCableRuleById(Long id);

    List<BizCableRule> getDetailByCameraId(Long cableId);

    int saveCableRules(Long cableId, List<BizCableRule> cableRuleVoList);
}
