package cn.yhjz.biz.service;

import java.util.List;

import cn.yhjz.biz.domain.AlertActionAttr;

/**
 * 动作的属性Service接口
 *
 * @author maguoping
 * @date 2022-07-27
 */
public interface IAlertActionAttrService {
    /**
     * 查询动作的属性
     *
     * @param id 动作的属性主键
     * @return 动作的属性
     */
    public AlertActionAttr selectAlertActionAttrById(Long id);

    /**
     * 查询动作的属性列表
     *
     * @param alertActionAttr 动作的属性
     * @return 动作的属性集合
     */
    public List<AlertActionAttr> selectAlertActionAttrList(AlertActionAttr alertActionAttr);

    /**
     * 新增动作的属性
     *
     * @param alertActionAttr 动作的属性
     * @return 结果
     */
    public int insertAlertActionAttr(AlertActionAttr alertActionAttr);

    /**
     * 修改动作的属性
     *
     * @param alertActionAttr 动作的属性
     * @return 结果
     */
    public int updateAlertActionAttr(AlertActionAttr alertActionAttr);

    /**
     * 批量删除动作的属性
     *
     * @param ids 需要删除的动作的属性主键集合
     * @return 结果
     */
    public int deleteAlertActionAttrByIds(Long[] ids);

    /**
     * 删除动作的属性信息
     *
     * @param id 动作的属性主键
     * @return 结果
     */
    public int deleteAlertActionAttrById(Long id);

    public int saveActionAttrs(List<AlertActionAttr> attrs);
}
