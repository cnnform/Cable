package cn.yhjz.bi.service.impl;

import java.util.List;

import cn.yhjz.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.yhjz.bi.mapper.BiPainterManagerMapper;
import cn.yhjz.bi.domain.BiPainterManager;
import cn.yhjz.bi.service.IBiPainterManagerService;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author yhjz
 * @date 2022-01-10
 */
@Service
public class BiPainterManagerServiceImpl implements IBiPainterManagerService 
{
    @Autowired
    private BiPainterManagerMapper biPainterManagerMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param pmId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public BiPainterManager selectBiPainterManagerByPmId(Long pmId)
    {
        return biPainterManagerMapper.selectBiPainterManagerByPmId(pmId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param biPainterManager 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<BiPainterManager> selectBiPainterManagerList(BiPainterManager biPainterManager)
    {
        return biPainterManagerMapper.selectBiPainterManagerList(biPainterManager);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param biPainterManager 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertBiPainterManager(BiPainterManager biPainterManager)
    {
        biPainterManager.setCreateTime(DateUtils.getNowDate());
        return biPainterManagerMapper.insertBiPainterManager(biPainterManager);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param biPainterManager 【请填写功能名称】
     * @return 结果
     */
    @Override
    public BiPainterManager insertBiPm(BiPainterManager biPainterManager) {
        biPainterManager.setCreateTime(DateUtils.getNowDate());
        biPainterManagerMapper.insertBiPainterManager(biPainterManager);
        return biPainterManager;
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param biPainterManager 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateBiPainterManager(BiPainterManager biPainterManager)
    {
        biPainterManager.setUpdateTime(DateUtils.getNowDate());
        return biPainterManagerMapper.updateBiPainterManager(biPainterManager);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param pmIds 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteBiPainterManagerByPmIds(Long[] pmIds)
    {
        return biPainterManagerMapper.deleteBiPainterManagerByPmIds(pmIds);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param pmId 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteBiPainterManagerByPmId(Long pmId)
    {
        return biPainterManagerMapper.deleteBiPainterManagerByPmId(pmId);
    }
}
