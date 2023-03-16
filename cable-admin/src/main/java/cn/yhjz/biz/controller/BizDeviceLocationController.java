package cn.yhjz.biz.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.yhjz.biz.domain.BizDeviceLocation;
import cn.yhjz.biz.service.IBizDeviceLocationService;


/**
 * 设备位置Controller
 * 
 * @author 马国平
 * @date 2022-04-09
 */
@RestController
@RequestMapping("/biz/location")
public class BizDeviceLocationController extends BaseController
{
    @Autowired
    private IBizDeviceLocationService bizDeviceLocationService;

    /**
     * 查询设备位置列表
     */
    @PreAuthorize("@ss.hasPermi('biz:location:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizDeviceLocation bizDeviceLocation)
    {
        startPage();
        List<BizDeviceLocation> list = bizDeviceLocationService.selectBizDeviceLocationList(bizDeviceLocation);
        return getDataTable(list);
    }

    /**
     * 导出设备位置列表
     */
    @PreAuthorize("@ss.hasPermi('biz:location:export')")
    @Log(title = "设备位置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizDeviceLocation bizDeviceLocation)
    {
        List<BizDeviceLocation> list = bizDeviceLocationService.selectBizDeviceLocationList(bizDeviceLocation);
        ExcelUtil<BizDeviceLocation> util = new ExcelUtil<BizDeviceLocation>(BizDeviceLocation.class);
        util.exportExcel(response, list, "设备位置数据");
    }

    /**
     * 获取设备位置详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:location:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(bizDeviceLocationService.selectBizDeviceLocationById(id));
    }

    /**
     * 新增设备位置
     */
    @PreAuthorize("@ss.hasPermi('biz:location:add')")
    @Log(title = "设备位置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizDeviceLocation bizDeviceLocation)
    {
        return toAjax(bizDeviceLocationService.insertBizDeviceLocation(bizDeviceLocation));
    }

    /**
     * 修改设备位置
     */
    @PreAuthorize("@ss.hasPermi('biz:location:edit')")
    @Log(title = "设备位置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizDeviceLocation bizDeviceLocation)
    {
        return toAjax(bizDeviceLocationService.updateBizDeviceLocation(bizDeviceLocation));
    }

    /**
     * 删除设备位置
     */
    @PreAuthorize("@ss.hasPermi('biz:location:remove')")
    @Log(title = "设备位置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bizDeviceLocationService.deleteBizDeviceLocationByIds(ids));
    }
}
