package cn.yhjz.biz.vo;

import cn.yhjz.biz.domain.BizCameraRule;
import cn.yhjz.biz.domain.BizCameraRuleOption;
import lombok.Data;

import java.util.List;

@Data
public class CameraRuleVo extends BizCameraRule {

    private List<AlertActionVo> actionList;

    private List<BizCameraRuleOption> optionList;
}
