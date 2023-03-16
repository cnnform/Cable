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
import cn.yhjz.biz.domain.AlertActionAttr;
import cn.yhjz.biz.service.IAlertActionAttrService;

/**
 * 动作的属性Controller
 * 
 * @author maguoping
 * @date 2022-07-27
 */
@RestController
@RequestMapping("/biz/alertActionAttr")
public class AlertActionAttrController extends BaseController
{
    @Autowired
    private IAlertActionAttrService alertActionAttrService;

    /**
     * 查询动作的属性列表
     */
    @PreAuthorize("@ss.hasPermi('biz:alertActionAttr:list')")
    @GetMapping("/list")
    public TableDataInfo list(AlertActionAttr alertActionAttr)
    {
        startPage();
        List<AlertActionAttr> list = alertActionAttrService.selectAlertActionAttrList(alertActionAttr);
        return getDataTable(list);
    }

    /**
     * 导出动作的属性列表
     */
    @PreAuthorize("@ss.hasPermi('biz:alertActionAttr:export')")
    @Log(title = "动作的属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AlertActionAttr alertActionAttr)
    {
        List<AlertActionAttr> list = alertActionAttrService.selectAlertActionAttrList(alertActionAttr);
        ExcelUtil<AlertActionAttr> util = new ExcelUtil<AlertActionAttr>(AlertActionAttr.class);
        util.exportExcel(response, list, "动作的属性数据");
    }

    /**
     * 获取动作的属性详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:alertActionAttr:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(alertActionAttrService.selectAlertActionAttrById(id));
    }

    /**
     * 新增动作的属性
     */
    @PreAuthorize("@ss.hasPermi('biz:alertActionAttr:add')")
    @Log(title = "动作的属性", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AlertActionAttr alertActionAttr)
    {
        return toAjax(alertActionAttrService.insertAlertActionAttr(alertActionAttr));
    }

    /**
     * 修改动作的属性
     */
    @PreAuthorize("@ss.hasPermi('biz:alertActionAttr:edit')")
    @Log(title = "动作的属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AlertActionAttr alertActionAttr)
    {
        return toAjax(alertActionAttrService.updateAlertActionAttr(alertActionAttr));
    }

    /**
     * 删除动作的属性
     */
    @PreAuthorize("@ss.hasPermi('biz:alertActionAttr:remove')")
    @Log(title = "动作的属性", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(alertActionAttrService.deleteAlertActionAttrByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('biz:alertActionAttr:add')")
    @Log(title = "动作的属性", businessType = BusinessType.DELETE)
    @PostMapping("/saveActionAttrs")
    public AjaxResult saveActionAttrs(@RequestBody List<AlertActionAttr> attrs)
    {
        //先删除原来的，再添加新的
        return toAjax(alertActionAttrService.saveActionAttrs(attrs));
    }
}
