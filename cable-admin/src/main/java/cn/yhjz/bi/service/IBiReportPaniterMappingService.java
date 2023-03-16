package cn.yhjz.bi.service;

import java.util.List;
import cn.yhjz.bi.domain.BiReportPaniterMapping;

/**
 * 看板与报关联Service接口
 *
 * @author yhjz
 * @date 2022-01-13
 */
public interface IBiReportPaniterMappingService
{
    /**
     * 查询看板与报关联
     *
     * @param id 看板与报关联主键
     * @return 看板与报关联
     */
    public BiReportPaniterMapping selectBiReportPaniterMappingById(Long id);

    /**
     * 查询看板与报关联列表
     *
     * @param biReportPaniterMapping 看板与报关联
     * @return 看板与报关联集合
     */
    public List<BiReportPaniterMapping> selectBiReportPaniterMappingList(BiReportPaniterMapping biReportPaniterMapping);

    /**
     * 新增看板与报关联
     *
     * @param biReportPaniterMapping 看板与报关联
     * @return 结果
     */
    public int insertBiReportPaniterMapping(BiReportPaniterMapping biReportPaniterMapping);

    /**
     * 修改看板与报关联
     *
     * @param biReportPaniterMapping 看板与报关联
     * @return 结果
     */
    public int updateBiReportPaniterMapping(BiReportPaniterMapping biReportPaniterMapping);

    /**
     * 批量删除看板与报关联
     *
     * @param ids 需要删除的看板与报关联主键集合
     * @return 结果
     */
    public int deleteBiReportPaniterMappingByIds(Long[] ids);

    /**
     * 删除看板与报关联信息
     *
     * @param id 看板与报关联主键
     * @return 结果
     */
    public int deleteBiReportPaniterMappingById(Long id);
}
