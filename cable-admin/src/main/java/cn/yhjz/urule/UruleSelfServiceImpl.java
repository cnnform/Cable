package cn.yhjz.urule;

import cn.yhjz.biz.actionexec.AlertRuleEntity;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rete.RuleData;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import com.bstek.urule.runtime.agenda.AgendaFilter;
import com.bstek.urule.runtime.service.KnowledgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@DependsOn("urule.knowledgeService")
public class UruleSelfServiceImpl {

//    public Rete rete;
//    public KnowledgePackage knowledgePackage;

    @Autowired
    private KnowledgeService knowledgeService;
    @Autowired
    private UruleSelfServiceImpl uruleSelfService;

    public List<RuleData> getRuleList() {
        try {
            KnowledgePackage knowledgePackage = knowledgeService.getKnowledge("线缆监控/cable");
            Rete rete = knowledgePackage.getRete();
            List<RuleData> ruleDataList = rete.getAllRuleData();
            return ruleDataList;
        } catch (IOException ioException) {
            return new ArrayList<>();
        }
    }

    /**
     * 执行一个规则
     * @param ruleName
     * @return 是否符合规则
     */
    public Boolean check(String ruleName, AlertRuleEntity alertRuleEntity) {
        try {
            KnowledgePackage knowledgePackage = knowledgeService.getKnowledge("线缆监控/cable");
            Rete rete = knowledgePackage.getRete();

            KnowledgeSession knowledgeSession = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);
            GeneralEntity generalEntity = new GeneralEntity("cn.yhjz.biz.actionexec.AlertRuleEntity");
            generalEntity.put("targetType", alertRuleEntity.getTargetType());
            generalEntity.put("behavior", alertRuleEntity.getBehavior());
            generalEntity.put("distance", alertRuleEntity.getDistance());
            knowledgeSession.insert(generalEntity);
            AgendaFilter filter = new RuleNameEqualsAgendaFilter(ruleName);
            knowledgeSession.fireRules(filter);
            Boolean result = (Boolean) generalEntity.get("result");
            return result;
        } catch (IOException ioException) {
            log.error("知识包不存在");
            return null;
        }
    }
}
