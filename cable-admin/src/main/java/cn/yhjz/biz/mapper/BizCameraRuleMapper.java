package cn.yhjz.biz.mapper;

import java.util.List;

import cn.yhjz.biz.domain.BizCameraRule;
import org.apache.ibatis.annotations.Param;

/**
 * 摄像头和规则关联Mapper接口
 *
 * @author maguoping
 * @date 2022-08-01
 */
public interface BizCameraRuleMapper {
    /**
     * 查询摄像头和规则关联
     *
     * @param id 摄像头和规则关联主键
     * @return 摄像头和规则关联
     */
    public BizCameraRule selectBizCameraRuleById(Long id);

    /**
     * 查询摄像头和规则关联列表
     *
     * @param bizCameraRule 摄像头和规则关联
     * @return 摄像头和规则关联集合
     */
    public List<BizCameraRule> selectBizCameraRuleList(BizCameraRule bizCameraRule);

    /**
     * 新增摄像头和规则关联
     *
     * @param bizCameraRule 摄像头和规则关联
     * @return 结果
     */
    public int insertBizCameraRule(BizCameraRule bizCameraRule);

    /**
     * 修改摄像头和规则关联
     *
     * @param bizCameraRule 摄像头和规则关联
     * @return 结果
     */
    public int updateBizCameraRule(BizCameraRule bizCameraRule);

    /**
     * 删除摄像头和规则关联
     *
     * @param id 摄像头和规则关联主键
     * @return 结果
     */
    public int deleteBizCameraRuleById(Long id);

    /**
     * 批量删除摄像头和规则关联
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizCameraRuleByIds(Long[] ids);

    int deleteByCameraId(@Param("cameraId") Long cameraId);
}
