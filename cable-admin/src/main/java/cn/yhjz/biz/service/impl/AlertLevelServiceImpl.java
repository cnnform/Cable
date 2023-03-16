package cn.yhjz.biz.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.yhjz.biz.domain.AlertAction;
import cn.yhjz.biz.domain.AlertActionAttr;
import cn.yhjz.biz.domain.AlertLevelAndAction;
import cn.yhjz.biz.mapper.AlertActionAttrMapper;
import cn.yhjz.biz.mapper.AlertActionMapper;
import cn.yhjz.biz.mapper.AlertLevelAndActionMapper;
import cn.yhjz.biz.vo.AlertActionAttrVo;
import cn.yhjz.biz.vo.AlertActionVo;
import cn.yhjz.biz.vo.AlertLevelVo;
import cn.yhjz.common.utils.DateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.yhjz.biz.mapper.AlertLevelMapper;
import cn.yhjz.biz.domain.AlertLevel;
import cn.yhjz.biz.service.IAlertLevelService;

/**
 * 报警等级Service业务层处理
 *
 * @author maguoping
 * @date 2022-07-27
 */
@Service
public class AlertLevelServiceImpl implements IAlertLevelService {
    @Autowired
    private AlertLevelMapper alertLevelMapper;

    @Autowired
    private AlertLevelAndActionMapper alertLevelAndActionMapper;

    @Autowired
    private AlertActionMapper alertActionMapper;

    @Autowired
    private AlertActionAttrMapper alertActionAttrMapper;

    /**
     * 查询报警等级
     *
     * @param id 报警等级主键
     * @return 报警等级
     */
    @Override
    public AlertLevel selectAlertLevelById(Long id) {
        return alertLevelMapper.selectAlertLevelById(id);
    }

    /**
     * 查询报警等级列表
     *
     * @param alertLevel 报警等级
     * @return 报警等级
     */
    @Override
    public List<AlertLevel> selectAlertLevelList(AlertLevel alertLevel) {
        return alertLevelMapper.selectAlertLevelList(alertLevel);
    }

    /**
     * 新增报警等级
     *
     * @param alertLevel 报警等级
     * @return 结果
     */
    @Override
    public int insertAlertLevel(AlertLevel alertLevel) {
        alertLevel.setCreateTime(DateUtils.getNowDate());
        return alertLevelMapper.insertAlertLevel(alertLevel);
    }

    /**
     * 修改报警等级
     *
     * @param alertLevel 报警等级
     * @return 结果
     */
    @Override
    public int updateAlertLevel(AlertLevel alertLevel) {
        alertLevel.setUpdateTime(DateUtils.getNowDate());
        return alertLevelMapper.updateAlertLevel(alertLevel);
    }

    /**
     * 批量删除报警等级
     *
     * @param ids 需要删除的报警等级主键
     * @return 结果
     */
    @Override
    public int deleteAlertLevelByIds(Long[] ids) {
        return alertLevelMapper.deleteAlertLevelByIds(ids);
    }

    /**
     * 删除报警等级信息
     *
     * @param id 报警等级主键
     * @return 结果
     */
    @Override
    public int deleteAlertLevelById(Long id) {
        return alertLevelMapper.deleteAlertLevelById(id);
    }

    /**
     * 根据报警等级id获得报警动作和它的配置项
     *
     * @param levelId
     * @return
     */
    @Override
    public AlertLevelVo getAlertLevelDetailByLevelId(Long levelId) {
        AlertLevel alertLevel = alertLevelMapper.selectAlertLevelById(levelId);
        if (alertLevel == null) {
            throw new RuntimeException("没找到报警等级");
        }
        //根据level获得动作列表
        AlertLevelAndAction alertLevelAndActionParam = new AlertLevelAndAction();
        alertLevelAndActionParam.setLevelId(alertLevel.getId());
        List<AlertLevelAndAction> alertLevelAndActionList =
                alertLevelAndActionMapper.selectAlertLevelAndActionList(alertLevelAndActionParam);
        AlertLevelVo alertLevelVo = new AlertLevelVo();
        BeanUtils.copyProperties(alertLevel, alertLevelVo);
        //获得action列表
        if (CollectionUtils.isNotEmpty(alertLevelAndActionList)) {
            List<AlertActionVo> actionVoList = new ArrayList<>();
            for (AlertLevelAndAction alaa : alertLevelAndActionList) {
                //根据id查询配置项
                AlertAction alertAction = alertActionMapper.selectAlertActionById(alaa.getActionId());
                AlertActionVo alertActionVo = new AlertActionVo();
                BeanUtils.copyProperties(alertAction, alertActionVo);
                //根据action查询配置项
                List<AlertActionAttr> alertActionAttrList =
                        alertActionAttrMapper.selectAlertActionAttrListByActionId(alertAction.getId());
                List<AlertActionAttrVo> alertActionAttrVoList = alertActionAttrList.stream().map((s) -> {
                    AlertActionAttrVo t = new AlertActionAttrVo();
                    BeanUtils.copyProperties(s, t);
                    return t;
                }).collect(Collectors.toList());
                alertActionVo.setAttrs(alertActionAttrVoList);
                actionVoList.add(alertActionVo);
            }
            alertLevelVo.setActionList(actionVoList);
        }
        return alertLevelVo;
    }
}
