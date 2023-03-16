package cn.yhjz.bi.mapper;

import java.util.List;
import cn.yhjz.bi.domain.BiDs;

/**
 * 数据源管理Mapper接口
 * 
 * @author yhjz
 * @date 2022-01-04
 */
public interface BiDsMapper 
{
    /**
     * 查询数据源管理
     * 
     * @param dsId 数据源管理主键
     * @return 数据源管理
     */
    public BiDs selectBiDsByDsId(Long dsId);

    /**
     * 查询数据源管理列表
     * 
     * @param biDs 数据源管理
     * @return 数据源管理集合
     */
    public List<BiDs> selectBiDsList(BiDs biDs);

    /**
     * 新增数据源管理
     * 
     * @param biDs 数据源管理
     * @return 结果
     */
    public int insertBiDs(BiDs biDs);

    /**
     * 修改数据源管理
     * 
     * @param biDs 数据源管理
     * @return 结果
     */
    public int updateBiDs(BiDs biDs);

    /**
     * 删除数据源管理
     * 
     * @param dsId 数据源管理主键
     * @return 结果
     */
    public int deleteBiDsByDsId(Long dsId);

    /**
     * 批量删除数据源管理
     * 
     * @param dsIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBiDsByDsIds(Long[] dsIds);

}
