package cn.yhjz.biz.mapper;

import cn.yhjz.biz.domain.BizArea;

import java.util.List;

/**
 * 网络区域Mapper接口
 *
 * @author yhjz
 * @date 2022-04-27
 */
public interface BizAreaMapper {
    /**
     * 查询网络区域
     *
     * @param id 网络区域主键
     * @return 网络区域
     */
    public BizArea selectBizAreaById(Long id);

    /**
     * 查询网络区域列表
     *
     * @param bizArea 网络区域
     * @return 网络区域集合
     */
    public List<BizArea> selectBizAreaList(BizArea bizArea);

    /**
     * 新增网络区域
     *
     * @param bizArea 网络区域
     * @return 结果
     */
    public int insertBizArea(BizArea bizArea);

    /**
     * 修改网络区域
     *
     * @param bizArea 网络区域
     * @return 结果
     */
    public int updateBizArea(BizArea bizArea);

    /**
     * 删除网络区域
     *
     * @param id 网络区域主键
     * @return 结果
     */
    public int deleteBizAreaById(Long id);

    /**
     * 批量删除网络区域
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizAreaByIds(Long[] ids);
}
