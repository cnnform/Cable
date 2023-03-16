package cn.yhjz.biz.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.yhjz.biz.vo.BizDeviceVo;
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
import cn.yhjz.biz.domain.BizDevice;
import cn.yhjz.biz.service.IBizDeviceService;
/**
 * 定位设备管理Controller
 * 
 * @author yhjz
 * @date 2022-02-15
 */
@RestController
@RequestMapping("biz/device")
public class BizDeviceController extends BaseController
{
    @Autowired
    private IBizDeviceService bizDeviceService;

    /**
     * 查询定位设备管理列表
     */
    @PreAuthorize("@ss.hasPermi('biz:device:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizDevice bizDevice)
    {
        startPage();
        List<BizDeviceVo> list = bizDeviceService.selectBizDeviceList(bizDevice);
        return getDataTable(list);
    }
    /**
     * 查询定位设备管理列表
     */
    @PreAuthorize("@ss.hasPermi('biz:device:edit')")
    @PostMapping("/editSerialNum")
    public AjaxResult editSerialNum(@RequestBody List<BizDevice> bizDeviceList)
    {
       return AjaxResult.success(bizDeviceService.editSerialNum(bizDeviceList));

    }
    /**
     * 导出定位设备管理列表
     */
    @PreAuthorize("@ss.hasPermi('biz:device:export')")
    @Log(title = "定位设备管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizDevice bizDevice)
    {
        List<BizDeviceVo> list = bizDeviceService.selectBizDeviceList(bizDevice);
        ExcelUtil<BizDeviceVo> util = new ExcelUtil<BizDeviceVo>(BizDeviceVo.class);
        util.exportExcel(response, list, "定位设备管理数据");
    }

    /**
     * 获取定位设备管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:device:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(bizDeviceService.selectBizDeviceById(id));
    }

    /**
     * 新增定位设备管理
     */
    @PreAuthorize("@ss.hasPermi('biz:device:add')")
    @Log(title = "定位设备管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizDevice bizDevice)
    {
        return toAjax(bizDeviceService.insertBizDevice(bizDevice));
    }

    /**
     * 修改定位设备管理
     */
    @PreAuthorize("@ss.hasPermi('biz:device:edit')")
    @Log(title = "定位设备管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizDevice bizDevice)
    {
        return toAjax(bizDeviceService.updateBizDevice(bizDevice));
    }

    /**
     * 删除定位设备管理
     */
    @PreAuthorize("@ss.hasPermi('biz:device:remove')")
    @Log(title = "定位设备管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bizDeviceService.deleteBizDeviceByIds(ids));
    }
}
