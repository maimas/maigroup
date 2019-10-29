package com.mg.persistence.commons;

import java.util.UUID;

public class TrackingIdGenerator {

    /**
     * Generate unique identifier
     *
     * @return - unique identifier
     */
    public static synchronized String generateUnique() {
        return UUID.randomUUID().toString();
    }

}
