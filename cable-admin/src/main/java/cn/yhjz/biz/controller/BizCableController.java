package cn.yhjz.biz.controller;

import java.util.List;

import cn.yhjz.biz.vo.BizCableVo;
import cn.yhjz.common.core.domain.entity.SysUser;
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
import cn.yhjz.biz.domain.BizCable;
import cn.yhjz.biz.service.IBizCableService;

/**
 * 线路Controller
 *
 * @author yhjz
 * @date 2022-09-01
 */
@RestController
@RequestMapping("/biz/cable")
public class BizCableController extends BaseController {
    @Autowired
    private IBizCableService bizCableService;

    /**
     * 查询线路列表
     */
    @PreAuthorize("@ss.hasPermi('biz:cable:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizCable bizCable) {
        startPage();
        List<BizCableVo> list = bizCableService.selectBizCableList(bizCable);
        return getDataTable(list);
    }
    /**
     * 查询线路列表，包含上面的定位设备
     */
    @PreAuthorize("@ss.hasPermi('biz:cable:list')")
    @GetMapping("/listWithDevice")
    public TableDataInfo listWithDevice(BizCable bizCable) {
        startPage();
        List<BizCableVo> list = bizCableService.listWithDevice(bizCable);
        return getDataTable(list);
    }
    @GetMapping("/selectChargeByClass")
    public AjaxResult selectChargeByClass(Long classId) {
        List<SysUser> list = bizCableService.selectChargeByClass(classId);

        return AjaxResult.success(list);
    }


    /**
     * 获取线路详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:cable:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(bizCableService.selectBizCableById(id));
    }

    /**
     * 新增线路
     */
    @PreAuthorize("@ss.hasPermi('biz:cable:add')")
    @Log(title = "线路", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizCable bizCable) {
        return toAjax(bizCableService.insertBizCable(bizCable));
    }

    /**
     * 修改线路
     */
    @PreAuthorize("@ss.hasPermi('biz:cable:edit')")
    @Log(title = "线路", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizCable bizCable) {
        return toAjax(bizCableService.updateBizCable(bizCable));
    }

    /**
     * 删除线路
     */
    @PreAuthorize("@ss.hasPermi('biz:cable:remove')")
    @Log(title = "线路", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bizCableService.deleteBizCableByIds(ids));
    }
}
