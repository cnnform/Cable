package cn.yhjz.bi.service;

import java.util.List;
import cn.yhjz.bi.domain.BiReportManager;

/**
 * 【请填写功能名称】Service接口
 *
 * @author yhjz
 * @date 2022-01-10
 */
public interface IBiReportManagerService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param rmId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public BiReportManager selectBiReportManagerByRmId(Long rmId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param biReportManager 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<BiReportManager> selectBiReportManagerList(BiReportManager biReportManager);

    /**
     * 新增【请填写功能名称】
     *
     * @param biReportManager 【请填写功能名称】
     * @return 结果
     */
    public int insertBiReportManager(BiReportManager biReportManager);

    /**
     * 修改【请填写功能名称】
     *
     * @param biReportManager 【请填写功能名称】
     * @return 结果
     */
    public int updateBiReportManager(BiReportManager biReportManager);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param rmIds 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteBiReportManagerByRmIds(Long[] rmIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param rmId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteBiReportManagerByRmId(Long rmId);
}
