package cn.yhjz.biz.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

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
import cn.yhjz.common.annotation.Log;
import cn.yhjz.common.core.controller.BaseController;
import cn.yhjz.common.core.domain.AjaxResult;
import cn.yhjz.common.core.page.TableDataInfo;
import cn.yhjz.common.enums.BusinessType;
import cn.yhjz.biz.domain.AlertAction;
import cn.yhjz.biz.service.IAlertActionService;


/**
 * 报警动作Controller
 * 
 * @author maguoping
 * @date 2022-07-28
 */
@RestController
@RequestMapping("/biz/alertAction")
public class AlertActionController extends BaseController
{
    @Autowired
    private IAlertActionService alertActionService;

    /**
     * 查询报警动作列表
     */
    @PreAuthorize("@ss.hasPermi('biz:alertAction:list')")
    @GetMapping("/list")
    public TableDataInfo list(AlertAction alertAction)
    {
        startPage();
        List<AlertAction> list = alertActionService.selectAlertActionList(alertAction);
        return getDataTable(list);
    }

    /**
     * 导出报警动作列表
     */
    @PreAuthorize("@ss.hasPermi('biz:alertAction:export')")
    @Log(title = "报警动作", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AlertAction alertAction)
    {
        List<AlertAction> list = alertActionService.selectAlertActionList(alertAction);
        ExcelUtil<AlertAction> util = new ExcelUtil<AlertAction>(AlertAction.class);
        util.exportExcel(response, list, "报警动作数据");
    }

    /**
     * 获取报警动作详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:alertAction:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(alertActionService.selectAlertActionById(id));
    }

    /**
     * 新增报警动作
     */
    @PreAuthorize("@ss.hasPermi('biz:alertAction:add')")
    @Log(title = "报警动作", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AlertAction alertAction)
    {
        return toAjax(alertActionService.insertAlertAction(alertAction));
    }

    /**
     * 修改报警动作
     */
    @PreAuthorize("@ss.hasPermi('biz:alertAction:edit')")
    @Log(title = "报警动作", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AlertAction alertAction)
    {
        return toAjax(alertActionService.updateAlertAction(alertAction));
    }

    /**
     * 删除报警动作
     */
    @PreAuthorize("@ss.hasPermi('biz:alertAction:remove')")
    @Log(title = "报警动作", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(alertActionService.deleteAlertActionByIds(ids));
    }
}
