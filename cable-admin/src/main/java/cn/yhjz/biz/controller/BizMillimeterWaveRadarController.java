package cn.yhjz.biz.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.yhjz.biz.domain.BizMillimeterWaveRadar;
import cn.yhjz.biz.service.IBizMillimeterWaveRadarService;
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

import cn.yhjz.common.utils.poi.ExcelUtil;

/**
 * 毫米波雷达管理Controller
 * 
 * @author yhjz
 * @date 2022-11-06
 */
@RestController
@RequestMapping("/system/radar")
public class BizMillimeterWaveRadarController extends BaseController
{
    @Autowired
    private IBizMillimeterWaveRadarService bizMillimeterWaveRadarService;

    /**
     * 查询毫米波雷达管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:radar:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizMillimeterWaveRadar bizMillimeterWaveRadar)
    {
        startPage();
        List<BizMillimeterWaveRadar> list = bizMillimeterWaveRadarService.selectBizMillimeterWaveRadarList(bizMillimeterWaveRadar);
        return getDataTable(list);
    }

    /**
     * 导出毫米波雷达管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:radar:export')")
    @Log(title = "毫米波雷达管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizMillimeterWaveRadar bizMillimeterWaveRadar)
    {
        List<BizMillimeterWaveRadar> list = bizMillimeterWaveRadarService.selectBizMillimeterWaveRadarList(bizMillimeterWaveRadar);
        ExcelUtil<BizMillimeterWaveRadar> util = new ExcelUtil<BizMillimeterWaveRadar>(BizMillimeterWaveRadar.class);
        util.exportExcel(response, list, "毫米波雷达管理数据");
    }

    /**
     * 获取毫米波雷达管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:radar:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(bizMillimeterWaveRadarService.selectBizMillimeterWaveRadarById(id));
    }

    /**
     * 新增毫米波雷达管理
     */
    @PreAuthorize("@ss.hasPermi('system:radar:add')")
    @Log(title = "毫米波雷达管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizMillimeterWaveRadar bizMillimeterWaveRadar)
    {
        return toAjax(bizMillimeterWaveRadarService.insertBizMillimeterWaveRadar(bizMillimeterWaveRadar));
    }

    /**
     * 修改毫米波雷达管理
     */
    @PreAuthorize("@ss.hasPermi('system:radar:edit')")
    @Log(title = "毫米波雷达管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizMillimeterWaveRadar bizMillimeterWaveRadar)
    {
        return toAjax(bizMillimeterWaveRadarService.updateBizMillimeterWaveRadar(bizMillimeterWaveRadar));
    }

    /**
     * 删除毫米波雷达管理
     */
    @PreAuthorize("@ss.hasPermi('system:radar:remove')")
    @Log(title = "毫米波雷达管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bizMillimeterWaveRadarService.deleteBizMillimeterWaveRadarByIds(ids));
    }
}
