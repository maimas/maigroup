package com.mg.persistence.services;


import com.mg.persistence.domain.bizitem.definition.BizItemSchemaType;
import com.mg.persistence.domain.bizitem.model.BizItemSchemaModel;
import com.mg.persistence.domain.bizitem.reader.BizItemSchemaXmlReader;
import com.mg.persistence.domain.bizitem.service.BizItemSchemaService;
import com.mg.persistence.domain.bizitemRelations.definition.BizItemRelationsType;
import com.mg.persistence.domain.bizitemRelations.model.BizItemRelationsModel;
import com.mg.persistence.domain.bizitemRelations.reader.BizItemRelationsXmlReader;
import com.mg.persistence.domain.bizitemRelations.service.BizItemRelationsService;
import com.mg.persistence.domain.enumeration.definition.EnumerationType;
import com.mg.persistence.domain.enumeration.model.EnumModel;
import com.mg.persistence.domain.enumeration.reader.EnumXmlReader;
import com.mg.persistence.domain.enumeration.service.EnumService;
import com.mg.persistence.exceptions.BizItemRelationException;
import com.mg.persistence.exceptions.RuntimeConfigurationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static com.mg.persistence.commons.Messages.*;

/**
 * Created by andrmaim on 8/17/2017.
 * <p>
 * Designed to initialize the models definition and create the database objects objects like schemas indexes etc.
 */
@Service
@Log4j2
public class SchemaInitializerService {

    private EnumService enumService;
    private BizItemSchemaService itemSchemaService;
    private BizItemRelationsService itemRelationsService;

    @Autowired
    public SchemaInitializerService(BizItemSchemaService itemSchemaService, EnumService enumService, BizItemRelationsService itemRelationsService) {
        this.itemSchemaService = itemSchemaService;
        this.itemRelationsService = itemRelationsService;
        this.enumService = enumService;
    }


    public void initMetadata(String schemasRootDir) {
        log.info(String.format(MSG_SERVICE_START, "System Metadata initialization"));
        try {

            URL contextResource = Thread.currentThread().getContextClassLoader().getResource(schemasRootDir);
            File externalResource = new File(schemasRootDir);

            File schemaDir = null;
            if (externalResource.exists()) {
                schemaDir = externalResource;
            } else if (contextResource != null) {
                schemaDir = new File(contextResource.toURI());
            }

            if (schemaDir == null || !schemaDir.exists()) {
                throw new RuntimeConfigurationException("Invalid metadata directory: " + schemasRootDir);
            }

            itemSchemaService.clearCache();
            itemRelationsService.clearCache();
            initSystemEnums(schemaDir);
            initSystemBizItemSchema(schemaDir);
            initSystemBizItemRelations(schemaDir);

        } catch (Exception e) {
            throw new RuntimeConfigurationException(e.getLocalizedMessage());
        }
        log.info(MSG_TASK_END_OK);
    }


    /**
     * Loads the enumerations in the database from the XML files.
     */
    private void initSystemEnums(File dir) {
        log.info(String.format(MSG_TASK_START, "Initialize Enums"));

        File enumSchemasDir = new File(dir, "enum");
        initSystemEnums(getAllFiles(enumSchemasDir));

        log.info(MSG_TASK_END_OK);
    }

    /**
     * Init BizItem schema definitions
     */
    private void initSystemBizItemSchema(File dir) {
        log.info(String.format(MSG_TASK_START, "Initialize BizItem Schemas"));

        File bizItemSchemas = new File(dir, "schema");
        initBizItemsSchemas(getAllFiles(bizItemSchemas));

        log.info(MSG_TASK_END_OK);
    }


    /**
     * Init BizItem schema definitions
     */
    private void initSystemBizItemRelations(File dir) {
        log.info(String.format(MSG_TASK_START, "Initialize BizItemRelations"));

        File bizItemRelationsDir = new File(dir, "schema_relations");
        initBizItemsRelationSchemas(getAllFiles(bizItemRelationsDir));

        log.info(MSG_TASK_END_OK);
    }


    private void initSystemEnums(List<File> files) {
        EnumXmlReader enumXmlReader = new EnumXmlReader();

        if (files == null || files.isEmpty()) {
            log.warn("Enums schema generation is skipped as there are no definition files present");
            return;
        }

        files.forEach(metaFile -> {
            EnumerationType enumeration = enumXmlReader.readXml(metaFile);

            List<EnumModel> enumModels = enumXmlReader.toEnumModels(enumeration);
            enumModels.forEach(en -> enumService.save(en));
        });
    }

    private void initBizItemsSchemas(List<File> files) {
        BizItemSchemaXmlReader schemaXmlReader = new BizItemSchemaXmlReader();

        if (files == null || files.isEmpty()) {
            log.warn("BizItemsSchemas generation is skipped as there are no definition files present");
            return;
        }

        files.forEach(metaFile -> {
            BizItemSchemaType schemaType = schemaXmlReader.readXml(metaFile);
            BizItemSchemaModel schemaModel = schemaXmlReader.toSchemaModel(schemaType);
            itemSchemaService.save(schemaModel);
        });
    }

    private void initBizItemsRelationSchemas(List<File> files) {
        BizItemRelationsXmlReader reader = new BizItemRelationsXmlReader();

        if (files == null || files.isEmpty()) {
            log.warn("BizItemsRelationSchemas generation is skipped as there are no definition files present");
            return;
        }

        files.forEach(metaFile -> {
            try {
                BizItemRelationsType xmlSchema = reader.readXml(metaFile);
                BizItemRelationsModel model = reader.parse(xmlSchema);
                itemRelationsService.save(model);
            } catch (BizItemRelationException e) {
                String msg = "Failed to init BizItem relation";
                log.error(msg, e);
                throw new RuntimeException(msg);
            }
        });
    }


    private List<File> getAllFiles(File file) {
        try {

            if (!file.exists()) {
                return null;
            }

            return Files.walk(Paths.get(file.toURI()))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeConfigurationException("Failed to lookup files in directory: " + file.getAbsolutePath());
        }
    }

}


