package cn.yhjz.biz.mapper;

import java.util.List;
import cn.yhjz.biz.domain.AlertActionAttr;
import org.apache.ibatis.annotations.Param;

/**
 * 动作的属性Mapper接口
 * 
 * @author maguoping
 * @date 2022-07-27
 */
public interface AlertActionAttrMapper 
{
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

    List<AlertActionAttr> selectAlertActionAttrListByActionId(Long actionId);

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
     * 删除动作的属性
     * 
     * @param id 动作的属性主键
     * @return 结果
     */
    public int deleteAlertActionAttrById(Long id);

    public int deleteByActionId(@Param("actionId") Long actionId);

    /**
     * 批量删除动作的属性
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAlertActionAttrByIds(Long[] ids);
}
