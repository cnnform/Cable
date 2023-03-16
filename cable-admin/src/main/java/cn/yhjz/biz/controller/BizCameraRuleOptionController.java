package cn.yhjz.biz.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import cn.yhjz.biz.domain.BizCameraRuleOption;
import cn.yhjz.biz.service.IBizCameraRuleOptionService;
import cn.yhjz.common.utils.poi.ExcelUtil;

/**
 * 摄像头规则配置项的值Controller
 * 
 * @author maguoping
 * @date 2022-08-03
 */
@RestController
@RequestMapping("/biz/cameraRuleOption")
public class BizCameraRuleOptionController extends BaseController
{
    @Autowired
    private IBizCameraRuleOptionService bizCameraRuleOptionService;

    /**
     * 查询摄像头规则配置项的值列表
     */
    @PreAuthorize("@ss.hasPermi('biz:cameraRuleOption:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizCameraRuleOption bizCameraRuleOption)
    {
        startPage();
        List<BizCameraRuleOption> list = bizCameraRuleOptionService.selectBizCameraRuleOptionList(bizCameraRuleOption);
        return getDataTable(list);
    }

    /**
     * 导出摄像头规则配置项的值列表
     */
    @PreAuthorize("@ss.hasPermi('biz:cameraRuleOption:export')")
    @Log(title = "摄像头规则配置项的值", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizCameraRuleOption bizCameraRuleOption)
    {
        List<BizCameraRuleOption> list = bizCameraRuleOptionService.selectBizCameraRuleOptionList(bizCameraRuleOption);
        ExcelUtil<BizCameraRuleOption> util = new ExcelUtil<BizCameraRuleOption>(BizCameraRuleOption.class);
        util.exportExcel(response, list, "摄像头规则配置项的值数据");
    }

    /**
     * 获取摄像头规则配置项的值详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:cameraRuleOption:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(bizCameraRuleOptionService.selectBizCameraRuleOptionById(id));
    }

    /**
     * 新增摄像头规则配置项的值
     */
    @PreAuthorize("@ss.hasPermi('biz:cameraRuleOption:add')")
    @Log(title = "摄像头规则配置项的值", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizCameraRuleOption bizCameraRuleOption)
    {
        return toAjax(bizCameraRuleOptionService.insertBizCameraRuleOption(bizCameraRuleOption));
    }

    /**
     * 修改摄像头规则配置项的值
     */
    @PreAuthorize("@ss.hasPermi('biz:cameraRuleOption:edit')")
    @Log(title = "摄像头规则配置项的值", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizCameraRuleOption bizCameraRuleOption)
    {
        return toAjax(bizCameraRuleOptionService.updateBizCameraRuleOption(bizCameraRuleOption));
    }

    /**
     * 删除摄像头规则配置项的值
     */
    @PreAuthorize("@ss.hasPermi('biz:cameraRuleOption:remove')")
    @Log(title = "摄像头规则配置项的值", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bizCameraRuleOptionService.deleteBizCameraRuleOptionByIds(ids));
    }
}
