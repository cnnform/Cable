package cn.yhjz.biz.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import cn.yhjz.urule.UruleSelfServiceImpl;
import com.bstek.urule.model.rete.RuleData;
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
import cn.yhjz.biz.domain.RuleMain;
import cn.yhjz.biz.service.IRuleMainService;
import cn.yhjz.common.utils.poi.ExcelUtil;

/**
 * 报警规则Controller
 *
 * @author maguoping
 * @date 2022-08-01
 */
@RestController
@RequestMapping("/biz/ruleMain")
public class RuleMainController extends BaseController {
    @Autowired
    private IRuleMainService ruleMainService;
    @Autowired
    private UruleSelfServiceImpl uruleSelfService;

    /**
     * 查询报警规则列表
     */
    @PreAuthorize("@ss.hasPermi('biz:ruleMain:list')")
    @GetMapping("/list")
    public TableDataInfo list(RuleMain ruleMain) {
        startPage();
        List<RuleMain> list = ruleMainService.selectRuleMainList(ruleMain);
        return getDataTable(list);
    }


    //    @PreAuthorize("@ss.hasPermi('biz:ruleMain:list')")
    @GetMapping("/listUrule")
    public AjaxResult listUrule() {
        List<RuleData> list = uruleSelfService.getRuleList();
        List<Map<String, String>> mapList = new ArrayList<>();
        for (RuleData ruleData : list) {
            Map<String, String> ruleMap = new HashMap<>();
            ruleMap.put("code", ruleData.getName());
            ruleMap.put("name", ruleData.getName());
            mapList.add(ruleMap);
        }
        return AjaxResult.success(mapList);
    }

    /**
     * 导出报警规则列表
     */
    @PreAuthorize("@ss.hasPermi('biz:ruleMain:export')")
    @Log(title = "报警规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RuleMain ruleMain) {
        List<RuleMain> list = ruleMainService.selectRuleMainList(ruleMain);
        ExcelUtil<RuleMain> util = new ExcelUtil<RuleMain>(RuleMain.class);
        util.exportExcel(response, list, "报警规则数据");
    }

    /**
     * 获取报警规则详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:ruleMain:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(ruleMainService.selectRuleMainById(id));
    }

    /**
     * 新增报警规则
     */
    @PreAuthorize("@ss.hasPermi('biz:ruleMain:add')")
    @Log(title = "报警规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RuleMain ruleMain) {
        return toAjax(ruleMainService.insertRuleMain(ruleMain));
    }

    /**
     * 修改报警规则
     */
    @PreAuthorize("@ss.hasPermi('biz:ruleMain:edit')")
    @Log(title = "报警规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RuleMain ruleMain) {
        return toAjax(ruleMainService.updateRuleMain(ruleMain));
    }

    /**
     * 删除报警规则
     */
    @PreAuthorize("@ss.hasPermi('biz:ruleMain:remove')")
    @Log(title = "报警规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(ruleMainService.deleteRuleMainByIds(ids));
    }
}
