package cn.yhjz.biz.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.yhjz.biz.domain.BizAlarm;
import cn.yhjz.biz.service.IBizAlarmService;
import cn.yhjz.common.annotation.Log;
import cn.yhjz.common.core.controller.BaseController;
import cn.yhjz.common.core.domain.AjaxResult;
import cn.yhjz.common.core.page.TableDataInfo;
import cn.yhjz.common.enums.BusinessType;
import cn.yhjz.common.utils.poi.ExcelUtil;
import org.joda.time.DateTime;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 报警管理Controller
 *
 * @author yhjz
 * @date 2022-05-07
 */
@RestController
@RequestMapping("/biz/alarm")
public class BizAlarmController extends BaseController {

    @Autowired
    private IBizAlarmService bizAlarmService;

    /**
     * 查询报警管理列表
     */
    @PreAuthorize("@ss.hasPermi('biz:alarm:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizAlarm bizAlarm) {
        startPage();
        List<BizAlarm> list = bizAlarmService.selectBizAlarmList(bizAlarm);
        return getDataTable(list);
    }

    /**
     * 导出报警管理列表
     */
    @PreAuthorize("@ss.hasPermi('biz:alarm:export')")
    @Log(title = "报警管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizAlarm bizAlarm) {
        List<BizAlarm> list = bizAlarmService.selectBizAlarmList(bizAlarm);
        ExcelUtil<BizAlarm> util = new ExcelUtil<BizAlarm>(BizAlarm.class);
        util.exportExcel(response, list, "报警管理数据");
    }

    /**
     * 获取报警管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:alarm:query')")
    @GetMapping(value = "/{alarmId}")
    public AjaxResult getInfo(@PathVariable("alarmId") Long alarmId) {
        return AjaxResult.success(bizAlarmService.selectBizAlarmByAlarmId(alarmId));
    }

    /**
     * 新增报警管理
     */
    @PreAuthorize("@ss.hasPermi('biz:alarm:add')")
    @Log(title = "报警管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizAlarm bizAlarm) {
        return toAjax(bizAlarmService.insertBizAlarm(bizAlarm));
    }

    /**
     * 修改报警管理
     */
    @PreAuthorize("@ss.hasPermi('biz:alarm:edit')")
    @Log(title = "报警管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizAlarm bizAlarm) {
        return toAjax(bizAlarmService.updateBizAlarm(bizAlarm));
    }

    /**
     * 删除报警管理
     */
    @PreAuthorize("@ss.hasPermi('biz:alarm:remove')")
    @Log(title = "报警管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{alarmIds}")
    public AjaxResult remove(@PathVariable Long[] alarmIds) {
        return toAjax(bizAlarmService.deleteBizAlarmByAlarmIds(alarmIds));
    }

    /**
     * 统计最近几天内，每天的警告数量
     *
     * @return
     */
    @GetMapping("/statistics")
    public AjaxResult statistics() {
        //最近n天
        int count = 10;
        DateTime now = DateTime.now();
        now.millisOfDay().setCopy(0);
        //7天之前
        //补全最近几天的数据

        DateTime startTime = now.minusDays(30);
        return AjaxResult.success(this.bizAlarmService.statistics(now, count));
    }

    @PreAuthorize("@ss.hasPermi('biz:alarm:list')")
    @GetMapping("/listNear")
    public AjaxResult listNear(@RequestParam Long count,@RequestParam Long timestamp){
        Date time = new Date(timestamp);
        return AjaxResult.success(this.bizAlarmService.listNear(count,time));
    }

}
