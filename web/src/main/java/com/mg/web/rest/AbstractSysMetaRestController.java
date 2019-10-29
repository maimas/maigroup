package com.mg.web.rest;


import com.mg.persistence.services.SchemaInitializerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class AbstractSysMetaRestController {

    @Autowired
    private SchemaInitializerService metadataInitializerService;


    public ResponseEntity initSchemas(String schemasRootDir) {
        metadataInitializerService.initMetadata(schemasRootDir);
        return new ResponseEntity<>("Schema Initialization completed successfully", HttpStatus.OK);
    }

}