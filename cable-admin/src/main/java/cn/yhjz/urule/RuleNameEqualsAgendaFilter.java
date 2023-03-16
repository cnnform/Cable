package cn.yhjz.urule;

import com.bstek.urule.runtime.agenda.Activation;
import com.bstek.urule.runtime.agenda.AgendaFilter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RuleNameEqualsAgendaFilter implements AgendaFilter {
    private final String ruleName;

    public RuleNameEqualsAgendaFilter(final String name) {
        this.ruleName = name;
    }
    public String getName() {
        return ruleName;
    }
    @Override
    public boolean accept(Activation var1) {
        if (var1.getRule().getName().equals(this.ruleName)) {
            return true;
        } else {
            return false;
        }
    }
}
