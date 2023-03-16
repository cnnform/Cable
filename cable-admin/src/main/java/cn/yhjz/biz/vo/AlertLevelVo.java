package cn.yhjz.biz.vo;

import cn.yhjz.biz.domain.AlertAction;
import cn.yhjz.biz.domain.AlertLevel;
import lombok.Data;

import java.util.List;

/**
 * 报警等级的封装对象，里面包含报警动作和报警动作的配置项
 */
@Data
public class AlertLevelVo extends AlertLevel {

    private List<AlertActionVo> actionList;
}
