package cn.yhjz.biz.vo;

import cn.yhjz.biz.domain.AlertAction;
import lombok.Data;

import java.util.List;

/**
 * 报警动作的封装对象，其中包含选项配置
 */
@Data
public class AlertActionVo extends AlertAction {
    private List<AlertActionAttrVo> attrs;
}
