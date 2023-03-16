package cn.yhjz.bi.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.yhjz.common.annotation.Log;
import cn.yhjz.common.core.controller.BaseController;
import cn.yhjz.common.core.domain.AjaxResult;
import cn.yhjz.common.core.page.TableDataInfo;
import cn.yhjz.common.enums.BusinessType;
import cn.yhjz.common.utils.poi.ExcelUtil;
import cn.yhjz.bi.service.IBiSqlService;
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
import cn.yhjz.bi.domain.BiSql;

/**
 * SQL存储
 * 
 * @author ldl
 * @date 2022-01-05
 */
@RestController
@RequestMapping("/bi/sql")
public class BiSqlController extends BaseController
{
    @Autowired
    private IBiSqlService biSqlService;

    /**
     * 查询SQL存储列表
     */
    @PreAuthorize("@ss.hasPermi('bi:sql:list')")
    @GetMapping("/list")
    public TableDataInfo list(BiSql biSql)
    {
        startPage();
        List<BiSql> list = biSqlService.selectBiSqlList(biSql);
        return getDataTable(list);
    }

    /**
     * 导出SQL存储列表
     */
    @PreAuthorize("@ss.hasPermi('bi:sql:export')")
    @Log(title = "SQL存储", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BiSql biSql)
    {
        List<BiSql> list = biSqlService.selectBiSqlList(biSql);
        ExcelUtil<BiSql> util = new ExcelUtil<BiSql>(BiSql.class);
        util.exportExcel(response, list, "SQL存储数据");
    }

    /**
     * 获取SQL存储详细信息
     */
    @PreAuthorize("@ss.hasPermi('bi:sql:query')")
    @GetMapping(value = "/{bsId}")
    public AjaxResult getInfo(@PathVariable("bsId") Long bsId)
    {
        return AjaxResult.success(biSqlService.selectBiSqlByBsId(bsId));
    }

    /**
     * 新增SQL存储
     */
    @PreAuthorize("@ss.hasPermi('bi:sql:add')")
    @Log(title = "SQL存储", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BiSql biSql)
    {
        return toAjax(biSqlService.insertBiSql(biSql));
    }

    /**
     * 修改SQL存储
     */
    @PreAuthorize("@ss.hasPermi('bi:sql:edit')")
    @Log(title = "SQL存储", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BiSql biSql)
    {
        return toAjax(biSqlService.updateBiSql(biSql));
    }

    /**
     * 删除SQL存储
     */
    @PreAuthorize("@ss.hasPermi('bi:sql:remove')")
    @Log(title = "SQL存储", businessType = BusinessType.DELETE)
	@DeleteMapping("/{bsIds}")
    public AjaxResult remove(@PathVariable Long[] bsIds)
    {
        return toAjax(biSqlService.deleteBiSqlByBsIds(bsIds));
    }
}
