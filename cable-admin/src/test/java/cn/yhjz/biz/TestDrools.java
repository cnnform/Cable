package cn.yhjz.biz;

import cn.yhjz.biz.actionexec.AlertRuleEntity;
import org.junit.jupiter.api.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;

import java.util.Date;

public class TestDrools {
    //消费100元以下 不加分

    //消费100元-500元 加100分

    //消费500元-1000元 加500分

    //消费1000元以上 加1000分

    @Test
    public void test() {
        //第一步 获取KieServices
        KieServices kieServices = KieServices.Factory.get();
        //第二步获取kieContainer
        KieContainer kieClasspathContainer = kieServices.getKieClasspathContainer("SimpleRuleKBase");
        //第三步获取kieSession
        KieSession simpleRuleKSession = kieClasspathContainer.newKieSession("simpleRuleKSession");
        //新建事实对象
        Order order = new Order();
        order.setScore(100);
        order.setAmount(5000);
        //第四步 插入事实对象到session中
        simpleRuleKSession.insert(order);
        //第五步 执行规则引擎
        simpleRuleKSession.fireAllRules();

        //第六步 关闭规则引擎
        simpleRuleKSession.dispose();
        System.out.println(order.getScore());
        System.out.println("规则执行完成，关闭规则");
    }

    @Test
    public void test2() {
        String resourceStr = "package rules;\n" +
                "\n" +
                "import cn.yhjz.biz.Order;\n" +
                "\n" +
                "//100元以下不加分\n" +
                "rule \"score_1\" //名称需唯一\n" +
                "when\n" +
                "$order:Order(amount < 100);\n" +
                "then\n" +
                "$order.setScore($order.getScore()+0);\n" +
                "System.out.println(\"触发规则，100元以下不加分\");\n" +
                "end\n" +
                "\n" +
                "//100元-500元 加100分\n" +
                "rule \"score_2\"\n" +
                "when\n" +
                "$order:Order(100 < amount && amount< 500)\n" +
                "then\n" +
                "$order.setScore($order.getScore()+100);\n" +
                "System.out.println(\"触发规则，100元-500元 加100分\");\n" +
                "end\n" +
                "//500元-1000元 加500分\n" +
                "rule \"score_3\"\n" +
                "when\n" +
                "$order:Order(500 < amount && amount < 1000)\n" +
                "then\n" +
                "$order.setScore($order.getScore()+500);\n" +
                "System.out.println(\"触发规则，500元-1000元 加500分\");\n" +
                "end\n" +
                "//1000元以上 加1000分\n" +
                "rule \"score_4\"\n" +
                "when\n" +
                "$order:Order(1000 < amount)\n" +
                "then\n" +
                "$order.setScore($order.getScore()+600);\n" +
                "System.out.println(\"触发规则，500元以上， 加600分\");\n" +
                "end";
        Resource resource = ResourceFactory.newByteArrayResource(resourceStr.getBytes());
        KieHelper helper = new KieHelper();
        helper.addResource(resource, ResourceType.DRL);
        KieBase kBase = helper.build();
        KieSession simpleRuleKSession = kBase.newKieSession();
        //新建事实对象
        Order order = new Order();
        order.setScore(100);
        order.setAmount(5000);
        //第四步 插入事实对象到session中
        simpleRuleKSession.insert(order);
        //第五步 执行规则引擎
        simpleRuleKSession.fireAllRules();

        //第六步 关闭规则引擎
        simpleRuleKSession.dispose();
        System.out.println(order.getScore());
        System.out.println("规则执行完成，关闭规则");
    }

    @Test
    public void testAlert() {
        long startTime = new Date().getTime();
        String resourceStr = "package rules;\n" +
                "\n" +
                "import cn.yhjz.biz.domain.AlertRuleEntity;\n" +
                "\n" +
                "\n" +
                "//5m之内报警\n" +
                "rule \"distance\" //名称需唯一\n" +
                "when\n" +
                "$entity:AlertRuleEntity(distance <  5.0 && targetType==\"wjj\" && behavior==\"dig\");\n" +
                "then\n" +
                "$entity.setResult(true);\n" +
                "end";
        Resource resource = ResourceFactory.newByteArrayResource(resourceStr.getBytes());
        KieHelper helper = new KieHelper();
        helper.addResource(resource, ResourceType.DRL);
        KieBase kBase = helper.build();
        KieSession simpleRuleKSession = kBase.newKieSession();
        //新建事实对象
        AlertRuleEntity entity = new AlertRuleEntity();
        entity.setDistance(3.0);
        entity.setTargetType("wjj");
        entity.setBehavior("dig");
        //第四步 插入事实对象到session中
        simpleRuleKSession.insert(entity);
        //第五步 执行规则引擎
        simpleRuleKSession.fireAllRules();

        //第六步 关闭规则引擎
        simpleRuleKSession.dispose();
        long endTime = new Date().getTime();
        System.out.println(endTime - startTime + "--" + entity.getResult());
        System.out.println("规则执行完成，关闭规则");
    }
}
