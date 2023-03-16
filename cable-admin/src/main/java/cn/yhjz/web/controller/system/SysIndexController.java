package cn.yhjz.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.yhjz.common.config.YHJZConfig;
import cn.yhjz.common.utils.StringUtils;

/**
 * 首页
 *
 * @author yhjz
 */
@RestController
public class SysIndexController
{
    /** 系统基础配置 */
    @Autowired
    private YHJZConfig yhjzConfig;

    /**
     * 访问首页，提示语
     */
    @RequestMapping({"","/"})
    public String index()
    {
        return StringUtils.format("欢迎使用{}管理系统,版本号：v{}", yhjzConfig.getName(), yhjzConfig.getVersion());
    }
}
