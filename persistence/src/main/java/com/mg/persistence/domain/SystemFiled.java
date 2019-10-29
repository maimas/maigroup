package com.mg.persistence.domain;

public interface SystemFiled {

    String ID = "_id";
    String TrackingId = "trackingId";
    String ItemType = "itemType";
    String TargetItemType = "targetItemType";
    String TargetItemTID = "targetItemTID";

    String CreatedDate = "createdDate";
    String ModifiedDate = "modifiedDate";
    String Content = "content";
    String Relations = "relations";

    //Enum
    String Value = "value";

    //Attachment

    String RelatedItemTID = "relatedItemTID";
    String RelatedItemType = "relatedItemType";
    String Data = "data";
    String DataType = "dataType";
    String FileName = "fileName";

    //User
    String AccountSource = "accountSource";
    String Disabled = "disabled";
    String Deleted = "deleted";
    String Email = "email";
    String Content_Email = "content.email";
    String Password = "password";
    String Roles = "roles";
    String Gender = "gender";

}
