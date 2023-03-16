package cn.yhjz.biz;

import cn.yhjz.biz.rule.RuleItemEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TestRuleItemEnum {


    @Test
    public void t() {
        log.info("{}", RuleItemEnum.time.getCode());
    }
}
