package cn.yhjz.bi.service;

import java.util.List;
import cn.yhjz.bi.domain.BiPainterManager;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author yhjz
 * @date 2022-01-10
 */
public interface IBiPainterManagerService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param pmId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public BiPainterManager selectBiPainterManagerByPmId(Long pmId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param biPainterManager 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<BiPainterManager> selectBiPainterManagerList(BiPainterManager biPainterManager);

    /**
     * 新增【请填写功能名称】
     * 
     * @param biPainterManager 【请填写功能名称】
     * @return 结果
     */
    public int insertBiPainterManager(BiPainterManager biPainterManager);


    /**
     * 新增【请填写功能名称】
     *
     * @param biPainterManager 【请填写功能名称】
     * @return 结果
     */
    public BiPainterManager insertBiPm(BiPainterManager biPainterManager);
    /**
     * 修改【请填写功能名称】
     * 
     * @param biPainterManager 【请填写功能名称】
     * @return 结果
     */
    public int updateBiPainterManager(BiPainterManager biPainterManager);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param pmIds 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteBiPainterManagerByPmIds(Long[] pmIds);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param pmId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteBiPainterManagerByPmId(Long pmId);
}
