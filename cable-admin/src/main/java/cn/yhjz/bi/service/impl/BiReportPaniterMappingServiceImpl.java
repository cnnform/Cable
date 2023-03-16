package cn.yhjz.bi.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.yhjz.bi.mapper.BiReportPaniterMappingMapper;
import cn.yhjz.bi.domain.BiReportPaniterMapping;
import cn.yhjz.bi.service.IBiReportPaniterMappingService;

/**
 * 看板与报关联Service业务层处理
 *
 * @author yhjz
 * @date 2022-01-13
 */
@Service
public class BiReportPaniterMappingServiceImpl implements IBiReportPaniterMappingService
{
    @Autowired
    private BiReportPaniterMappingMapper biReportPaniterMappingMapper;

    /**
     * 查询看板与报关联
     *
     * @param id 看板与报关联主键
     * @return 看板与报关联
     */
    @Override
    public BiReportPaniterMapping selectBiReportPaniterMappingById(Long id)
    {
        return biReportPaniterMappingMapper.selectBiReportPaniterMappingById(id);
    }

    /**
     * 查询看板与报关联列表
     *
     * @param biReportPaniterMapping 看板与报关联
     * @return 看板与报关联
     */
    @Override
    public List<BiReportPaniterMapping> selectBiReportPaniterMappingList(BiReportPaniterMapping biReportPaniterMapping)
    {
        return biReportPaniterMappingMapper.selectBiReportPaniterMappingList(biReportPaniterMapping);
    }

    /**
     * 新增看板与报关联
     *
     * @param biReportPaniterMapping 看板与报关联
     * @return 结果
     */
    @Override
    public int insertBiReportPaniterMapping(BiReportPaniterMapping biReportPaniterMapping)
    {
        return biReportPaniterMappingMapper.insertBiReportPaniterMapping(biReportPaniterMapping);
    }

    /**
     * 修改看板与报关联
     *
     * @param biReportPaniterMapping 看板与报关联
     * @return 结果
     */
    @Override
    public int updateBiReportPaniterMapping(BiReportPaniterMapping biReportPaniterMapping)
    {
        return biReportPaniterMappingMapper.updateBiReportPaniterMapping(biReportPaniterMapping);
    }

    /**
     * 批量删除看板与报关联
     *
     * @param ids 需要删除的看板与报关联主键
     * @return 结果
     */
    @Override
    public int deleteBiReportPaniterMappingByIds(Long[] ids)
    {
        return biReportPaniterMappingMapper.deleteBiReportPaniterMappingByIds(ids);
    }

    /**
     * 删除看板与报关联信息
     *
     * @param id 看板与报关联主键
     * @return 结果
     */
    @Override
    public int deleteBiReportPaniterMappingById(Long id)
    {
        return biReportPaniterMappingMapper.deleteBiReportPaniterMappingById(id);
    }
}
