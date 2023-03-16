package cn.yhjz.bi.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.yhjz.common.annotation.Log;
import cn.yhjz.common.config.YHJZConfig;
import cn.yhjz.common.core.controller.BaseController;
import cn.yhjz.common.core.domain.AjaxResult;
import cn.yhjz.common.core.page.TableDataInfo;
import cn.yhjz.common.enums.BusinessType;
import cn.yhjz.common.utils.file.FileUploadUtils;
import cn.yhjz.common.utils.poi.ExcelUtil;
import cn.yhjz.common.utils.uuid.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.yhjz.bi.domain.BiPainterManager;
import cn.yhjz.bi.service.IBiPainterManagerService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 看板管理
 *
 * @author ldl
 * @date 2022-01-12
 */
@RestController
@RequestMapping("/bi/painterManager")
public class BiPainterManagerController extends BaseController
{
    @Autowired
    private IBiPainterManagerService biPainterManagerService;

    /**
     * 查询看板管理列表
     */
    @PreAuthorize("@ss.hasPermi('bi:painter:list')")
    @GetMapping("/list")
    public TableDataInfo list(BiPainterManager biPainterManager)
    {
        startPage();
        List<BiPainterManager> list = biPainterManagerService.selectBiPainterManagerList(biPainterManager);
        return getDataTable(list);
    }

    /**
     * 导出看板管理列表
     */
    @PreAuthorize("@ss.hasPermi('bi:painter:export')")
    @Log(title = "看板管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BiPainterManager biPainterManager)
    {
        List<BiPainterManager> list = biPainterManagerService.selectBiPainterManagerList(biPainterManager);
        ExcelUtil<BiPainterManager> util = new ExcelUtil<BiPainterManager>(BiPainterManager.class);
        util.exportExcel(response, list, "看板管理数据");
    }

    /**
     * 获取看板管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('bi:painter:query')")
    @GetMapping(value = "/{pmId}")
    public AjaxResult getInfo(@PathVariable("pmId") Long pmId)
    {
        return AjaxResult.success(biPainterManagerService.selectBiPainterManagerByPmId(pmId));
    }

    /**
     * 新增看板管理
     */
    @PreAuthorize("@ss.hasPermi('bi:painter:add')")
    @Log(title = "看板管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BiPainterManager biPainterManager)
    {
        if (biPainterManager.getPmId() == null) {
            biPainterManager.setPmCode(UUID.fastUUID().toString(true));
            biPainterManager.setCreateUser(getUsername());
            return toAjax(biPainterManagerService.insertBiPainterManager(biPainterManager));
        }else{
            biPainterManager.setUpdateUser(getUsername());
            return toAjax(biPainterManagerService.updateBiPainterManager(biPainterManager));
        }

    }


    /**
     * 新增看板管理-返回对象
     */
    @PreAuthorize("@ss.hasPermi('bi:painter:add')")
    @Log(title = "看板管理", businessType = BusinessType.INSERT)
    @PostMapping("/addReObj")
    public AjaxResult addReObj(@RequestBody BiPainterManager biPainterManager)
    {
        biPainterManager.setPmCode(UUID.fastUUID().toString(true));
        biPainterManager.setCreateUser(getUsername());
        biPainterManager=biPainterManagerService.insertBiPm(biPainterManager);
        AjaxResult result=null;
        if(biPainterManager !=null && biPainterManager.getPmId()!=null){
            result=AjaxResult.success(biPainterManager);
        }else{
            result=AjaxResult.error();
        }
        return result;
    }
    /**
     * 修改看板管理
     */
    @PreAuthorize("@ss.hasPermi('bi:painter:edit')")
    @Log(title = "看板管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BiPainterManager biPainterManager)
    {
        biPainterManager.setUpdateUser(getUsername());
        return toAjax(biPainterManagerService.updateBiPainterManager(biPainterManager));
    }

    /**
     * 删除看板管理
     */
    @PreAuthorize("@ss.hasPermi('bi:painter:remove')")
    @Log(title = "看板管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{pmIds}")
    public AjaxResult remove(@PathVariable Long[] pmIds)
    {
        return toAjax(biPainterManagerService.deleteBiPainterManagerByPmIds(pmIds));
    }

    /**
     * 背景图片上传
     */
    @Log(title = "背景图片上传", businessType = BusinessType.UPDATE)
    @PostMapping("/backgroundImage")
    public AjaxResult avatar(@RequestParam("file") MultipartFile file) throws IOException
    {
        if (!file.isEmpty())
        {
            String avatar = FileUploadUtils.upload(YHJZConfig.getBackgroundImagePath(), file);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("imgUrl", avatar);
            return ajax;
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }
}
