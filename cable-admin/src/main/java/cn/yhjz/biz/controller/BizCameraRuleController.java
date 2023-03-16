package cn.yhjz.biz.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.yhjz.biz.vo.AlertActionVo;
import cn.yhjz.biz.vo.CameraRuleVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.yhjz.common.annotation.Log;
import cn.yhjz.common.core.controller.BaseController;
import cn.yhjz.common.core.domain.AjaxResult;
import cn.yhjz.common.core.page.TableDataInfo;
import cn.yhjz.common.enums.BusinessType;
import cn.yhjz.biz.domain.BizCameraRule;
import cn.yhjz.biz.service.IBizCameraRuleService;
import cn.yhjz.common.utils.poi.ExcelUtil;

/**
 * 摄像头和规则关联Controller
 *
 * @author maguoping
 * @date 2022-08-01
 */
@RestController
@RequestMapping("/biz/cameraRule")
public class BizCameraRuleController extends BaseController {
    @Autowired
    private IBizCameraRuleService bizCameraRuleService;

    /**
     * 查询摄像头和规则关联列表
     */
    @PreAuthorize("@ss.hasPermi('biz:cameraRule:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizCameraRule bizCameraRule) {
        startPage();
        List<BizCameraRule> list = bizCameraRuleService.selectBizCameraRuleList(bizCameraRule);
        return getDataTable(list);
    }

    /**
     * 导出摄像头和规则关联列表
     */
    @PreAuthorize("@ss.hasPermi('biz:cameraRule:export')")
    @Log(title = "摄像头和规则关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizCameraRule bizCameraRule) {
        List<BizCameraRule> list = bizCameraRuleService.selectBizCameraRuleList(bizCameraRule);
        ExcelUtil<BizCameraRule> util = new ExcelUtil<BizCameraRule>(BizCameraRule.class);
        util.exportExcel(response, list, "摄像头和规则关联数据");
    }

    /**
     * 获取摄像头和规则关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:cameraRule:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(bizCameraRuleService.selectBizCameraRuleById(id));
    }

    /**
     * 新增摄像头和规则关联
     */
    @PreAuthorize("@ss.hasPermi('biz:cameraRule:add')")
    @Log(title = "摄像头和规则关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizCameraRule bizCameraRule) {
        return toAjax(bizCameraRuleService.insertBizCameraRule(bizCameraRule));
    }

    /**
     * 修改摄像头和规则关联
     */
    @PreAuthorize("@ss.hasPermi('biz:cameraRule:edit')")
    @Log(title = "摄像头和规则关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizCameraRule bizCameraRule) {
        return toAjax(bizCameraRuleService.updateBizCameraRule(bizCameraRule));
    }

    /**
     * 删除摄像头和规则关联
     */
    @PreAuthorize("@ss.hasPermi('biz:cameraRule:remove')")
    @Log(title = "摄像头和规则关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bizCameraRuleService.deleteBizCameraRuleByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('biz:cameraRule:add')")
    @Log(title = "摄像头和规则关联", businessType = BusinessType.UPDATE)
    @PostMapping("/saveCameraRules")
    public AjaxResult saveCameraRules(Long cameraId, @RequestBody List<CameraRuleVo> cameraRuleVoList) {
        if (cameraId == null) {
            return AjaxResult.error("need cameraId");
        }
        this.bizCameraRuleService.saveCameraRules(cameraId, cameraRuleVoList);
        return AjaxResult.success();
    }


    /**
     * 根据摄像头id获得摄像头关联的规则和等级
     *
     * @param cameraId
     * @return
     */
    @PreAuthorize("@ss.hasPermi('biz:cameraRule:query')")
    @PostMapping("/getDetailByCameraId")
    public AjaxResult getDetailByCameraId(Long cameraId) {
        List<BizCameraRule> cameraRuleList = this.bizCameraRuleService.getDetailByCameraId(cameraId);
        return AjaxResult.success(cameraRuleList);
    }

    /**
     * 根据s摄像头id获得摄像头配置的各动作的参数的值
     *
     * @param cameraId
     * @return
     */
    @PreAuthorize("@ss.hasPermi('biz:cameraRule:query')")
    @PostMapping("/getRuleOptionValueByCameraId")
    public AjaxResult getRuleOptionValueByCameraId(Long cameraId) {
        List<CameraRuleVo> cameraRuleVoList = this.bizCameraRuleService.getRuleOptionValueByCameraId(cameraId);
        return AjaxResult.success(cameraRuleVoList);
    }
}
