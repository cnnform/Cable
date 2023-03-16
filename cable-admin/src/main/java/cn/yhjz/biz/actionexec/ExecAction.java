package cn.yhjz.biz.actionexec;

import cn.yhjz.biz.domain.BizCamera;
import cn.yhjz.biz.domain.RuleMain;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@Data
public class ExecAction {

    private String actionCode;
    private String actionName;

    private BizCamera camera;
    private RuleMain rule;
    private List<ExecActionAttr> attrList;


    public String getAttrValue(String attrCode) {
        if (CollectionUtils.isNotEmpty(this.attrList)) {
            for (ExecActionAttr attr : attrList) {
                if (attrCode.equals(attr.getAttrCode())) {
                    return attr.getAttrValue();
                }
            }
        }
        return null;
    }
}
