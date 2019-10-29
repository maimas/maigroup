package com.mg.web.rest.advancedsearch;

import com.mg.persistence.services.QueryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;


/**
 * Created by andrmaim on 8/17/2017.
 * <p>
 * Rest query executor. It provides save and search capabilities.
 * <p>
 * The result is a <code>ResponseEntity<code/> with the data and the status.
 */
@Component
@Log4j2
public class RestSearchService extends AbstractRestSearchService {


    protected RestSearchService(QueryService queryService) {
        super(queryService);
    }


    /**
     * Method executes <@code findAll> on a itemType based on the <@code queryProvider> container in a deferred mode.
     *
     * @param httpParams  - that hold search criteria information.
     * @param itemType    - itemType class name used to find the target itemType
     * @param resultClass - class name of expected result
     * @return - a deferred result that acts as an observable.
     */
    public Object search(Map<String, String> httpParams, String itemType, Class resultClass, Optional<Pageable> pageable) {

        try {
            Object data = super.executeQuery(httpParams, itemType, resultClass, pageable);
            return new ResponseEntity<>(data, HttpStatus.OK);

        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
