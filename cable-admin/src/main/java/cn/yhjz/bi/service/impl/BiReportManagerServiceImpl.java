package cn.yhjz.bi.service.impl;

import java.util.List;

import cn.yhjz.bi.domain.BiReportManager;
import cn.yhjz.bi.mapper.BiReportManagerMapper;
import cn.yhjz.bi.service.IBiReportManagerService;
import cn.yhjz.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * BI报Service业务层处理
 *
 * @author yhjz
 * @date 2022-01-12
 */
@Service
public class BiReportManagerServiceImpl implements IBiReportManagerService
{

    @Autowired
    private BiReportManagerMapper biReportManagerMapper;

    /**
     * 查询BI报
     *
     * @param rmId BI报主键
     * @return BI报
     */
    @Override
    public BiReportManager selectBiReportManagerByRmId(Long rmId)
    {
        return biReportManagerMapper.selectBiReportManagerByRmId(rmId);
    }

    /**
     * 查询BI报列表
     *
     * @param biReportManager BI报
     * @return BI报
     */
    @Override
    public List<BiReportManager> selectBiReportManagerList(BiReportManager biReportManager)
    {
        return biReportManagerMapper.selectBiReportManagerList(biReportManager);
    }

    /**
     * 新增BI报
     *
     * @param biReportManager BI报
     * @return 结果
     */
    @Override
    public int insertBiReportManager(BiReportManager biReportManager)
    {
        biReportManager.setCreateTime(DateUtils.getNowDate());
        return biReportManagerMapper.insertBiReportManager(biReportManager);
    }

    /**
     * 修改BI报
     *
     * @param biReportManager BI报
     * @return 结果
     */
    @Override
    public int updateBiReportManager(BiReportManager biReportManager)
    {
        biReportManager.setUpdateTime(DateUtils.getNowDate());
        return biReportManagerMapper.updateBiReportManager(biReportManager);
    }

    /**
     * 批量删除BI报
     *
     * @param rmIds 需要删除的BI报主键
     * @return 结果
     */
    @Override
    public int deleteBiReportManagerByRmIds(Long[] rmIds)
    {
        return biReportManagerMapper.deleteBiReportManagerByRmIds(rmIds);
    }

    /**
     * 删除BI报信息
     *
     * @param rmId BI报主键
     * @return 结果
     */
    @Override
    public int deleteBiReportManagerByRmId(Long rmId)
    {
        return biReportManagerMapper.deleteBiReportManagerByRmId(rmId);
    }
}
