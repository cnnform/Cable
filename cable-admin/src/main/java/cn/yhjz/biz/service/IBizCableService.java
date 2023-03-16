package cn.yhjz.biz.service;

import java.util.List;
import cn.yhjz.biz.domain.BizCable;
import cn.yhjz.biz.vo.BizCableVo;
import cn.yhjz.common.core.domain.entity.SysUser;

/**
 * 线路Service接口
 * 
 * @author yhjz
 * @date 2022-09-01
 */
public interface IBizCableService
{
    /**
     * 查询线路
     * 
     * @param id 线路主键
     * @return 线路
     */
    public BizCable selectBizCableById(Long id);
    List<SysUser> selectChargeByClass(Long classId);

    /**
     * 查询线路列表
     *
     * @param bizCable 线路
     * @return 线路集合
     */
    public List<BizCableVo> selectBizCableList(BizCable bizCable);

    /**
     * 查询线路列表，包含定位设备列表
     * @param bizCable
     * @return
     */
    List<BizCableVo> listWithDevice(BizCable bizCable);

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
     * 批量删除线路
     * 
     * @param ids 需要删除的线路主键集合
     * @return 结果
     */
    public int deleteBizCableByIds(Long[] ids);

    /**
     * 删除线路信息
     * 
     * @param id 线路主键
     * @return 结果
     */
    public int deleteBizCableById(Long id);

    SysUser selectInchargeByCableId(Long cableId);
}
