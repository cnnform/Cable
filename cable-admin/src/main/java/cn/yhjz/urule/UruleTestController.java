package cn.yhjz.urule;

import cn.yhjz.common.core.domain.AjaxResult;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/uruletest")
@Slf4j
public class UruleTestController {

    @Autowired
    private KnowledgeService knowledgeService;

    @RequestMapping("/t")
    public AjaxResult test(String ruleName) throws IOException {
        KnowledgePackage knowledgePackage = knowledgeService.getKnowledge("线缆监控/cable");

//        List<Rete> reteList = knowledgePackage.getAloneRetes();
//        for (Rete rete : reteList) {
//            List<RuleData> ruleDataList = rete.getAllRuleData();
//            for (RuleData ruleData : ruleDataList) {
//                log.info("ruleName:{}", ruleData.getName());
//            }
//        }
        KnowledgeSession knowledgeSession = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);
        GeneralEntity generalEntity = new GeneralEntity("cn.yhjz.biz.actionexec.AlertRuleEntity");
        generalEntity.put("targetType", "wjj");
        generalEntity.put("behavior", "dig");
        generalEntity.put("distance", 1.0);
        knowledgeSession.insert(generalEntity);

        AgendaFilter filter = new RuleNameEqualsAgendaFilter(ruleName);
        knowledgeSession.fireRules(filter);


        String applyTarget = (String) generalEntity.get("applyTarget");
        String applyAction = (String) generalEntity.get("applyAction");
        Boolean result = (Boolean) generalEntity.get("result");
        log.info("applyType:{}  ,   applyAction:{},   result:{}", applyTarget, applyAction,result);
        return AjaxResult.success();
    }
}
