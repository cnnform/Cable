package cn.yhjz.biz.service;

import java.util.List;

import cn.yhjz.biz.domain.BizMillimeterWaveRadar;


/**
 * 毫米波雷达管理Service接口
 * 
 * @author yhjz
 * @date 2022-11-06
 */
public interface IBizMillimeterWaveRadarService 
{
    /**
     * 查询毫米波雷达管理
     * 
     * @param id 毫米波雷达管理主键
     * @return 毫米波雷达管理
     */
    public BizMillimeterWaveRadar selectBizMillimeterWaveRadarById(Long id);

    /**
     * 查询毫米波雷达管理列表
     * 
     * @param bizMillimeterWaveRadar 毫米波雷达管理
     * @return 毫米波雷达管理集合
     */
    public List<BizMillimeterWaveRadar> selectBizMillimeterWaveRadarList(BizMillimeterWaveRadar bizMillimeterWaveRadar);

    /**
     * 新增毫米波雷达管理
     * 
     * @param bizMillimeterWaveRadar 毫米波雷达管理
     * @return 结果
     */
    public int insertBizMillimeterWaveRadar(BizMillimeterWaveRadar bizMillimeterWaveRadar);

    /**
     * 修改毫米波雷达管理
     * 
     * @param bizMillimeterWaveRadar 毫米波雷达管理
     * @return 结果
     */
    public int updateBizMillimeterWaveRadar(BizMillimeterWaveRadar bizMillimeterWaveRadar);

    /**
     * 批量删除毫米波雷达管理
     * 
     * @param ids 需要删除的毫米波雷达管理主键集合
     * @return 结果
     */
    public int deleteBizMillimeterWaveRadarByIds(Long[] ids);

    /**
     * 删除毫米波雷达管理信息
     * 
     * @param id 毫米波雷达管理主键
     * @return 结果
     */
    public int deleteBizMillimeterWaveRadarById(Long id);
}
