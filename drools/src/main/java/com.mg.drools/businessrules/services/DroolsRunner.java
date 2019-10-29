package com.mg.drools.businessrules.services;

import com.mg.drools.businessrules.configuration.BusinessRulesCache;
import com.mg.drools.businessrules.exceptions.BusinessRulesException;
import com.mg.persistence.domain.bizitem.model.BizItemModel;
import lombok.extern.log4j.Log4j2;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.HashMap;

import static com.mg.persistence.commons.Messages.MSG_TASK_END;
import static com.mg.persistence.commons.Messages.MSG_TASK_START;

@Log4j2
class DroolsRunner {

    private KieContainer kieContainer;


    DroolsRunner(String selector) {
        kieContainer = BusinessRulesCache.getInstance().get(selector);
    }


    /**
     * @param model             - bizItem to run rules on.
     * @param ruleCustomGlobals - map of objects that can be used in the rule as global
     * @return - bizItem model passed trough the rules
     * @throws BusinessRulesException if an exception occurs
     */
    public BizItemModel run(BizItemModel model, HashMap<String, Object> ruleCustomGlobals) throws BusinessRulesException {

        if (kieContainer == null) {
            throw new BusinessRulesException(String.format("Knowledge base for [%s] not found in the cache", model.getItemType()));
        }

        try {
            long startTime = System.currentTimeMillis();
            log.debug(String.format(MSG_TASK_START, "Running rules on " + model.getItemType()));

            KieSession kieSession = setRulesGlobals(kieContainer.newKieSession(), ruleCustomGlobals);
            if (log.isDebugEnabled()) {
                addAfterMatchEventListener(kieSession);
            }
            kieSession.insert(model);
            kieSession.fireAllRules();
            kieSession.dispose();

            String execDoneMsg = String.format("Rules execution took [%s ms] for the item [%s]", (System.currentTimeMillis() - startTime), model.getItemType());
            log.debug(String.format(MSG_TASK_END, execDoneMsg));

        } catch (Exception e) {
            String msg = e.getLocalizedMessage();
            log.error(msg, e);
            throw new BusinessRulesException(msg);
        }

        return model;
    }


    /**
     * Injects objects in to the rules. (Services, Utils, Models, etc.)
     *
     * @param kieSession        - new kie session
     * @param ruleCustomGlobals - map with objects to be injected
     * @return - kieSession
     */
    private KieSession setRulesGlobals(KieSession kieSession, HashMap<String, Object> ruleCustomGlobals) {
        if (ruleCustomGlobals == null) {//set global objects that can be accessed in te rules
            ruleCustomGlobals = new HashMap<>();
        }
        ruleCustomGlobals.forEach(kieSession::setGlobal);
        return kieSession;
    }


    /**
     * It will print matches after they have fired.
     *
     * @param kSession - execution session
     */
    private void addAfterMatchEventListener(KieSession kSession) {
        kSession.addEventListener(new DefaultAgendaEventListener() {
            public void afterMatchFired(AfterMatchFiredEvent event) {
                super.afterMatchFired(event);
                log.debug(event);
            }
        });
    }
}
