package com.mg.persistence.exceptions;

import java.util.List;

/**
 * Created by andrmaim on 8/17/2017.
 */


public class BizItemBusinessValidationException extends Exception {
    public BizItemBusinessValidationException(String collection, List<String> violations) {

        super(collection + " business validation failed " + (violations != null ? violations.toString() : ""));
    }
}
