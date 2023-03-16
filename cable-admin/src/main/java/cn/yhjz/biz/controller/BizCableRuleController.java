package cn.yhjz.biz.controller;

import cn.yhjz.biz.domain.BizCableRule;
import cn.yhjz.biz.domain.BizCameraRule;
import cn.yhjz.biz.service.IBizCableRuleService;
import cn.yhjz.biz.vo.CameraRuleVo;
import cn.yhjz.common.annotation.Log;
import cn.yhjz.common.core.controller.BaseController;
import cn.yhjz.common.core.domain.AjaxResult;
import cn.yhjz.common.core.page.TableDataInfo;
import cn.yhjz.common.enums.BusinessType;
import cn.yhjz.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 线路和规则关联Controller
 * 
 * @author maguoping
 * @date 2022-09-07
 */
@RestController
@RequestMapping("/biz/cableRule")
public class BizCableRuleController extends BaseController
{
    @Autowired
    private IBizCableRuleService bizCableRuleService;

    /**
     * 查询线路和规则关联列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BizCableRule bizCableRule)
    {
        startPage();
        List<BizCableRule> list = bizCableRuleService.selectBizCableRuleList(bizCableRule);
        return getDataTable(list);
    }

    /**
     * 导出线路和规则关联列表
     */
    @Log(title = "线路和规则关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizCableRule bizCableRule)
    {
        List<BizCableRule> list = bizCableRuleService.selectBizCableRuleList(bizCableRule);
        ExcelUtil<BizCableRule> util = new ExcelUtil<BizCableRule>(BizCableRule.class);
        util.exportExcel(response, list, "线路和规则关联数据");
    }

    /**
     * 获取线路和规则关联详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(bizCableRuleService.selectBizCableRuleById(id));
    }

    /**
     * 新增线路和规则关联
     */
    @Log(title = "线路和规则关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizCableRule bizCableRule)
    {
        return toAjax(bizCableRuleService.insertBizCableRule(bizCableRule));
    }

    /**
     * 修改线路和规则关联
     */
    @Log(title = "线路和规则关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizCableRule bizCableRule)
    {
        return toAjax(bizCableRuleService.updateBizCableRule(bizCableRule));
    }

    /**
     * 删除线路和规则关联
     */
    @Log(title = "线路和规则关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bizCableRuleService.deleteBizCableRuleByIds(ids));
    }

    @PostMapping("/getDetailByCableId")
    public AjaxResult getDetailByCableId(Long cableId) {
        List<BizCableRule> cameraRuleList = this.bizCableRuleService.getDetailByCameraId(cableId);
        return AjaxResult.success(cameraRuleList);
    }

    @Log(title = "线路和规则关联", businessType = BusinessType.UPDATE)
    @PostMapping("/saveCableRules")
    public AjaxResult saveCableRules(Long cableId, @RequestBody List<BizCableRule> cableRuleVoList) {
        if (cableId == null) {
            return AjaxResult.error("need cableId");
        }
        this.bizCableRuleService.saveCableRules(cableId, cableRuleVoList);
        return AjaxResult.success();
    }
}
