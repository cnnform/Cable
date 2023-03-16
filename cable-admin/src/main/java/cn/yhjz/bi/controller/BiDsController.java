package cn.yhjz.bi.controller;

import java.sql.*;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

import cn.yhjz.bi.utils.DataBaseUtil;
import cn.yhjz.bi.vo.DsBaseVo;
import cn.yhjz.common.annotation.Log;
import cn.yhjz.common.core.controller.BaseController;
import cn.yhjz.common.core.domain.AjaxResult;
import cn.yhjz.common.core.page.TableDataInfo;
import cn.yhjz.common.enums.BusinessType;
import cn.yhjz.common.utils.StringUtils;
import cn.yhjz.common.utils.poi.ExcelUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.yhjz.bi.domain.BiDs;
import cn.yhjz.bi.service.IBiDsService;

/**
 * 数据源管理
 * 
 * @author ldl
 * @date 2022-01-04
 */
@RestController
@RequestMapping("bi/ds")
public class BiDsController extends BaseController {

    @Autowired
    private IBiDsService biDsService;

    /**
     * 运行SQL
     *
     * @param param > 数据集合
     */
    @PreAuthorize("@ss.hasPermi('bi:ds:runSQL')")
    @RequestMapping("runSQL")
    public AjaxResult runSql(@RequestBody Map<String,String> param){
        Statement statement = null;
        ResultSet rs = null;
        BiDs biDs = biDsService.selectBiDsByDsId(Long.valueOf(String.valueOf(param.get("dsId"))));
        DsBaseVo dsBase = DsBaseVo.dsBaseInit().get(biDs.getDsType());
        if (dsBase != null) {
            String Url = dsBase.getUrl().replace("ip", biDs.getDsIp()).replace("port", biDs.getDsPort()).replace("dbname", biDs.getDsDbName());
            Connection connection = DataBaseUtil.connectDb(dsBase.getDriver(), Url, biDs.getDsUsername(), biDs.getDsPassword());
            if (connection == null) {
                return AjaxResult.error("连接失败!");
            }
            try {
                statement = connection.createStatement();
                rs = statement.executeQuery(String.valueOf(param.get("sql")));
                ResultSetMetaData md = rs.getMetaData();
                List mapList = new ArrayList<>();
                while(rs.next()) {
                    Map map = new HashMap();
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        map.put(md.getColumnName(i), rs.getObject(i));
                    }
                    mapList.add(map);
                }
                return AjaxResult.success(mapList);
            } catch (SQLException e) {
                return AjaxResult.error("SQL执行失败, 错误信息: " + e.getMessage());
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                    if (statement != null) {
                        statement.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException e) {
                    logger.error("关闭SQL连接错误!");
                }
            }
        }
        return AjaxResult.error("暂不支持该数据库类型!");
    }

    /**
     * 测试连接
     *
     * @param biDs > 数据集合
     */
    @PreAuthorize("@ss.hasPermi('bi:ds:linkTest')")
    @RequestMapping("/linkTest")
    private AjaxResult linkTest(@RequestBody BiDs biDs) {
        if (StringUtils.isEmpty(biDs.getDsType())){
            return AjaxResult.error("错误消息: 类型错误!");
        }
        DsBaseVo dsBase = DsBaseVo.dsBaseInit().get(biDs.getDsType());
        if (dsBase != null) {
            try {
                Class.forName(dsBase.getDriver());
                String Url = dsBase.getUrl().replace("ip", biDs.getDsIp()).replace("port", biDs.getDsPort()).replace("dbname", biDs.getDsDbName());
                DriverManager.getConnection(Url, biDs.getDsUsername(), biDs.getDsPassword());
            } catch (ClassNotFoundException | SQLException e) {
                return AjaxResult.error("驱动连接失败! 错误消息: " + e.getMessage());
            }
            return AjaxResult.success("连接成功!");
        }
        return AjaxResult.error("暂不支持该数据库类型!");
    }

    /**
     * 获取支持的数据库
     */
    @GetMapping("dsBaseInit")
    public AjaxResult dsBaseInit() {
        List<String> Dslist = new ArrayList<>();
        for (String ds: DsBaseVo.dsBaseInit().keySet()) {
            Dslist.add(ds);
        }
        return AjaxResult.success(Dslist);
    }

    /**
     * 查询数据源管理列表
     */
    @PreAuthorize("@ss.hasPermi('bi:ds:list')")
    @GetMapping("/list")
    public TableDataInfo list(BiDs biDs)
    {
        startPage();
        List<BiDs> list = biDsService.selectBiDsList(biDs);
        return getDataTable(list);
    }

    /**
     * 导出数据源管理列表
     */
    @PreAuthorize("@ss.hasPermi('bi:ds:export')")
    @Log(title = "数据源管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BiDs biDs)
    {
        List<BiDs> list = biDsService.selectBiDsList(biDs);
        ExcelUtil<BiDs> util = new ExcelUtil<BiDs>(BiDs.class);
        util.exportExcel(response, list, "数据源管理数据");
    }

    /**
     * 获取数据源管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('bi:ds:query')")
    @GetMapping(value = "/{dsId}")
    public AjaxResult getInfo(@PathVariable("dsId") Long dsId)
    {
        return AjaxResult.success(biDsService.selectBiDsByDsId(dsId));
    }

    /**
     * 新增数据源管理
     */
    @PreAuthorize("@ss.hasPermi('bi:ds:add')")
    @Log(title = "数据源管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BiDs biDs)
    {
        return toAjax(biDsService.insertBiDs(biDs));
    }

    /**
     * 修改数据源管理
     */
    @PreAuthorize("@ss.hasPermi('bi:ds:edit')")
    @Log(title = "数据源管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BiDs biDs)
    {
        return toAjax(biDsService.updateBiDs(biDs));
    }

    /**
     * 删除数据源管理
     */
    @PreAuthorize("@ss.hasPermi('bi:ds:remove')")
    @Log(title = "数据源管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{dsIds}")
    public AjaxResult remove(@PathVariable Long[] dsIds)
    {
        return toAjax(biDsService.deleteBiDsByDsIds(dsIds));
    }
}
