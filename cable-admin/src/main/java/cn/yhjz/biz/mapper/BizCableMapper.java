package cn.yhjz.biz.mapper;

import java.util.List;
import cn.yhjz.biz.domain.BizCable;
import cn.yhjz.biz.vo.BizCableVo;
import cn.yhjz.common.core.domain.entity.SysUser;

/**
 * 线路Mapper接口
 * 
 * @author yhjz
 * @date 2022-09-01
 */
public interface BizCableMapper
{
    /**
     * 查询线路
     * 
     * @param id 线路主键
     * @return 线路
     */
    public BizCable selectBizCableById(Long id);

    /**
     * 查询线路列表
     * 
     * @param bizCable 线路
     * @return 线路集合
     */
    public List<BizCableVo> selectBizCableList(BizCable bizCable);

    /**
     * 根据班组id获得其下的所有负责人
     * @param deptId
     * @return
     */
    List<SysUser> selectChargeByClass(Long classId);

    /**
     * 新增线路
     * 
     * @param bizCable 线路
     * @return 结果
     */
    public int insertBizCable(BizCable bizCable);

    /**
     * 修改线路
     * 
     * @param bizCable 线路
     * @return 结果
     */
    public int updateBizCable(BizCable bizCable);

    /**
     * 删除线路
     * 
     * @param id 线路主键
     * @return 结果
     */
    public int deleteBizCableById(Long id);

    /**
     * 批量删除线路
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizCableByIds(Long[] ids);

    SysUser selectInchargeByCableId(Long cableId);
}
