package cn.yhjz.common.utils;

import com.github.pagehelper.PageHelper;
import cn.yhjz.common.core.page.PageDomain;
import cn.yhjz.common.core.page.TableSupport;
import cn.yhjz.common.utils.sql.SqlUtil;

/**
 * 分页工具类
 * 
 * @author yhjz
 */
public class PageUtils extends PageHelper
{
    /**
     * 设置请求分页数据
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (null==pageSize) {
            pageSize = 10;
        }
        if (null==pageNum) {
            pageSize = 1;
        }
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }
}
