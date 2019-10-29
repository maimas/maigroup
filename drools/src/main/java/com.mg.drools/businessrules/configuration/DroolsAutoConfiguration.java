package com.mg.drools.businessrules.configuration;


import com.mg.drools.businessrules.exceptions.BusinessRulesConfigurationException;
import lombok.extern.log4j.Log4j2;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static com.mg.persistence.commons.Messages.*;


@Configuration
@Log4j2
public class DroolsAutoConfiguration {

    @Value("classpath:rules")
    private Resource rules;


    @Bean
    public BusinessRulesCache build() throws IOException {
        return buildBusinessRulesCache(rules.getFile().getAbsolutePath());
    }

    /**
     * Generates the KieContainer for a specific rules folder tree.
     *
     * @param rulesDir - rules folder to include in the container file system
     * @return - KieContainer
     */
    private KieContainer kieContainer(File rulesDir) {
        long startTime = System.currentTimeMillis();
        log.debug(String.format(MSG_TASK_START, "Initializing Container for [" + rulesDir.getName() + "]"));

        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = getKieFileSystem(kieServices, rulesDir.getName(), rulesDir);
        KieModule kieModule = getKieModule(kieServices, kieFileSystem);
        KieContainer kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());

        log.debug(String.format("Completed in [%s ms].", (System.currentTimeMillis() - startTime)));
        return kieContainer;
    }


    /**
     * Generates the KieFileSystem
     *
     * @param kieServices - KieServices to be used
     * @param releaseID   - jar release id
     * @param rulesDir    - rules to be loaded
     * @return - KieFileSystem with all the rules in it
     */
    private KieFileSystem getKieFileSystem(KieServices kieServices, String releaseID, File rulesDir) {

        List<File> rules = getAllFiles(rulesDir);
        log.debug(String.format("Loading [%s] Rules from [%s]", rules.size(), rulesDir));

        ReleaseId releaseId = kieServices.newReleaseId("drools", releaseID, "1.0");
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.generateAndWritePomXML(releaseId);
        rules.forEach(rule -> kieFileSystem.write(ResourceFactory.newFileResource(rule)));

        return kieFileSystem;
    }

    /**
     * @param kieServices   - KieServices
     * @param kieFileSystem - KieFileSystem to be build
     * @return - KieModule with all teh knowledge base in it.
     */
    private KieModule getKieModule(KieServices kieServices, KieFileSystem kieFileSystem) {
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            log.error(kieBuilder.getResults().getMessages().toString());
            throw new BusinessRulesConfigurationException("Filed to build rules KnowledgeBase.");
        }
        return kieBuilder.getKieModule();
    }


    //----------------------Private Methods-------------------------

    private File[] getRulesDirs(String baseDirPath) {
        File rulesRootDir = new File(baseDirPath);
        if (rulesRootDir.exists()) {
            return rulesRootDir.listFiles();
        } else {
            throw new BusinessRulesConfigurationException("Rules root directory not found: " + baseDirPath);
        }
    }


    private BusinessRulesCache buildBusinessRulesCache(String baseDirPath) {
        log.info(String.format(MSG_SERVICE_START, "INITIALIZING BUSINESS RULES"));
        BusinessRulesCache businessRulesCache = BusinessRulesCache.getInstance();

        for (File dir : getRulesDirs(baseDirPath)) {
            businessRulesCache.put(dir.getName(), kieContainer(dir));
            log.info(MSG_SERVICE_END);
        }
        log.info(MSG_TASK_END_OK);
        return businessRulesCache;
    }


    private List<File> getAllFiles(File file) {
        try {
            return Files.walk(Paths.get(file.toURI()))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessRulesConfigurationException("Failed to lookup files in directory: " + file.getAbsolutePath());
        }
    }
}
