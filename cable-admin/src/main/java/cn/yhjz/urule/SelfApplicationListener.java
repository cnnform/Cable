package cn.yhjz.urule;

import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.service.KnowledgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SelfApplicationListener implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private KnowledgeService knowledgeService;
    @Autowired
    private UruleSelfServiceImpl uruleSelfService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
//            KnowledgePackage knowledgePackage = knowledgeService.getKnowledge("线缆监控/cable");
//            Rete rete = knowledgePackage.getRete();
//            uruleSelfService.rete = rete;
//            uruleSelfService.knowledgePackage = knowledgePackage;
        } catch (Exception exception) {
            log.error("加载知识包失败");
        }
    }
}
