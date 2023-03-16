package cn.yhjz.biz.service.impl;

import java.util.List;

import cn.yhjz.common.annotation.Excel;
import cn.yhjz.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.yhjz.biz.mapper.AlertActionAttrMapper;
import cn.yhjz.biz.domain.AlertActionAttr;
import cn.yhjz.biz.service.IAlertActionAttrService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 动作的属性Service业务层处理
 *
 * @author maguoping
 * @date 2022-07-27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AlertActionAttrServiceImpl implements IAlertActionAttrService {
    @Autowired
    private AlertActionAttrMapper alertActionAttrMapper;

    /**
     * 查询动作的属性
     *
     * @param id 动作的属性主键
     * @return 动作的属性
     */
    @Override
    public AlertActionAttr selectAlertActionAttrById(Long id) {
        return alertActionAttrMapper.selectAlertActionAttrById(id);
    }

    /**
     * 查询动作的属性列表
     *
     * @param alertActionAttr 动作的属性
     * @return 动作的属性
     */
    @Override
    public List<AlertActionAttr> selectAlertActionAttrList(AlertActionAttr alertActionAttr) {
        return alertActionAttrMapper.selectAlertActionAttrList(alertActionAttr);
    }

    /**
     * 新增动作的属性
     *
     * @param alertActionAttr 动作的属性
     * @return 结果
     */
    @Override
    public int insertAlertActionAttr(AlertActionAttr alertActionAttr) {
        alertActionAttr.setCreateTime(DateUtils.getNowDate());
        return alertActionAttrMapper.insertAlertActionAttr(alertActionAttr);
    }

    /**
     * 修改动作的属性
     *
     * @param alertActionAttr 动作的属性
     * @return 结果
     */
    @Override
    public int updateAlertActionAttr(AlertActionAttr alertActionAttr) {
        alertActionAttr.setUpdateTime(DateUtils.getNowDate());
        return alertActionAttrMapper.updateAlertActionAttr(alertActionAttr);
    }

    /**
     * 批量删除动作的属性
     *
     * @param ids 需要删除的动作的属性主键
     * @return 结果
     */
    @Override
    public int deleteAlertActionAttrByIds(Long[] ids) {
        return alertActionAttrMapper.deleteAlertActionAttrByIds(ids);
    }

    /**
     * 删除动作的属性信息
     *
     * @param id 动作的属性主键
     * @return 结果
     */
    @Override
    public int deleteAlertActionAttrById(Long id) {
        return alertActionAttrMapper.deleteAlertActionAttrById(id);
    }

    @Override
    public int saveActionAttrs(List<AlertActionAttr> attrs) {
        //先删除原来的，再保存新的
        Long actionId = attrs.get(0).getActionId();
        alertActionAttrMapper.deleteByActionId(actionId);
        int res = 0;
        for (AlertActionAttr attr : attrs) {
            attr.setId(null);
            this.alertActionAttrMapper.insertAlertActionAttr(attr);
            res++;
        }
        return res;
    }
}
