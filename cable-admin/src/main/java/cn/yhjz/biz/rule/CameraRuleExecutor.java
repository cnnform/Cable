package cn.yhjz.biz.rule;

import cn.yhjz.biz.actionexec.ActionExecuter;
import cn.yhjz.biz.actionexec.AlertRuleEntity;
import cn.yhjz.biz.actionexec.ExecAction;
import cn.yhjz.biz.actionexec.ExecActionAttr;
import cn.yhjz.biz.domain.*;
import cn.yhjz.biz.service.*;
import cn.yhjz.biz.utils.MongodbService;
import cn.yhjz.common.core.domain.entity.SysUser;
import cn.yhjz.urule.UruleSelfServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.kie.api.KieBase;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class CameraRuleExecutor {
    @Autowired
    private IBizCameraService bizCameraService;

    @Autowired
    private IBizCameraRuleService bizCameraRuleService;
    @Autowired
    private IRuleMainService ruleMainService;

    @Autowired
    private IAlertLevelService levelService;

    @Autowired
    private IBizCameraRuleOptionService optionService;

    @Autowired
    private IAlertLevelAndActionService levelAndActionService;

    @Autowired
    private IAlertActionAttrService attrService;

    @Autowired
    private IAlertActionService actionService;
    @Autowired
    private ActionExecuter actionExecuter;
    @Autowired
    private IBizAlarmService alarmService;

    @Autowired
    private IBizCableRuleService cableRuleService;

    @Autowired
    private IBizCableService cableService;

    @Autowired
    private UruleSelfServiceImpl uruleSelfService;
    @Autowired
    private MongodbService mongodbService;

    public void execute(MqttAlertRuleEntity mqttAlertRuleEntity) {
        String deviceId = mqttAlertRuleEntity.getDeviceId();
        BizCamera bizCamera = bizCameraService.selectBizCameraByDeviceId(deviceId);
        BizCameraRule bizCameraRule = new BizCameraRule();
        bizCameraRule.setCameraId(bizCamera.getId());

        String base64 = mqttAlertRuleEntity.getImgBase64();

        //执行规则
        List<BizCameraRule> cameraRuleList = bizCameraRuleService.selectBizCameraRuleList(bizCameraRule);
        if (CollectionUtils.isEmpty(cameraRuleList)) {
            //如果摄像头没有配置规则，就去摄像头的线路上找
            Long cableId = bizCamera.getCableId();
            List<BizCableRule> bizCableRuleList = cableRuleService.selectBizCableRuleByCableId(cableId);
            cameraRuleList = new ArrayList<>();
            for (BizCableRule cableRule : bizCableRuleList) {
                BizCameraRule tempRule = new BizCameraRule();
                BeanUtils.copyProperties(cableRule, tempRule);
                cameraRuleList.add(tempRule);
            }
        }
        for (BizCameraRule cameraRule : cameraRuleList) {
            AlertRuleEntity ruleEntity = new AlertRuleEntity();
            ruleEntity.setTargetType(mqttAlertRuleEntity.getTargetType());
            ruleEntity.setDistance(mqttAlertRuleEntity.getDistance());
            ruleEntity.setBehavior(mqttAlertRuleEntity.getBehavior());
            ruleEntity.setCurrentTime(mqttAlertRuleEntity.getCurrentTime());
//            this.doOneRule(bizCamera, cameraRule, ruleEntity);
            Boolean res = this.doOneRuleUrule(bizCamera, cameraRule, ruleEntity);
            if (res != null && res) {
                //保存报警消息
                BizAlarm bizAlarm = new BizAlarm();
                bizAlarm.setDeviceId(deviceId);
                bizAlarm.setCameraId(bizCamera.getId());
                bizAlarm.setTimeStamp(mqttAlertRuleEntity.getCurrentTime());
                bizAlarm.setLon(mqttAlertRuleEntity.getLon());
                bizAlarm.setLat(mqttAlertRuleEntity.getLat());
                bizAlarm.setEventTarget(mqttAlertRuleEntity.getTargetType());
                bizAlarm.setEventType("enter");
                bizAlarm.setAlertLevelName(cameraRule.getLevelName());
                byte[] fileContent = Base64.getDecoder().decode(base64);
                String fileId = mongodbService.saveFile(fileContent);
                bizAlarm.setImgBase64(fileId);
                bizAlarm.setAlertDesc(String.format("%s", cameraRule.getRuleCode()));
                alarmService.insertBizAlarm(bizAlarm);
            }
        }
    }

    /**
     * 处理一个摄像头的一个规则
     */
    private void doOneRule(BizCamera camera, BizCameraRule cameraRule, AlertRuleEntity ruleEntity) {
        Long ruleId = cameraRule.getRuleId();
        Long levelId = cameraRule.getLevelId();
        Long refId = cameraRule.getId();
        //查询出规则
        RuleMain ruleMain = ruleMainService.selectRuleMainById(ruleId);
        //查询出等级
        AlertLevel alertLevel = levelService.selectAlertLevelById(levelId);
        //查询出配置项的值
        BizCameraRuleOption optionParam = new BizCameraRuleOption();
        optionParam.setRefId(refId);
        List<BizCameraRuleOption> optionList =
                optionService.selectBizCameraRuleOptionList(optionParam);
        //运行规则引擎的代码
        String ruleContent = ruleMain.getRuleContent();
        Boolean checkResult = this.checkRule(ruleContent, ruleEntity);
        if (checkResult) {
            log.info("符合规则");
            //触发报警动作
            //根据报警等级获得所有的报警动作
            AlertLevelAndAction levelAndActionParam = new AlertLevelAndAction();
            levelAndActionParam.setLevelId(alertLevel.getId());
            //保存一条报警
            BizAlarm alarm = new BizAlarm();
            alarm.setImgBase64("c9e2bd2575cc4410f999a74578c70670");
            alarm.setCameraId(camera.getId());
            alarm.setEventType("rule");
            alarm.setDeviceId(camera.getDeviceId());
            alarm.setStatus(1L);
            alarm.setTimeStamp(new Date());
            alarm.setAlertDesc(String.format("摄像头%s发生了报警：%s", camera.getDeviceId(), ruleMain.getRuleName()));
            alarmService.insertBizAlarm(alarm);
            List<AlertLevelAndAction> levelAndActionList =
                    levelAndActionService.selectAlertLevelAndActionList(levelAndActionParam);
            for (AlertLevelAndAction levelAndAction : levelAndActionList) {
                this.doAction(ruleMain, camera, levelAndAction, optionList);
            }
        } else {
            log.info("不符合规则");
        }
    }

    /**
     * 使用urule引擎执行规则
     * 如果摄像头没有配置规则，就去找摄像头的线路
     *
     * @param camera
     * @param cameraRule
     * @param ruleEntity
     */
    private Boolean doOneRuleUrule(BizCamera camera, BizCameraRule cameraRule, AlertRuleEntity ruleEntity) {
        Long ruleId = cameraRule.getRuleId();
        Long levelId = cameraRule.getLevelId();
        Long refId = cameraRule.getId();

        //查询出等级
        AlertLevel alertLevel = levelService.selectAlertLevelById(levelId);
        cameraRule.setLevelName(alertLevel.getLevelName());
        String ruleCode = cameraRule.getRuleCode();
        Boolean res = uruleSelfService.check(ruleCode, ruleEntity);
        RuleMain ruleMain = new RuleMain();
        ruleMain.setRuleName(ruleCode);
        if (res != null && res) {
            //根据报警等级获得所有的报警动作
            AlertLevelAndAction levelAndActionParam = new AlertLevelAndAction();
            levelAndActionParam.setLevelId(alertLevel.getId());
            List<AlertLevelAndAction> levelAndActionList =
                    levelAndActionService.selectAlertLevelAndActionList(levelAndActionParam);
            for (AlertLevelAndAction levelAndAction : levelAndActionList) {
                this.doActionUrule(ruleMain, camera, levelAndAction, ruleEntity.getCurrentTime());
            }
        }
        return res;
    }

    private void doAction(RuleMain ruleMain, BizCamera camera, AlertLevelAndAction levelAndAction, List<BizCameraRuleOption> optionList) {
        Long actionId = levelAndAction.getActionId();
        //获得报警动作详情
        AlertAction alertAction = actionService.selectAlertActionById(actionId);
        log.info("触发动作：{}", alertAction.getActionName());
        AlertActionAttr attrParam = new AlertActionAttr();
        attrParam.setActionId(alertAction.getId());

        List<AlertActionAttr> attrList =
                attrService.selectAlertActionAttrList(attrParam);
        //根据每个attr的code，获得它的值
        ExecAction execAction = new ExecAction();
        execAction.setRule(ruleMain);
        execAction.setCamera(camera);
        execAction.setActionCode(alertAction.getActionCode());
        execAction.setActionName(alertAction.getActionName());
        List<ExecActionAttr> execActionAttrList = new ArrayList<>();
        execAction.setAttrList(execActionAttrList);
        for (AlertActionAttr attr : attrList) {
            ExecActionAttr execActionAttr = new ExecActionAttr();
            execActionAttr.setAttrCode(attr.getAttrCode());
            for (BizCameraRuleOption option : optionList) {
                if (attr.getAttrCode().equals(option.getAttrCode())) {
                    String value = option.getAttrValue();
                    execActionAttr.setAttrValue(value);
                    break;
                }
            }
            execActionAttrList.add(execActionAttr);
        }
        actionExecuter.dealAction(execAction);
    }

    /**
     * 触发报警动作，发短息、发邮件……
     *
     * @param ruleMain
     * @param camera
     * @param levelAndAction
     */
    private void doActionUrule(RuleMain ruleMain, BizCamera camera, AlertLevelAndAction levelAndAction, Date happenTime) {
        //如果要发邮件或发短息，短息号码或邮箱地址从摄像头和线路关联的负责人处取得
        Long actionId = levelAndAction.getActionId();
        //获得报警动作详情
        AlertAction alertAction = actionService.selectAlertActionById(actionId);
        ExecAction execAction = new ExecAction();
        execAction.setRule(ruleMain);
        execAction.setCamera(camera);
        execAction.setActionCode(alertAction.getActionCode());
        execAction.setActionName(alertAction.getActionName());

        List<ExecActionAttr> attrList = new ArrayList<>();
        //把电话号码或者邮箱放到attrList中
        //查找该线路的负责人
        SysUser chargeMan = cableService.selectInchargeByCableId(camera.getCableId());
        if (chargeMan == null) {
            log.error("事件发生，但没找到线路负责人");
            return;
        }
        ExecActionAttr attrPhone = new ExecActionAttr();
        attrPhone.setAttrCode("phone_number");
        attrPhone.setAttrValue(chargeMan.getPhonenumber());
        ExecActionAttr attrEmail = new ExecActionAttr();
        attrEmail.setAttrCode("email_address");
        attrEmail.setAttrValue(chargeMan.getEmail());
        attrList.add(attrPhone);
        attrList.add(attrEmail);
        execAction.setAttrList(attrList);
        actionExecuter.dealActionUrule(execAction, happenTime);
    }

    private Boolean checkRule(String ruleContent, AlertRuleEntity ruleEntity) {
        Resource resource = ResourceFactory.newByteArrayResource(ruleContent.getBytes());
//        log.info(ruleContent);
        KieHelper helper = new KieHelper();
        helper.addResource(resource, ResourceType.DRL);
        KieBase kBase = helper.build();
        KieSession simpleRuleKSession = kBase.newKieSession();

        //第四步 插入事实对象到session中
        simpleRuleKSession.insert(ruleEntity);
        //第五步 执行规则引擎
        simpleRuleKSession.fireAllRules();

        //第六步 关闭规则引擎
        simpleRuleKSession.dispose();
        return ruleEntity.getResult();

    }
}
