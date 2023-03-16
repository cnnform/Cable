package cn.yhjz.biz.service.impl;

import java.util.List;
import cn.yhjz.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.yhjz.biz.mapper.BizCameraRuleOptionMapper;
import cn.yhjz.biz.domain.BizCameraRuleOption;
import cn.yhjz.biz.service.IBizCameraRuleOptionService;

/**
 * 摄像头规则配置项的值Service业务层处理
 * 
 * @author maguoping
 * @date 2022-08-03
 */
@Service
public class BizCameraRuleOptionServiceImpl implements IBizCameraRuleOptionService 
{
    @Autowired
    private BizCameraRuleOptionMapper bizCameraRuleOptionMapper;

    /**
     * 查询摄像头规则配置项的值
     * 
     * @param id 摄像头规则配置项的值主键
     * @return 摄像头规则配置项的值
     */
    @Override
    public BizCameraRuleOption selectBizCameraRuleOptionById(Long id)
    {
        return bizCameraRuleOptionMapper.selectBizCameraRuleOptionById(id);
    }

    /**
     * 查询摄像头规则配置项的值列表
     * 
     * @param bizCameraRuleOption 摄像头规则配置项的值
     * @return 摄像头规则配置项的值
     */
    @Override
    public List<BizCameraRuleOption> selectBizCameraRuleOptionList(BizCameraRuleOption bizCameraRuleOption)
    {
        return bizCameraRuleOptionMapper.selectBizCameraRuleOptionList(bizCameraRuleOption);
    }

    /**
     * 新增摄像头规则配置项的值
     * 
     * @param bizCameraRuleOption 摄像头规则配置项的值
     * @return 结果
     */
    @Override
    public int insertBizCameraRuleOption(BizCameraRuleOption bizCameraRuleOption)
    {
        bizCameraRuleOption.setCreateTime(DateUtils.getNowDate());
        return bizCameraRuleOptionMapper.insertBizCameraRuleOption(bizCameraRuleOption);
    }

    /**
     * 修改摄像头规则配置项的值
     * 
     * @param bizCameraRuleOption 摄像头规则配置项的值
     * @return 结果
     */
    @Override
    public int updateBizCameraRuleOption(BizCameraRuleOption bizCameraRuleOption)
    {
        bizCameraRuleOption.setUpdateTime(DateUtils.getNowDate());
        return bizCameraRuleOptionMapper.updateBizCameraRuleOption(bizCameraRuleOption);
    }

    /**
     * 批量删除摄像头规则配置项的值
     * 
     * @param ids 需要删除的摄像头规则配置项的值主键
     * @return 结果
     */
    @Override
    public int deleteBizCameraRuleOptionByIds(Long[] ids)
    {
        return bizCameraRuleOptionMapper.deleteBizCameraRuleOptionByIds(ids);
    }

    /**
     * 删除摄像头规则配置项的值信息
     * 
     * @param id 摄像头规则配置项的值主键
     * @return 结果
     */
    @Override
    public int deleteBizCameraRuleOptionById(Long id)
    {
        return bizCameraRuleOptionMapper.deleteBizCameraRuleOptionById(id);
    }
}
