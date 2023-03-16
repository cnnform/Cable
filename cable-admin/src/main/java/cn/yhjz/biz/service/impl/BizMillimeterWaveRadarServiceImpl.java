package cn.yhjz.biz.service.impl;

import java.util.List;

import cn.yhjz.biz.domain.BizMillimeterWaveRadar;
import cn.yhjz.biz.mapper.BizMillimeterWaveRadarMapper;
import cn.yhjz.biz.service.IBizMillimeterWaveRadarService;
import cn.yhjz.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 毫米波雷达管理Service业务层处理
 * 
 * @author yhjz
 * @date 2022-11-06
 */
@Service
public class BizMillimeterWaveRadarServiceImpl implements IBizMillimeterWaveRadarService
{
    @Autowired
    private BizMillimeterWaveRadarMapper bizMillimeterWaveRadarMapper;

    /**
     * 查询毫米波雷达管理
     * 
     * @param id 毫米波雷达管理主键
     * @return 毫米波雷达管理
     */
    @Override
    public BizMillimeterWaveRadar selectBizMillimeterWaveRadarById(Long id)
    {
        return bizMillimeterWaveRadarMapper.selectBizMillimeterWaveRadarById(id);
    }

    /**
     * 查询毫米波雷达管理列表
     * 
     * @param bizMillimeterWaveRadar 毫米波雷达管理
     * @return 毫米波雷达管理
     */
    @Override
    public List<BizMillimeterWaveRadar> selectBizMillimeterWaveRadarList(BizMillimeterWaveRadar bizMillimeterWaveRadar)
    {
        return bizMillimeterWaveRadarMapper.selectBizMillimeterWaveRadarList(bizMillimeterWaveRadar);
    }

    /**
     * 新增毫米波雷达管理
     * 
     * @param bizMillimeterWaveRadar 毫米波雷达管理
     * @return 结果
     */
    @Override
    public int insertBizMillimeterWaveRadar(BizMillimeterWaveRadar bizMillimeterWaveRadar)
    {
        bizMillimeterWaveRadar.setCreateTime(DateUtils.getNowDate());
        return bizMillimeterWaveRadarMapper.insertBizMillimeterWaveRadar(bizMillimeterWaveRadar);
    }

    /**
     * 修改毫米波雷达管理
     * 
     * @param bizMillimeterWaveRadar 毫米波雷达管理
     * @return 结果
     */
    @Override
    public int updateBizMillimeterWaveRadar(BizMillimeterWaveRadar bizMillimeterWaveRadar)
    {
        bizMillimeterWaveRadar.setUpdateTime(DateUtils.getNowDate());
        return bizMillimeterWaveRadarMapper.updateBizMillimeterWaveRadar(bizMillimeterWaveRadar);
    }

    /**
     * 批量删除毫米波雷达管理
     * 
     * @param ids 需要删除的毫米波雷达管理主键
     * @return 结果
     */
    @Override
    public int deleteBizMillimeterWaveRadarByIds(Long[] ids)
    {
        return bizMillimeterWaveRadarMapper.deleteBizMillimeterWaveRadarByIds(ids);
    }

    /**
     * 删除毫米波雷达管理信息
     * 
     * @param id 毫米波雷达管理主键
     * @return 结果
     */
    @Override
    public int deleteBizMillimeterWaveRadarById(Long id)
    {
        return bizMillimeterWaveRadarMapper.deleteBizMillimeterWaveRadarById(id);
    }
}
