package com.mg.persistence.exceptions;

import java.util.List;

/**
 * Created by andrmaim on 8/17/2017.
 */


public class BizItemSchemaValidationException extends Exception {

    public BizItemSchemaValidationException(String collection, List<String> violations) {
        super(String.format("[%s] validation failed [%s]", collection, (violations != null ? violations.toString() : "")));
    }

}
