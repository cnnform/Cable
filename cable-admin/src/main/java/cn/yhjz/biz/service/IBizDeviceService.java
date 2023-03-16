package cn.yhjz.biz.service;

import java.util.List;
import cn.yhjz.biz.domain.BizDevice;
import cn.yhjz.biz.vo.BizDeviceVo;

/**
 * 定位设备管理Service接口
 * 
 * @author yhjz
 * @date 2022-02-15
 */
public interface IBizDeviceService 
{
    /**
     * 查询定位设备管理
     * 
     * @param id 定位设备管理主键
     * @return 定位设备管理
     */
    public BizDevice selectBizDeviceById(Long id);

    void saveDeviceLocation(BizDevice bizDevice);

    /**
     * 查询定位设备管理列表
     * 
     * @param bizDevice 定位设备管理
     * @return 定位设备管理集合
     */
    public List<BizDeviceVo> selectBizDeviceList(BizDevice bizDevice);

    /**
     * 新增定位设备管理
     * 
     * @param bizDevice 定位设备管理
     * @return 结果
     */
    public int insertBizDevice(BizDevice bizDevice);

    /**
     * 修改定位设备管理
     * 
     * @param bizDevice 定位设备管理
     * @return 结果
     */
    public int updateBizDevice(BizDevice bizDevice);

    /**
     * 批量删除定位设备管理
     * 
     * @param ids 需要删除的定位设备管理主键集合
     * @return 结果
     */
    public int deleteBizDeviceByIds(Long[] ids);

    /**
     * 删除定位设备管理信息
     * 
     * @param id 定位设备管理主键
     * @return 结果
     */
    public int deleteBizDeviceById(Long id);


     int editSerialNum(List<BizDevice> bizDeviceList);
}
