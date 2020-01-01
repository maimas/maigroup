package com.mg.drools.businessrules.services;


import com.mg.drools.businessrules.base.BusinessRuleGlobals;
import com.mg.drools.businessrules.base.RuleHeaders;
import com.mg.drools.businessrules.exceptions.BusinessRulesException;
import com.mg.persistence.domain.bizitem.model.BizItemModel;
import com.mg.persistence.domain.bizitem.service.BizItemConverterService;
import lombok.extern.log4j.Log4j2;
import org.drools.core.event.DefaultAgendaEventListener;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Log4j2
@Service
public class BusinessRulesService {

    private BizItemConverterService bizItemConverterService;

    @Autowired
    public BusinessRulesService(BizItemConverterService bizItemConverterService) {
        this.bizItemConverterService = bizItemConverterService;
    }

    /**
     * Executed business rules on the model
     *
     * @param model - business item
     * @return - result of the rues execution
     * @throws BusinessRulesException - if rules could not be loaded or executed
     */
    public BizItemModel execute(BizItemModel model, HashMap<String, Object> ruleGlobals, RuleHeaders ruleHeaders) throws BusinessRulesException {

        if (ruleGlobals == null) {
            ruleGlobals = new HashMap<>();
        }
        if (ruleHeaders == null) {
            ruleHeaders = new RuleHeaders();
        }

        model = bizItemConverterService.castContent(model);

        ruleGlobals.put(BusinessRuleGlobals.RuleHeaders, ruleHeaders);
        ruleGlobals.put(BusinessRuleGlobals.Log, log);
//        ruleGlobals.set(BusinessRuleGlobals.BusinessRulesService, this);

        DroolsRunner droolsRunner = new DroolsRunner(model.getItemType());
        droolsRunner.run(model, ruleGlobals);

        if (ruleHeaders.hasErrors()) {
            throw new BusinessRulesException(ruleHeaders.getErrorsAsString());
        }

        return model;
    }

//
//    public void clearCache() {
//        String msg = "Removing Business Rules cache";
//        log.info(String.format(MSG_TASK_START, msg));
//        BusinessRulesCache.getInstance().clear();
//        log.info(MSG_TASK_END_OK);
//    }
//


    private void addAfterMatchEventListener(KieSession kSession) {
        kSession.addEventListener(new DefaultAgendaEventListener() {
            public void afterMatchFired(AfterMatchFiredEvent event) {
                super.afterMatchFired(event);
                log.info(event);
            }
        });
    }
}
