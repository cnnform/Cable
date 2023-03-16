package cn.yhjz.bi.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.yhjz.common.annotation.Log;
import cn.yhjz.common.core.controller.BaseController;
import cn.yhjz.common.core.domain.AjaxResult;
import cn.yhjz.common.core.page.TableDataInfo;
import cn.yhjz.common.enums.BusinessType;
import cn.yhjz.common.utils.poi.ExcelUtil;
import cn.yhjz.common.utils.uuid.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.yhjz.bi.domain.BiReportManager;
import cn.yhjz.bi.service.IBiReportManagerService;

/**
 * 报表管理功能
 * 
 * @author ldl
 * @date 2022-01-10
 */
@RestController
@RequestMapping("/bi/reportManager")
public class BiReportManagerController extends BaseController
{

    @Autowired
    private IBiReportManagerService biReportManagerService;

    /**
     * 查询【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('bi:report:list')")
    @GetMapping("/list")
    public TableDataInfo list(BiReportManager biReportManager)
    {
        startPage();
        List<BiReportManager> list = biReportManagerService.selectBiReportManagerList(biReportManager);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('bi:report:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BiReportManager biReportManager)
    {
        List<BiReportManager> list = biReportManagerService.selectBiReportManagerList(biReportManager);
        ExcelUtil<BiReportManager> util = new ExcelUtil<BiReportManager>(BiReportManager.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @PreAuthorize("@ss.hasPermi('bi:report:query')")
    @GetMapping(value = "/{rmId}")
    public AjaxResult getInfo(@PathVariable("rmId") Long rmId)
    {
        return AjaxResult.success(biReportManagerService.selectBiReportManagerByRmId(rmId));
    }

    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('bi:report:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BiReportManager biReportManager)
    {
        if (null==biReportManager.getRmId()) {
            biReportManager.setRmCode(UUID.fastUUID().toString(true));
            biReportManager.setCreateUser(getUsername());
            return toAjax(biReportManagerService.insertBiReportManager(biReportManager));
        }else{
            biReportManager.setUpdateUser(getUsername());
            return toAjax(biReportManagerService.updateBiReportManager(biReportManager));
        }
    }

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('bi:report:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BiReportManager biReportManager)
    {
        biReportManager.setUpdateUser(getUsername());
        return toAjax(biReportManagerService.updateBiReportManager(biReportManager));
    }

    /**
     * 删除【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('bi:report:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
	@DeleteMapping("/{rmIds}")
    public AjaxResult remove(@PathVariable Long[] rmIds)
    {
        return toAjax(biReportManagerService.deleteBiReportManagerByRmIds(rmIds));
    }
}
