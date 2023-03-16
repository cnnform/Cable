package cn.yhjz.biz.service.impl;

import java.util.List;

import cn.yhjz.biz.domain.BizCameraRule;
import cn.yhjz.biz.domain.BizCameraRuleOption;
import cn.yhjz.biz.vo.AlertActionAttrVo;
import cn.yhjz.biz.vo.AlertActionVo;
import cn.yhjz.biz.vo.CameraRuleVo;
import cn.yhjz.common.utils.DateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.yhjz.biz.mapper.BizCableRuleMapper;
import cn.yhjz.biz.domain.BizCableRule;
import cn.yhjz.biz.service.IBizCableRuleService;

/**
 * 线路和规则关联Service业务层处理
 *
 * @author maguoping
 * @date 2022-09-07
 */
@Service
public class BizCableRuleServiceImpl implements IBizCableRuleService {
    @Autowired
    private BizCableRuleMapper bizCableRuleMapper;

    /**
     * 查询线路和规则关联
     *
     * @param id 线路和规则关联主键
     * @return 线路和规则关联
     */
    @Override
    public BizCableRule selectBizCableRuleById(Long id) {
        return bizCableRuleMapper.selectBizCableRuleById(id);
    }
    /**
     * 查询线路和规则关联
     *
     * @param id 线路和规则关联主键
     * @return 线路和规则关联
     */
    @Override
    public List<BizCableRule> selectBizCableRuleByCableId(Long cableId) {
        return bizCableRuleMapper.selectBizCableRuleByCableId(cableId);
    }

    /**
     * 查询线路和规则关联列表
     *
     * @param bizCableRule 线路和规则关联
     * @return 线路和规则关联
     */
    @Override
    public List<BizCableRule> selectBizCableRuleList(BizCableRule bizCableRule) {
        return bizCableRuleMapper.selectBizCableRuleList(bizCableRule);
    }

    /**
     * 新增线路和规则关联
     *
     * @param bizCableRule 线路和规则关联
     * @return 结果
     */
    @Override
    public int insertBizCableRule(BizCableRule bizCableRule) {
        bizCableRule.setCreateTime(DateUtils.getNowDate());
        return bizCableRuleMapper.insertBizCableRule(bizCableRule);
    }

    /**
     * 修改线路和规则关联
     *
     * @param bizCableRule 线路和规则关联
     * @return 结果
     */
    @Override
    public int updateBizCableRule(BizCableRule bizCableRule) {
        bizCableRule.setUpdateTime(DateUtils.getNowDate());
        return bizCableRuleMapper.updateBizCableRule(bizCableRule);
    }

    /**
     * 批量删除线路和规则关联
     *
     * @param ids 需要删除的线路和规则关联主键
     * @return 结果
     */
    @Override
    public int deleteBizCableRuleByIds(Long[] ids) {
        return bizCableRuleMapper.deleteBizCableRuleByIds(ids);
    }

    /**
     * 删除线路和规则关联信息
     *
     * @param id 线路和规则关联主键
     * @return 结果
     */
    @Override
    public int deleteBizCableRuleById(Long id) {
        return bizCableRuleMapper.deleteBizCableRuleById(id);
    }

    @Override
    public List<BizCableRule> getDetailByCameraId(Long cameraId) {
        BizCableRule param = new BizCableRule();
        param.setCableId(cameraId);
        List<BizCableRule> cameraRuleList = bizCableRuleMapper.selectBizCableRuleList(param);
        return cameraRuleList;
    }



    @Override
    public int saveCableRules(Long cableId, List<BizCableRule> cableRuleVoList) {
        //先把之前的全部删除
        this.deleteAllByCableId(cableId);
        //添加现在的
        //首先保存规则和等级，得到新增数据的id作为refid
        for (BizCableRule cableRuleVo : cableRuleVoList) {
            cableRuleVo.setCableId(cableId);
            BizCableRule cableRule = new BizCableRule();
            cableRule.setId(null);
            BeanUtils.copyProperties(cableRuleVo, cableRule);
            bizCableRuleMapper.insertBizCableRule(cableRule);
        }
        return 1;
    }

    public int deleteAllByCableId(Long cableId) {
        //先删除配置项
        BizCableRule param = new BizCableRule();
        param.setCableId(cableId);
        this.bizCableRuleMapper.deleteByCableId(cableId);
        return 0;
    }
}
