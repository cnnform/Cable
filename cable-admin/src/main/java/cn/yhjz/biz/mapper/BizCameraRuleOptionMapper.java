package cn.yhjz.biz.mapper;

import java.util.List;
import cn.yhjz.biz.domain.BizCameraRuleOption;

/**
 * 摄像头规则配置项的值Mapper接口
 * 
 * @author maguoping
 * @date 2022-08-03
 */
public interface BizCameraRuleOptionMapper 
{
    /**
     * 查询摄像头规则配置项的值
     * 
     * @param id 摄像头规则配置项的值主键
     * @return 摄像头规则配置项的值
     */
    public BizCameraRuleOption selectBizCameraRuleOptionById(Long id);

    /**
     * 查询摄像头规则配置项的值列表
     * 
     * @param bizCameraRuleOption 摄像头规则配置项的值
     * @return 摄像头规则配置项的值集合
     */
    public List<BizCameraRuleOption> selectBizCameraRuleOptionList(BizCameraRuleOption bizCameraRuleOption);

    /**
     * 新增摄像头规则配置项的值
     * 
     * @param bizCameraRuleOption 摄像头规则配置项的值
     * @return 结果
     */
    public int insertBizCameraRuleOption(BizCameraRuleOption bizCameraRuleOption);

    /**
     * 修改摄像头规则配置项的值
     * 
     * @param bizCameraRuleOption 摄像头规则配置项的值
     * @return 结果
     */
    public int updateBizCameraRuleOption(BizCameraRuleOption bizCameraRuleOption);

    /**
     * 删除摄像头规则配置项的值
     * 
     * @param id 摄像头规则配置项的值主键
     * @return 结果
     */
    public int deleteBizCameraRuleOptionById(Long id);

    /**
     * 批量删除摄像头规则配置项的值
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizCameraRuleOptionByIds(Long[] ids);

    int deleteByRefId(Long refId);

    List<BizCameraRuleOption> selectBizCameraRuleOptionByRefId(Long refId);
}
