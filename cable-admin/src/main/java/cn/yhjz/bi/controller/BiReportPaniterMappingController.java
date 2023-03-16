package cn.yhjz.bi.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.yhjz.common.annotation.Log;
import cn.yhjz.common.core.controller.BaseController;
import cn.yhjz.common.core.domain.AjaxResult;
import cn.yhjz.common.core.page.TableDataInfo;
import cn.yhjz.common.enums.BusinessType;
import cn.yhjz.common.utils.poi.ExcelUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.yhjz.bi.domain.BiReportPaniterMapping;
import cn.yhjz.bi.service.IBiReportPaniterMappingService;

/**
 * 看板报表管理
 * 
 * @author ldl
 * @date 2022-01-10
 */
@RestController
@RequestMapping("/bi/mapping")
public class BiReportPaniterMappingController extends BaseController
{
    @Autowired
    private IBiReportPaniterMappingService biReportPaniterMappingService;

    /**
     * 查询【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('bi:mapping:list')")
    @GetMapping("/list")
    public TableDataInfo list(BiReportPaniterMapping biReportPaniterMapping)
    {
        startPage();
        List<BiReportPaniterMapping> list = biReportPaniterMappingService.selectBiReportPaniterMappingList(biReportPaniterMapping);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('bi:mapping:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BiReportPaniterMapping biReportPaniterMapping)
    {
        List<BiReportPaniterMapping> list = biReportPaniterMappingService.selectBiReportPaniterMappingList(biReportPaniterMapping);
        ExcelUtil<BiReportPaniterMapping> util = new ExcelUtil<BiReportPaniterMapping>(BiReportPaniterMapping.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @PreAuthorize("@ss.hasPermi('bi:mapping:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(biReportPaniterMappingService.selectBiReportPaniterMappingById(id));
    }

    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('bi:mapping:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BiReportPaniterMapping biReportPaniterMapping)
    {
        return toAjax(biReportPaniterMappingService.insertBiReportPaniterMapping(biReportPaniterMapping));
    }

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('bi:mapping:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BiReportPaniterMapping biReportPaniterMapping)
    {
        return toAjax(biReportPaniterMappingService.updateBiReportPaniterMapping(biReportPaniterMapping));
    }

    /**
     * 删除【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('bi:mapping:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(biReportPaniterMappingService.deleteBiReportPaniterMappingByIds(ids));
    }

}
