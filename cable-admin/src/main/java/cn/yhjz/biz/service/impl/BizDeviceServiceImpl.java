package cn.yhjz.biz.service.impl;

import java.util.Date;
import java.util.List;

import cn.yhjz.biz.domain.BizDeviceLocation;
import cn.yhjz.biz.mapper.BizDeviceLocationMapper;
import cn.yhjz.biz.vo.BizDeviceVo;
import cn.yhjz.common.utils.DateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.yhjz.biz.mapper.BizDeviceMapper;
import cn.yhjz.biz.domain.BizDevice;
import cn.yhjz.biz.service.IBizDeviceService;

/**
 * 定位设备管理Service业务层处理
 *
 * @author yhjz
 * @date 2022-02-15
 */
@Service
public class BizDeviceServiceImpl implements IBizDeviceService
{
    @Autowired
    private BizDeviceMapper bizDeviceMapper;
    @Autowired
    private BizDeviceLocationMapper bizDeviceLocationMapper;

    /**
     * 查询定位设备管理
     *
     * @param id 定位设备管理主键
     * @return 定位设备管理
     */
    @Override
    public BizDevice selectBizDeviceById(Long id)
    {
        return bizDeviceMapper.selectBizDeviceById(id);
    }

    /**
     *
     * @param bizDevice
     */
    @Override
    public void saveDeviceLocation(BizDevice bizDevice){
        //查看设备是否存在，如果不存在就增加
        BizDevice param = new BizDevice();
        param.setDeviceId(bizDevice.getDeviceId());

        List<BizDeviceVo> oldDevices = bizDeviceMapper.selectBizDeviceList(param);
        if (CollectionUtils.isEmpty(oldDevices)) {
            bizDeviceMapper.insertBizDevice(bizDevice);
        }else{
            //更新进去
            bizDeviceMapper.updateBizDeviceByDeviceId(bizDevice);
        }

        //查找设备位置的数量，保留50个
        /*BizDeviceLocation paramLocation = new BizDeviceLocation();
        paramLocation.setDeviceId(bizDevice.getDeviceId());
        List<BizDeviceLocation> oldLocations = bizDeviceLocationMapper.selectBizDeviceLocationList(paramLocation);
        if (CollectionUtils.isNotEmpty(oldLocations) && oldLocations.size() >= 50) {
            //删除最早的一条
            bizDeviceLocationMapper.deleteBizDeviceLocationById(oldLocations.get(0).getId());
        }
        BizDeviceLocation newLocation = new BizDeviceLocation();
        newLocation.setDeviceId(bizDevice.getDeviceId());
        newLocation.setLat(bizDevice.getLat());
        newLocation.setLon(bizDevice.getLon());
        newLocation.setDepth(bizDevice.getDepth());
        newLocation.setCreateTime(new Date());
        newLocation.setCreateBy("_sys_");
        bizDeviceLocationMapper.insertBizDeviceLocation(newLocation);*/
    }

    /**
     * 查询定位设备管理列表
     *
     * @param bizDevice 定位设备管理
     * @return 定位设备管理
     */
    @Override
    public List<BizDeviceVo> selectBizDeviceList(BizDevice bizDevice)
    {
        return bizDeviceMapper.selectBizDeviceList(bizDevice);
    }

    /**
     * 新增定位设备管理
     *
     * @param bizDevice 定位设备管理
     * @return 结果
     */
    @Override
    public int insertBizDevice(BizDevice bizDevice)
    {
        bizDevice.setCreateTime(DateUtils.getNowDate());
        return bizDeviceMapper.insertBizDevice(bizDevice);
    }

    /**
     * 修改定位设备管理
     *
     * @param bizDevice 定位设备管理
     * @return 结果
     */
    @Override
    public int updateBizDevice(BizDevice bizDevice)
    {
        bizDevice.setUpdateTime(DateUtils.getNowDate());
        return bizDeviceMapper.updateBizDevice(bizDevice);
    }

    /**
     * 批量删除定位设备管理
     *
     * @param ids 需要删除的定位设备管理主键
     * @return 结果
     */
    @Override
    public int deleteBizDeviceByIds(Long[] ids)
    {
        String[] deviceIds = new String[ids.length];
        for (int i=0;i<ids.length;i++) {
            Long id = ids[i];
            BizDevice bizDevice=this.bizDeviceMapper.selectBizDeviceById(id);
            deviceIds[i] = bizDevice.getDeviceId();
        }
        this.bizDeviceLocationMapper.deleteByDeviceIds(deviceIds);
        return bizDeviceMapper.deleteBizDeviceByIds(ids);
    }

    /**
     * 删除定位设备管理信息
     *
     * @param id 定位设备管理主键
     * @return 结果
     */
    @Override
    public int deleteBizDeviceById(Long id)
    {
        return bizDeviceMapper.deleteBizDeviceById(id);
    }

    @Override
    public int editSerialNum(List<BizDevice> bizDeviceList){
        int result=0;
        for (BizDevice bizDevice : bizDeviceList) {
            BizDevice n = new BizDevice();
            n.setId(bizDevice.getId());
            n.setSerialNo(bizDevice.getSerialNo());
            result+=bizDeviceMapper.updateBizDevice(n);
        }
        return result;
    }
}
