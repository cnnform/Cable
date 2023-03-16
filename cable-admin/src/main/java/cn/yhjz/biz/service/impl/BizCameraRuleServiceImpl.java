package cn.yhjz.biz.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import cn.yhjz.biz.domain.BizCameraRuleOption;
import cn.yhjz.biz.mapper.BizCameraRuleOptionMapper;
import cn.yhjz.biz.vo.AlertActionAttrVo;
import cn.yhjz.biz.vo.AlertActionVo;
import cn.yhjz.biz.vo.CameraRuleVo;
import cn.yhjz.common.core.domain.AjaxResult;
import cn.yhjz.common.utils.DateUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.yhjz.biz.mapper.BizCameraRuleMapper;
import cn.yhjz.biz.domain.BizCameraRule;
import cn.yhjz.biz.service.IBizCameraRuleService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 摄像头和规则关联Service业务层处理
 *
 * @author maguoping
 * @date 2022-08-01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BizCameraRuleServiceImpl implements IBizCameraRuleService {
    @Autowired
    private BizCameraRuleMapper bizCameraRuleMapper;

    @Autowired
    private BizCameraRuleOptionMapper bizCameraRuleOptionMapper;

    /**
     * 查询摄像头和规则关联
     *
     * @param id 摄像头和规则关联主键
     * @return 摄像头和规则关联
     */
    @Override
    public BizCameraRule selectBizCameraRuleById(Long id) {
        return bizCameraRuleMapper.selectBizCameraRuleById(id);
    }

    /**
     * 查询摄像头和规则关联列表
     *
     * @param bizCameraRule 摄像头和规则关联
     * @return 摄像头和规则关联
     */
    @Override
    public List<BizCameraRule> selectBizCameraRuleList(BizCameraRule bizCameraRule) {
        return bizCameraRuleMapper.selectBizCameraRuleList(bizCameraRule);
    }

    /**
     * 新增摄像头和规则关联
     *
     * @param bizCameraRule 摄像头和规则关联
     * @return 结果
     */
    @Override
    public int insertBizCameraRule(BizCameraRule bizCameraRule) {
        bizCameraRule.setCreateTime(DateUtils.getNowDate());
        return bizCameraRuleMapper.insertBizCameraRule(bizCameraRule);
    }

    /**
     * 修改摄像头和规则关联
     *
     * @param bizCameraRule 摄像头和规则关联
     * @return 结果
     */
    @Override
    public int updateBizCameraRule(BizCameraRule bizCameraRule) {
        bizCameraRule.setUpdateTime(DateUtils.getNowDate());
        return bizCameraRuleMapper.updateBizCameraRule(bizCameraRule);
    }

    /**
     * 批量删除摄像头和规则关联
     *
     * @param ids 需要删除的摄像头和规则关联主键
     * @return 结果
     */
    @Override
    public int deleteBizCameraRuleByIds(Long[] ids) {
        return bizCameraRuleMapper.deleteBizCameraRuleByIds(ids);
    }

    /**
     * 删除摄像头和规则关联信息
     *
     * @param id 摄像头和规则关联主键
     * @return 结果
     */
    @Override
    public int deleteBizCameraRuleById(Long id) {
        return bizCameraRuleMapper.deleteBizCameraRuleById(id);
    }

    /**
     * 保存摄像头的报警规则和报警动作的配置
     *
     * @param cameraId
     * @param cameraRuleVoList
     * @return
     */
    @Override
    public int saveCameraRules(Long cameraId, List<CameraRuleVo> cameraRuleVoList) {
        //先把之前的全部删除
        this.deleteAllByCameraId(cameraId);
        //添加现在的
        //首先保存规则和等级，得到新增数据的id作为refid
        for (CameraRuleVo cameraRuleVo : cameraRuleVoList) {
            cameraRuleVo.setCameraId(cameraId);
            BizCameraRule cameraRule = new BizCameraRule();
            cameraRule.setId(null);
            BeanUtils.copyProperties(cameraRuleVo, cameraRule);
            bizCameraRuleMapper.insertBizCameraRule(cameraRule);
            Long refId = cameraRule.getId();
            //保存配置项的值
            List<AlertActionVo> alertActionVoList = cameraRuleVo.getActionList();
            if (CollectionUtils.isNotEmpty(alertActionVoList)) {
                for (AlertActionVo alertActionVo : alertActionVoList) {
                    Long actionId = alertActionVo.getId();
                    String actionCode = alertActionVo.getActionCode();
                    List<AlertActionAttrVo> actionAttrVoList = alertActionVo.getAttrs();
                    if (CollectionUtils.isNotEmpty(actionAttrVoList)) {
                        for (AlertActionAttrVo alertActionAttrVo : actionAttrVoList) {
                            String attrCode = alertActionAttrVo.getAttrCode();
                            String attrValue = alertActionAttrVo.getAttrValue();
                            BizCameraRuleOption bizCameraRuleOption = new BizCameraRuleOption();
                            bizCameraRuleOption.setRefId(refId);
                            bizCameraRuleOption.setActionId(actionId);
                            bizCameraRuleOption.setActionCode(actionCode);
                            bizCameraRuleOption.setAttrCode(attrCode);
                            bizCameraRuleOption.setAttrValue(attrValue);
                            this.bizCameraRuleOptionMapper.insertBizCameraRuleOption(bizCameraRuleOption);
                        }

                    }
                }
            }
        }
        return 1;
    }

    /**
     * 根据摄像头id删除所有报警相关的配置
     *
     * @param cameraId
     * @return
     */
    public int deleteAllByCameraId(Long cameraId) {
        //先删除配置项
        BizCameraRule param = new BizCameraRule();
        param.setCameraId(cameraId);
        List<BizCameraRule> ruleList = this.bizCameraRuleMapper.selectBizCameraRuleList(param);
        if (CollectionUtils.isNotEmpty(ruleList)) {
            for (BizCameraRule rule : ruleList) {
                this.bizCameraRuleOptionMapper.deleteByRefId(rule.getId());
            }
        }
        this.bizCameraRuleMapper.deleteByCameraId(cameraId);
        return 0;
    }

    /**
     * 返回一个摄像头的规则和报警等级的配置
     *
     * @param cameraId
     * @return
     */
    @Override
    public List<BizCameraRule> getDetailByCameraId(Long cameraId) {
        BizCameraRule param = new BizCameraRule();
        param.setCameraId(cameraId);
        List<BizCameraRule> cameraRuleList = bizCameraRuleMapper.selectBizCameraRuleList(param);
        return cameraRuleList;
    }

    @Override
    public List<CameraRuleVo> getRuleOptionValueByCameraId(Long cameraId) {
        BizCameraRule param = new BizCameraRule();
        param.setCameraId(cameraId);
        List<BizCameraRule> cameraRuleList = bizCameraRuleMapper.selectBizCameraRuleList(param);
        if (CollectionUtils.isNotEmpty(cameraRuleList)) {
            List<CameraRuleVo> cameraRuleVoList = cameraRuleList.stream().map((s) -> {
                CameraRuleVo t = new CameraRuleVo();
                BeanUtils.copyProperties(s, t);
                return t;
            }).collect(Collectors.toList());
            for (CameraRuleVo cameraRuleVo : cameraRuleVoList) {
                Long refId = cameraRuleVo.getId();
                List<BizCameraRuleOption> bizCameraRuleOptionList
                        = this.bizCameraRuleOptionMapper.selectBizCameraRuleOptionByRefId(refId);
                cameraRuleVo.setOptionList(bizCameraRuleOptionList);
            }
            return cameraRuleVoList;
        } else {
            return null;
        }
    }
}
