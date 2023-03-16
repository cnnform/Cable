package cn.yhjz.biz.service.impl;

import java.util.List;

import cn.yhjz.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.yhjz.biz.mapper.BizDeviceLocationMapper;
import cn.yhjz.biz.domain.BizDeviceLocation;
import cn.yhjz.biz.service.IBizDeviceLocationService;

/**
 * 设备位置Service业务层处理
 * 
 * @author 马国平
 * @date 2022-04-09
 */
@Service
public class BizDeviceLocationServiceImpl implements IBizDeviceLocationService 
{
    @Autowired
    private BizDeviceLocationMapper bizDeviceLocationMapper;

    /**
     * 查询设备位置
     * 
     * @param id 设备位置主键
     * @return 设备位置
     */
    @Override
    public BizDeviceLocation selectBizDeviceLocationById(Long id)
    {
        return bizDeviceLocationMapper.selectBizDeviceLocationById(id);
    }

    /**
     * 查询设备位置列表
     * 
     * @param bizDeviceLocation 设备位置
     * @return 设备位置
     */
    @Override
    public List<BizDeviceLocation> selectBizDeviceLocationList(BizDeviceLocation bizDeviceLocation)
    {
        return bizDeviceLocationMapper.selectBizDeviceLocationList(bizDeviceLocation);
    }

    /**
     * 新增设备位置
     * 
     * @param bizDeviceLocation 设备位置
     * @return 结果
     */
    @Override
    public int insertBizDeviceLocation(BizDeviceLocation bizDeviceLocation)
    {
        bizDeviceLocation.setCreateTime(DateUtils.getNowDate());
        return bizDeviceLocationMapper.insertBizDeviceLocation(bizDeviceLocation);
    }

    /**
     * 修改设备位置
     * 
     * @param bizDeviceLocation 设备位置
     * @return 结果
     */
    @Override
    public int updateBizDeviceLocation(BizDeviceLocation bizDeviceLocation)
    {
        bizDeviceLocation.setUpdateTime(DateUtils.getNowDate());
        return bizDeviceLocationMapper.updateBizDeviceLocation(bizDeviceLocation);
    }

    /**
     * 批量删除设备位置
     * 
     * @param ids 需要删除的设备位置主键
     * @return 结果
     */
    @Override
    public int deleteBizDeviceLocationByIds(Long[] ids)
    {
        return bizDeviceLocationMapper.deleteBizDeviceLocationByIds(ids);
    }

    /**
     * 删除设备位置信息
     * 
     * @param id 设备位置主键
     * @return 结果
     */
    @Override
    public int deleteBizDeviceLocationById(Long id)
    {
        return bizDeviceLocationMapper.deleteBizDeviceLocationById(id);
    }
}
