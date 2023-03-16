package cn.yhjz.bi.service;

import java.util.List;
import cn.yhjz.bi.domain.BiSql;

/**
 * SQL存储Service接口
 * 
 * @author yhjz
 * @date 2022-01-05
 */
public interface IBiSqlService 
{
    /**
     * 查询SQL存储
     * 
     * @param bsId SQL存储主键
     * @return SQL存储
     */
    public BiSql selectBiSqlByBsId(Long bsId);

    /**
     * 查询SQL存储列表
     * 
     * @param biSql SQL存储
     * @return SQL存储集合
     */
    public List<BiSql> selectBiSqlList(BiSql biSql);

    /**
     * 新增SQL存储
     * 
     * @param biSql SQL存储
     * @return 结果
     */
    public int insertBiSql(BiSql biSql);

    /**
     * 修改SQL存储
     * 
     * @param biSql SQL存储
     * @return 结果
     */
    public int updateBiSql(BiSql biSql);

    /**
     * 批量删除SQL存储
     * 
     * @param bsIds 需要删除的SQL存储主键集合
     * @return 结果
     */
    public int deleteBiSqlByBsIds(Long[] bsIds);

}
