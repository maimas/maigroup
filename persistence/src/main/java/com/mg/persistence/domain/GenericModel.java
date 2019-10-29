package com.mg.persistence.domain;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Getter
@Setter
public abstract class GenericModel {

    @Id
    private String _id;
    private String trackingId, itemType;
    private Date createdDate, modifiedDate;

}
