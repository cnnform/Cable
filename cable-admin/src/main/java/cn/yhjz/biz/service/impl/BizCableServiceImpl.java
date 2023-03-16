package cn.yhjz.biz.service.impl;

import java.util.List;

import cn.yhjz.biz.domain.BizDevice;
import cn.yhjz.biz.mapper.BizDeviceMapper;
import cn.yhjz.biz.vo.BizCableVo;
import cn.yhjz.biz.vo.BizDeviceVo;
import cn.yhjz.common.core.domain.entity.SysUser;
import cn.yhjz.common.utils.DateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.yhjz.biz.mapper.BizCableMapper;
import cn.yhjz.biz.domain.BizCable;
import cn.yhjz.biz.service.IBizCableService;

/**
 * 线路Service业务层处理
 *
 * @author yhjz
 * @date 2022-09-01
 */
@Service
public class BizCableServiceImpl implements IBizCableService {
    @Autowired
    private BizCableMapper bizCableMapper;

    @Autowired
    private BizDeviceMapper bizDeviceMapper;

    /**
     * 查询线路
     *
     * @param id 线路主键
     * @return 线路
     */
    @Override
    public BizCable selectBizCableById(Long id) {
        return bizCableMapper.selectBizCableById(id);
    }

    @Override
    public List<SysUser> selectChargeByClass(Long classId) {
        return bizCableMapper.selectChargeByClass(classId);
    }

    /**
     * 查询线路列表
     *
     * @param bizCable 线路
     * @return 线路
     */
    @Override
    public List<BizCableVo> selectBizCableList(BizCable bizCable) {
        List<BizCableVo> cableVoList = bizCableMapper.selectBizCableList(bizCable);
        return cableVoList;
    }

    /**
     * 新增线路
     *
     * @param bizCable 线路
     * @return 结果
     */
    @Override
    public int insertBizCable(BizCable bizCable) {
        bizCable.setCreateTime(DateUtils.getNowDate());
        return bizCableMapper.insertBizCable(bizCable);
    }

    /**
     * 修改线路
     *
     * @param bizCable 线路
     * @return 结果
     */
    @Override
    public int updateBizCable(BizCable bizCable) {
        bizCable.setUpdateTime(DateUtils.getNowDate());
        return bizCableMapper.updateBizCable(bizCable);
    }

    /**
     * 批量删除线路
     *
     * @param ids 需要删除的线路主键
     * @return 结果
     */
    @Override
    public int deleteBizCableByIds(Long[] ids) {
        return bizCableMapper.deleteBizCableByIds(ids);
    }

    /**
     * 删除线路信息
     *
     * @param id 线路主键
     * @return 结果
     */
    @Override
    public int deleteBizCableById(Long id) {
        return bizCableMapper.deleteBizCableById(id);
    }

    @Override
    public SysUser selectInchargeByCableId(Long cableId) {
        return bizCableMapper.selectInchargeByCableId(cableId);
    }

    /**
     * 查询线路列表，包含它绑定的定位设备列表
     *
     * @param bizCable
     * @return
     */
    @Override
    public List<BizCableVo> listWithDevice(BizCable bizCable) {

        List<BizCableVo> cableVoList = bizCableMapper.selectBizCableList(bizCable);
        //获得一个线缆上的所有定位设备，并按照序号排列
        if (CollectionUtils.isNotEmpty(cableVoList)) {
            BizDevice param = new BizDevice();
            for (BizCableVo vo : cableVoList) {
                param.setCableId(vo.getId());
                List<BizDeviceVo> deviceVoList = bizDeviceMapper.selectBizDeviceList(param);
                vo.setDeviceList(deviceVoList);
            }
        }
        return cableVoList;
    }
}
