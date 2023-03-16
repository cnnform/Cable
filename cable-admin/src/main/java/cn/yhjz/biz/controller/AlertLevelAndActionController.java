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
import cn.yhjz.biz.domain.AlertLevelAndAction;
import cn.yhjz.biz.service.IAlertLevelAndActionService;
import cn.yhjz.common.utils.poi.ExcelUtil;

/**
 * 报警的动作配置Controller
 * 
 * @author maguoping
 * @date 2022-07-28
 */
@RestController
@RequestMapping("/biz/alertLevelAndAction")
public class AlertLevelAndActionController extends BaseController
{
    @Autowired
    private IAlertLevelAndActionService alertLevelAndActionService;

    /**
     * 查询报警的动作配置列表
     */
    @PreAuthorize("@ss.hasPermi('biz:alertLevelAndAction:list')")
    @GetMapping("/list")
    public TableDataInfo list(AlertLevelAndAction alertLevelAndAction)
    {
        startPage();
        List<AlertLevelAndAction> list = alertLevelAndActionService.selectAlertLevelAndActionList(alertLevelAndAction);
        return getDataTable(list);
    }

    /**
     * 导出报警的动作配置列表
     */
    @PreAuthorize("@ss.hasPermi('biz:alertLevelAndAction:export')")
    @Log(title = "报警的动作配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AlertLevelAndAction alertLevelAndAction)
    {
        List<AlertLevelAndAction> list = alertLevelAndActionService.selectAlertLevelAndActionList(alertLevelAndAction);
        ExcelUtil<AlertLevelAndAction> util = new ExcelUtil<AlertLevelAndAction>(AlertLevelAndAction.class);
        util.exportExcel(response, list, "报警的动作配置数据");
    }

    /**
     * 获取报警的动作配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:alertLevelAndAction:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(alertLevelAndActionService.selectAlertLevelAndActionById(id));
    }

    /**
     * 新增报警的动作配置
     */
    @PreAuthorize("@ss.hasPermi('biz:alertLevelAndAction:add')")
    @Log(title = "报警的动作配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AlertLevelAndAction alertLevelAndAction)
    {
        return toAjax(alertLevelAndActionService.insertAlertLevelAndAction(alertLevelAndAction));
    }

    /**
     * 修改报警的动作配置
     */
    @PreAuthorize("@ss.hasPermi('biz:alertLevelAndAction:edit')")
    @Log(title = "报警的动作配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AlertLevelAndAction alertLevelAndAction)
    {
        return toAjax(alertLevelAndActionService.updateAlertLevelAndAction(alertLevelAndAction));
    }

    /**
     * 删除报警的动作配置
     */
    @PreAuthorize("@ss.hasPermi('biz:alertLevelAndAction:remove')")
    @Log(title = "报警的动作配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(alertLevelAndActionService.deleteAlertLevelAndActionByIds(ids));
    }
    @PreAuthorize("@ss.hasPermi('biz:alertLevelAndAction:edit')")
    @Log(title = "保存一个等级的动作", businessType = BusinessType.UPDATE)
    @PostMapping("/saveLevelAndActions")
    public AjaxResult saveLevelAndActions(@RequestBody List<AlertLevelAndAction> actions,Long levelId){
        return toAjax(alertLevelAndActionService.saveLevelAndActions(levelId,actions));
    }
}
