package cn.yhjz.biz.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.yhjz.biz.vo.AlertLevelVo;
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
import cn.yhjz.biz.domain.AlertLevel;
import cn.yhjz.biz.service.IAlertLevelService;

/**
 * 报警等级Controller
 *
 * @author maguoping
 * @date 2022-07-27
 */
@RestController
@RequestMapping("/biz/alertLevel")
public class AlertLevelController extends BaseController {
    @Autowired
    private IAlertLevelService alertLevelService;

    /**
     * 查询报警等级列表
     */
    @PreAuthorize("@ss.hasPermi('biz:alertLevel:list')")
    @GetMapping("/list")
    public TableDataInfo list(AlertLevel alertLevel) {
        startPage();
        List<AlertLevel> list = alertLevelService.selectAlertLevelList(alertLevel);
        return getDataTable(list);
    }

    /**
     * 导出报警等级列表
     */
    @PreAuthorize("@ss.hasPermi('biz:alertLevel:export')")
    @Log(title = "报警等级", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AlertLevel alertLevel) {
        List<AlertLevel> list = alertLevelService.selectAlertLevelList(alertLevel);
        ExcelUtil<AlertLevel> util = new ExcelUtil<AlertLevel>(AlertLevel.class);
        util.exportExcel(response, list, "报警等级数据");
    }

    /**
     * 获取报警等级详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:alertLevel:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(alertLevelService.selectAlertLevelById(id));
    }

    /**
     * 新增报警等级
     */
    @PreAuthorize("@ss.hasPermi('biz:alertLevel:add')")
    @Log(title = "报警等级", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AlertLevel alertLevel) {
        return toAjax(alertLevelService.insertAlertLevel(alertLevel));
    }

    /**
     * 修改报警等级
     */
    @PreAuthorize("@ss.hasPermi('biz:alertLevel:edit')")
    @Log(title = "报警等级", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AlertLevel alertLevel) {
        return toAjax(alertLevelService.updateAlertLevel(alertLevel));
    }

    /**
     * 删除报警等级
     */
    @PreAuthorize("@ss.hasPermi('biz:alertLevel:remove')")
    @Log(title = "报警等级", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(alertLevelService.deleteAlertLevelByIds(ids));
    }

    /**
     * 根据报警等级获得报警等级所有配置项
     *
     * @return
     */
    @PostMapping("getAlertLevelDetailByLevelId")
    public AjaxResult getAlertLevelDetailByLevelId(Long levelId) {
        AlertLevelVo alertLevelVo = this.alertLevelService.getAlertLevelDetailByLevelId(levelId);

        return AjaxResult.success(alertLevelVo);
    }

    /**
     * 获得所有报警等级的详细配置
     *
     * @return
     */
    @PostMapping("getAlertLevelDetail")
    public AjaxResult getAlertLevelDetail() {
        List<AlertLevelVo> alertLevelVoList = new ArrayList<>();

        List<AlertLevel> alertLevelList = this.alertLevelService.selectAlertLevelList(null);
        for (AlertLevel level : alertLevelList) {
            AlertLevelVo alertLevelVo = this.alertLevelService.getAlertLevelDetailByLevelId(level.getId());
            alertLevelVoList.add(alertLevelVo);
        }
        return AjaxResult.success(alertLevelVoList);
    }
}
