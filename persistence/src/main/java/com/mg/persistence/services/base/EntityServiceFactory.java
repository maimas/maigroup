package com.mg.persistence.services.base;


import com.mg.persistence.exceptions.EntityServiceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

import static com.mg.persistence.commons.Messages.*;


/**
 * This factory service loads creates a local cache where it stores all
 * the persistence services that are implementing the interface <@code>EntityService</@code>
 */
@Service
@Log4j2
public class EntityServiceFactory {

    @Autowired
    private List<EntityService> services;
    private static final HashMap<Class, EntityService> cache = new HashMap<>();


//        @PostConstruct
//    public void initServicesCache() {
//        logger.info(String.format(Messages.MSG_SERVICE_START, "PERSISTENCE SERVICES FACTORY"));
//
//        this.logger.info("Loading persistence Entity Services in to cache...");
//        services.forEach(service -> {
//            boolean loaded = false;
//            Type[] superInterfaces = service.getClass().getGenericInterfaces();
//            if (superInterfaces != null && superInterfaces.length > 0) {
//                Type[] actualTypeArguments = ((ParameterizedTypeImpl) superInterfaces[0]).getActualTypeArguments();
//
//                if (actualTypeArguments != null && actualTypeArguments.length > 0) {
//                    try {
//                        Class persistenceModelClass = Class.forName(((Class) actualTypeArguments[0]).getName());
//                        cache.set(persistenceModelClass, service);
//                        loaded = true;
//                    } catch (ClassNotFoundException ignore) {
//                    }
//                }
//            }
//            if (!loaded) {
//                this.logger.warn(String.format("Failed to load Entity Services in to cache: [%s]", service.getClass().getName()));
//            }
//
//        });
//        this.logger.info(String.format("Loaded [%s] Services.", cache.size()));
//
//        logger.info(String.format(Messages.MSG_SERVICE_START, "PERSISTENCE SERVICES FACTORY - [OK]"));
//    }

    @PostConstruct
    public void initServicesCache() {
        log.info(String.format(MSG_SERVICE_START, "LOADING APPLICATION SERVICES"));

        services.forEach(service -> {
            cache.put(service.getClass(), service);
            log.info(String.format("--> [%s]", service.getClass().getSimpleName()));
        });
        log.info(String.format(MSG_TASK_END, "Loaded [" + cache.size() + "] services."));
        log.info(MSG_SERVICE_END);
    }


    /**
     * Serves the Entity Service for the provided class.
     *
     * @param entityClass - class of the Persistence model
     * @return - persistence service;
     */
    public static EntityService getService(Class entityClass) {
        if (cache.containsKey(entityClass)) {
            return cache.get(entityClass);
        } else {
            throw new EntityServiceNotFoundException("Entity Service not found for the class: " + entityClass);
        }
    }

    public static HashMap<Class, EntityService> getServices() {
        return cache;
    }

}
