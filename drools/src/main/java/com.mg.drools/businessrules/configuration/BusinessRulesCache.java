package com.mg.drools.businessrules.configuration;

import lombok.extern.log4j.Log4j2;
import org.kie.api.runtime.KieContainer;

import java.util.HashMap;

@Log4j2
public class BusinessRulesCache {

    static private BusinessRulesCache instance;

    private BusinessRulesCache() {
    }

    public static BusinessRulesCache getInstance() {
        if (instance == null) {
            instance = new BusinessRulesCache();
        }
        return instance;
    }

    private static HashMap<String, KieContainer> businessRulesCache = new HashMap<>();

    public boolean contains(String key) {
        boolean containsKey = businessRulesCache.containsKey(key);
        log.debug(String.format("Lookup for KieContainer key [%s] in cache... Found ->[%s]", key, containsKey));
        return containsKey;
    }

    public KieContainer get(String key) {
        log.debug(String.format("Retrieving KieContainer for key [%s] from cache.", key));
        return businessRulesCache.get(key);
    }

    public void put(String key, KieContainer container) {
        log.debug(String.format("Storing in to cache KieContainer for key [%s].", key));
        businessRulesCache.put(key, container);
    }

    public void clear() {
        log.debug(String.format("BusinessRules cache removed. Total removed [%s]", businessRulesCache.size()));
        businessRulesCache.clear();

    }

}
