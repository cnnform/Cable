package cn.yhjz.biz.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.yhjz.biz.domain.BizArea;
import cn.yhjz.biz.service.IBizAreaService;
import cn.yhjz.common.annotation.Log;
import cn.yhjz.common.core.controller.BaseController;
import cn.yhjz.common.core.domain.AjaxResult;
import cn.yhjz.common.core.page.TableDataInfo;
import cn.yhjz.common.enums.BusinessType;
import cn.yhjz.common.utils.StringUtils;
import cn.yhjz.common.utils.poi.ExcelUtil;
import cn.yhjz.nio.camera.CameraMessageHandler;
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

/**
 * 网络区域Controller
 *
 * @author yhjz
 * @date 2022-04-27
 */
@RestController
@RequestMapping("/biz/area")
public class BizAreaController extends BaseController {

    @Autowired
    private IBizAreaService bizAreaService;

    /**
     * 查询网络区域列表
     */
    @PreAuthorize("@ss.hasPermi('biz:area:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizArea bizArea) {
        startPage();
        List<BizArea> list = bizAreaService.selectBizAreaList(bizArea);
        return getDataTable(list);
    }

    /**
     * 导出网络区域列表
     */
    @PreAuthorize("@ss.hasPermi('biz:area:export')")
    @Log(title = "网络区域", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizArea bizArea) {
        List<BizArea> list = bizAreaService.selectBizAreaList(bizArea);
        ExcelUtil<BizArea> util = new ExcelUtil<BizArea>(BizArea.class);
        util.exportExcel(response, list, "网络区域数据");
    }

    /**
     * 获取网络区域详细信息
     */
    @PreAuthorize("@ss.hasPermi('biz:area:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(bizAreaService.selectBizAreaById(id));
    }

    /**
     * 新增网络区域
     */
    @PreAuthorize("@ss.hasPermi('biz:area:add')")
    @Log(title = "网络区域", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizArea bizArea) {
        return toAjax(bizAreaService.insertBizArea(bizArea));
    }

    /**
     * 修改网络区域
     */
    @PreAuthorize("@ss.hasPermi('biz:area:edit')")
    @Log(title = "网络区域", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizArea bizArea) {
        return toAjax(bizAreaService.updateBizArea(bizArea));
    }

    /**
     * 删除网络区域
     */
    @PreAuthorize("@ss.hasPermi('biz:area:remove')")
    @Log(title = "网络区域", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(bizAreaService.deleteBizAreaByIds(ids));
    }

    /**
     * 刷新网络区域内的摄像头连接
     */
    @Log(title = "网络区域", businessType = BusinessType.DELETE)
    @PostMapping("/refresh")
    public AjaxResult refresh(@RequestBody BizArea bizArea) {
        if (StringUtils.isEmpty(bizArea.getAreaCode())) {
            return AjaxResult.error("网络区域为空!");
        }
        CameraMessageHandler cameraAnticipation = new CameraMessageHandler();
        cameraAnticipation.refreshHttp(bizArea.getAreaCode());
        return AjaxResult.success("通知成功!");
    }
}
