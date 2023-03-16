package cn.yhjz.biz.service;

import java.util.List;

import cn.yhjz.biz.domain.BizCameraRule;
import cn.yhjz.biz.vo.CameraRuleVo;
import com.alibaba.fastjson.JSONObject;

/**
 * 摄像头和规则关联Service接口
 *
 * @author maguoping
 * @date 2022-08-01
 */
public interface IBizCameraRuleService {
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
     * 批量删除摄像头和规则关联
     *
     * @param ids 需要删除的摄像头和规则关联主键集合
     * @return 结果
     */
    public int deleteBizCameraRuleByIds(Long[] ids);

    /**
     * 删除摄像头和规则关联信息
     *
     * @param id 摄像头和规则关联主键
     * @return 结果
     */
    public int deleteBizCameraRuleById(Long id);

    public int saveCameraRules(Long id, List<CameraRuleVo> cameraRuleVoList);

    /**
     * 根据摄像头id获得
     *
     * @param cameraId
     * @return
     */
    List<BizCameraRule> getDetailByCameraId(Long cameraId);


    /**
     * 根据摄像头id获得配置项的值
     *
     * @param cameraId
     * @return
     */
    List<CameraRuleVo> getRuleOptionValueByCameraId(Long cameraId);

}
