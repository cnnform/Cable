package cn.yhjz.biz.service.impl;

import java.util.List;

import cn.yhjz.biz.domain.BizArea;
import cn.yhjz.biz.mapper.BizAreaMapper;
import cn.yhjz.biz.service.IBizAreaService;
import cn.yhjz.common.utils.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 网络区域Service业务层处理
 *
 * @author yhjz
 * @date 2022-04-27
 */
@Service
public class BizAreaServiceImpl implements IBizAreaService {

    @Autowired
    private BizAreaMapper bizAreaMapper;

    /**
     * 查询网络区域
     *
     * @param id 网络区域主键
     * @return 网络区域
     */
    @Override
    public BizArea selectBizAreaById(Long id) {
        return bizAreaMapper.selectBizAreaById(id);
    }

    /**
     * 查询网络区域列表
     *
     * @param bizArea 网络区域
     * @return 网络区域
     */
    @Override
    public List<BizArea> selectBizAreaList(BizArea bizArea) {
        return bizAreaMapper.selectBizAreaList(bizArea);
    }

    /**
     * 新增网络区域
     *
     * @param bizArea 网络区域
     * @return 结果
     */
    @Override
    public int insertBizArea(BizArea bizArea) {
        bizArea.setAreaCode(UUID.fastUUID().toString(true));
        return bizAreaMapper.insertBizArea(bizArea);
    }

    /**
     * 修改网络区域
     *
     * @param bizArea 网络区域
     * @return 结果
     */
    @Override
    public int updateBizArea(BizArea bizArea) {
        return bizAreaMapper.updateBizArea(bizArea);
    }

    /**
     * 批量删除网络区域
     *
     * @param ids 需要删除的网络区域主键
     * @return 结果
     */
    @Override
    public int deleteBizAreaByIds(Long[] ids) {
        return bizAreaMapper.deleteBizAreaByIds(ids);
    }

    /**
     * 删除网络区域信息
     *
     * @param id 网络区域主键
     * @return 结果
     */
    @Override
    public int deleteBizAreaById(Long id) {
        return bizAreaMapper.deleteBizAreaById(id);
    }
}
