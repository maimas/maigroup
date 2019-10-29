package com.mg.web.rest;


import com.mg.persistence.domain.SystemFiled;
import com.mg.persistence.domain.enumeration.model.EnumModel;
import com.mg.persistence.domain.enumeration.service.EnumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public abstract class AbstractEnumsRestController {

    @Autowired
    private EnumService enumService;


    @GetMapping("/{enumType}")
    public ResponseEntity getEnum(@PathVariable String enumType) {
        Query query = new Query(Criteria.where(SystemFiled.TrackingId).exists(true));
        List<EnumModel> enums = enumService.find(query, enumType);

        return (!enums.isEmpty()) ? ResponseEntity.ok(enums) : ResponseEntity.notFound().build();
    }

}