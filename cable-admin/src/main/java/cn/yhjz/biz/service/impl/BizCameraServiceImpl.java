package cn.yhjz.biz.service.impl;

import java.util.List;
import cn.yhjz.biz.domain.BizCamera;
import cn.yhjz.biz.mapper.BizCameraMapper;
import cn.yhjz.biz.service.IBizCameraService;
import cn.yhjz.biz.vo.BizCameraVo;
import cn.yhjz.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 摄像头管理Service业务层处理
 * 
 * @author yhjz
 * @date 2022-04-12
 */
@Service
public class BizCameraServiceImpl implements IBizCameraService {

    @Autowired
    private BizCameraMapper bizCameraMapper;

    /**
     * 查询摄像头管理
     * 
     * @param id 摄像头管理主键
     * @return 摄像头管理
     */
    @Override
    public BizCamera selectBizCameraById(Long id)
    {
        return bizCameraMapper.selectBizCameraById(id);
    }

    /**
     * 查询摄像头管理列表
     * 
     * @param bizCamera 摄像头管理
     * @return 摄像头管理
     */
    @Override
    public List<BizCameraVo> selectBizCameraList(BizCamera bizCamera) {
        return bizCameraMapper.selectBizCameraList(bizCamera);
    }

    /**
     * 新增摄像头管理
     * 
     * @param bizCamera 摄像头管理
     * @return 结果
     */
    @Override
    public int insertBizCamera(BizCamera bizCamera) {
        bizCamera.setCreateTime(DateUtils.getNowDate());
        return bizCameraMapper.insertBizCamera(bizCamera);
    }

    /**
     * 修改摄像头管理
     * 
     * @param bizCamera 摄像头管理
     * @return 结果
     */
    @Override
    public int updateBizCamera(BizCamera bizCamera) {
        bizCamera.setUpdateTime(DateUtils.getNowDate());
        return bizCameraMapper.updateBizCamera(bizCamera);
    }
    @Override
    public int updateBizCameraPTZ(BizCamera bizCamera) {
        bizCamera.setUpdateTime(DateUtils.getNowDate());
        return bizCameraMapper.updateBizCameraPTZ(bizCamera);
    }

    /**
     * 批量删除摄像头管理
     * 
     * @param ids 需要删除的摄像头管理主键
     * @return 结果
     */
    @Override
    public int deleteBizCameraByIds(Long[] ids)
    {
        return bizCameraMapper.deleteBizCameraByIds(ids);
    }

    /**
     * 删除摄像头管理信息
     * 
     * @param id 摄像头管理主键
     * @return 结果
     */
    @Override
    public int deleteBizCameraById(Long id)
    {
        return bizCameraMapper.deleteBizCameraById(id);
    }

    @Override
    public BizCamera selectBizCameraByDeviceId(String deviceId){
        return bizCameraMapper.selectBizCameraByDeviceId(deviceId);
    }
}
