package cn.yhjz.biz.mapper;

import java.util.List;
import cn.yhjz.biz.domain.BizDeviceLocation;

/**
 * 设备位置Mapper接口
 * 
 * @author 马国平
 * @date 2022-04-09
 */
public interface BizDeviceLocationMapper 
{
    /**
     * 查询设备位置
     * 
     * @param id 设备位置主键
     * @return 设备位置
     */
    public BizDeviceLocation selectBizDeviceLocationById(Long id);

    /**
     * 查询设备位置列表
     * 
     * @param bizDeviceLocation 设备位置
     * @return 设备位置集合
     */
    public List<BizDeviceLocation> selectBizDeviceLocationList(BizDeviceLocation bizDeviceLocation);

    /**
     * 新增设备位置
     * 
     * @param bizDeviceLocation 设备位置
     * @return 结果
     */
    public int insertBizDeviceLocation(BizDeviceLocation bizDeviceLocation);

    /**
     * 修改设备位置
     * 
     * @param bizDeviceLocation 设备位置
     * @return 结果
     */
    public int updateBizDeviceLocation(BizDeviceLocation bizDeviceLocation);

    /**
     * 删除设备位置
     * 
     * @param id 设备位置主键
     * @return 结果
     */
    public int deleteBizDeviceLocationById(Long id);

    /**
     * 批量删除设备位置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizDeviceLocationByIds(Long[] ids);

    int deleteByDeviceIds(String[] ids );
}
