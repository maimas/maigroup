package com.mg.persistence.exceptions;


public class EntityServiceNotFoundException extends RuntimeException {

    public EntityServiceNotFoundException() {
        super("Entity Service not found");
    }

    public EntityServiceNotFoundException(String msg) {
        super(msg);
    }

}
