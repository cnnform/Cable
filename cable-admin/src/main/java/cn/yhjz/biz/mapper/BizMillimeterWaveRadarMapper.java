package cn.yhjz.biz.mapper;

import cn.yhjz.biz.domain.BizMillimeterWaveRadar;

import java.util.List;


/**
 * 毫米波雷达管理Mapper接口
 * 
 * @author yhjz
 * @date 2022-11-06
 */
public interface BizMillimeterWaveRadarMapper 
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
     * 删除毫米波雷达管理
     * 
     * @param id 毫米波雷达管理主键
     * @return 结果
     */
    public int deleteBizMillimeterWaveRadarById(Long id);

    /**
     * 批量删除毫米波雷达管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizMillimeterWaveRadarByIds(Long[] ids);
}
