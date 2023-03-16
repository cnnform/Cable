package cn.yhjz.bi.service.impl;

import java.util.List;

import cn.yhjz.bi.mapper.BiSqlMapper;
import cn.yhjz.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.yhjz.bi.mapper.BiDsMapper;
import cn.yhjz.bi.domain.BiDs;
import cn.yhjz.bi.service.IBiDsService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据源管理Service业务层处理
 * 
 * @author yhjz
 * @date 2022-01-04
 */
@Service
public class BiDsServiceImpl implements IBiDsService 
{
    @Autowired
    private BiDsMapper biDsMapper;

    @Autowired
    private BiSqlMapper biSqlMapper;

    /**
     * 查询数据源管理
     * 
     * @param dsId 数据源管理主键
     * @return 数据源管理
     */
    @Override
    public BiDs selectBiDsByDsId(Long dsId)
    {
        return biDsMapper.selectBiDsByDsId(dsId);
    }

    /**
     * 查询数据源管理列表
     * 
     * @param biDs 数据源管理
     * @return 数据源管理
     */
    @Override
    public List<BiDs> selectBiDsList(BiDs biDs)
    {
        return biDsMapper.selectBiDsList(biDs);
    }

    /**
     * 新增数据源管理
     * 
     * @param biDs 数据源管理
     * @return 结果
     */
    @Override
    public int insertBiDs(BiDs biDs)
    {
        biDs.setDsCreateTime(DateUtils.getNowDate());
        return biDsMapper.insertBiDs(biDs);
    }

    /**
     * 修改数据源管理
     * 
     * @param biDs 数据源管理
     * @return 结果
     */
    @Override
    public int updateBiDs(BiDs biDs)
    {
        return biDsMapper.updateBiDs(biDs);
    }

    /**
     * 批量删除数据源管理
     * 
     * @param dsIds 需要删除的数据源管理主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteBiDsByDsIds(Long[] dsIds) {
        biSqlMapper.deleteBiSqlByDsIds(dsIds);
        return biDsMapper.deleteBiDsByDsIds(dsIds);
    }

    /**
     * 删除数据源管理信息
     * 
     * @param dsId 数据源管理主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteBiDsByDsId(Long dsId)
    {
        Long[] dsIds = {dsId};
        biSqlMapper.deleteBiSqlByDsIds(dsIds);
        return biDsMapper.deleteBiDsByDsId(dsId);
    }

}
