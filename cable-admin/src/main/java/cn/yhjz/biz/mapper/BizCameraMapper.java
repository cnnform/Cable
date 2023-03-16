package cn.yhjz.biz.mapper;

import cn.yhjz.biz.domain.BizCamera;
import cn.yhjz.biz.vo.BizCameraVo;

import java.util.List;

/**
 * 摄像头管理Mapper接口
 * 
 * @author yhjz
 * @date 2022-04-12
 */
public interface BizCameraMapper {
    /**
     * 查询摄像头管理
     * 
     * @param id 摄像头管理主键
     * @return 摄像头管理
     */
    public BizCamera selectBizCameraById(Long id);

    /**
     * 查询摄像头管理列表
     * 
     * @param bizCamera 摄像头管理
     * @return 摄像头管理集合
     */
    public List<BizCameraVo> selectBizCameraList(BizCamera bizCamera);

    /**
     * 新增摄像头管理
     * 
     * @param bizCamera 摄像头管理
     * @return 结果
     */
    public int insertBizCamera(BizCamera bizCamera);

    /**
     * 修改摄像头管理
     * 
     * @param bizCamera 摄像头管理
     * @return 结果
     */
    public int updateBizCamera(BizCamera bizCamera);
    public int updateBizCameraPTZ(BizCamera bizCamera);

    /**
     * 删除摄像头管理
     * 
     * @param id 摄像头管理主键
     * @return 结果
     */
    public int deleteBizCameraById(Long id);

    /**
     * 批量删除摄像头管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizCameraByIds(Long[] ids);
    public BizCamera selectBizCameraByDeviceId(String deviceId);

    BizCameraVo selectDetailById(String deviceId);
}
