package cn.yhjz.bi.service.impl;

import java.util.List;

import cn.yhjz.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.yhjz.bi.mapper.BiSqlMapper;
import cn.yhjz.bi.domain.BiSql;
import cn.yhjz.bi.service.IBiSqlService;

/**
 * SQL存储Service业务层处理
 * 
 * @author yhjz
 * @date 2022-01-05
 */
@Service
public class BiSqlServiceImpl implements IBiSqlService 
{
    @Autowired
    private BiSqlMapper biSqlMapper;

    /**
     * 查询SQL存储
     * 
     * @param bsId SQL存储主键
     * @return SQL存储
     */
    @Override
    public BiSql selectBiSqlByBsId(Long bsId)
    {
        return biSqlMapper.selectBiSqlByBsId(bsId);
    }

    /**
     * 查询SQL存储列表
     * 
     * @param biSql SQL存储
     * @return SQL存储
     */
    @Override
    public List<BiSql> selectBiSqlList(BiSql biSql)
    {
        return biSqlMapper.selectBiSqlList(biSql);
    }

    /**
     * 新增SQL存储
     * 
     * @param biSql SQL存储
     * @return 结果
     */
    @Override
    public int insertBiSql(BiSql biSql)
    {
        biSql.setCreateTime(DateUtils.getNowDate());
        return biSqlMapper.insertBiSql(biSql);
    }

    /**
     * 修改SQL存储
     * 
     * @param biSql SQL存储
     * @return 结果
     */
    @Override
    public int updateBiSql(BiSql biSql)
    {
        biSql.setUpdateTime(DateUtils.getNowDate());
        return biSqlMapper.updateBiSql(biSql);
    }

    /**
     * 批量删除SQL存储
     * 
     * @param bsIds 需要删除的SQL存储主键
     * @return 结果
     */
    @Override
    public int deleteBiSqlByBsIds(Long[] bsIds)
    {
        return biSqlMapper.deleteBiSqlByBsIds(bsIds);
    }

}
