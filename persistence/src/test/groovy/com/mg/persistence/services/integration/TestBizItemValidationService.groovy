package com.mg.persistence.services.integration

import com.mg.persistence.config.MongoDBConfig
import com.mg.persistence.domain.bizitem.model.BizItemModel
import com.mg.persistence.domain.bizitem.service.BizItemConverterService
import com.mg.persistence.domain.bizitem.service.BizItemSchemaService
import com.mg.persistence.domain.bizitem.service.BizItemService
import com.mg.persistence.domain.bizitemRelations.service.BizItemRelationsService
import com.mg.persistence.domain.enumeration.service.EnumService
import com.mg.persistence.services.QueryService
import com.mg.persistence.services.SchemaInitializerService
import com.mg.persistence.validation.BizItemValidationService
import org.apache.commons.beanutils.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DuplicateKeyException
import org.springframework.test.context.TestPropertySource
import spock.lang.Unroll

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic

/**
 * This tests suite is designed to ensure correctness of the model validation constraints.
 */

@SpringBootTest(classes = [
        SchemaInitializerService.class,
        BizItemSchemaService.class,
        EnumService.class,
        BizItemRelationsService.class,
        BizItemValidationService.class,
        BizItemConverterService.class,
        BizItemService.class,
        QueryService.class,
        MongoDBConfig.class])
@TestPropertySource(locations = "classpath:application.properties")
class TestBizItemValidationService extends IntegrationTestsSetup {


    @Autowired
    private SchemaInitializerService initializerService
    @Autowired
    private BizItemService bizItemService
    @Autowired
    private QueryService queryService


    static boolean schemaInitialized
    static def futureDate = new Date(System.currentTimeMillis() + System.currentTimeMillis())
    static def pastDate = new Date(System.currentTimeMillis() - 1000000)

    def setup() {
        if (!schemaInitialized) {
            initializerService.initMetadata("models")
            schemaInitialized = true
        }
    }


    def "test: model unique constraint"() {

        setup: "creating two objects with same value for the unique field"
        BizItemModel model = bizItemService.save(generateModel())
        BizItemModel dupModel = generateModel()
        dupModel.setTrackingId(model.getTrackingId())

        when: "saving second object"
        bizItemService.save(dupModel)

        then: "dup key exception is thrown"
        DuplicateKeyException e = thrown()
        e.getMessage().startsWith("E11000 duplicate key error collection: testDB.Car index: trackingId_1")
    }

    @Unroll
    def "test: model validation constraint: [#fieldName]--> [#fieldValue] "() {
        given:
        BizItemModel model = generateModel()

        expect:
        validateModel(model, fieldName, fieldValue, checkValue, expecteException, exceptionMsg)

        where:
        fieldName                   | fieldValue            | expecteException | checkValue | exceptionMsg
        "createdDate"               | -1                    | true             | false      | "DateConverter does not support default String to 'Date' conversion."
        "createdDate"               | 1                     | true             | false      | "DateConverter does not support default String to 'Date' conversion."
        "createdDate"               | "1"                   | true             | false      | "DateConverter does not support default String to 'Date' conversion."
        "createdDate"               | ""                    | true             | false      | "No value specified for 'Date'"
        "createdDate"               | " "                   | true             | false      | "No value specified for 'Date'"
        "createdDate"               | pastDate              | false            | false      | null


        "modifiedDate"              | -1                    | true             | false      | "DateConverter does not support default String to 'Date' conversion."
        "modifiedDate"              | 1                     | true             | false      | "DateConverter does not support default String to 'Date' conversion."
        "modifiedDate"              | "1"                   | true             | false      | "DateConverter does not support default String to 'Date' conversion."
        "modifiedDate"              | ""                    | true             | false      | "No value specified for 'Date'"
        "modifiedDate"              | " "                   | true             | false      | "No value specified for 'Date'"
        "modifiedDate"              | pastDate              | false            | false      | null

        "trackingId"                | null                  | false            | false      | null
        "trackingId"                | " "                   | true             | false      | '[Car] validation failed [[Field [TrackingID] length should be between [1] and [100], value [] ]]'
        "trackingId"                | "-1"                  | false            | true       | null
        "trackingId"                | "1"                   | false            | true       | null
        "trackingId"                | ""                    | true             | false      | '[Car] validation failed [[Field [TrackingID] length should be between [1] and [100], value [] ]]'
        "trackingId"                | '1234'                | false            | true       | null
        "trackingId"                | '1234'                | true             | false      | 'E11000 duplicate key error collection:'

        "itemType"                  | null                  | true             | false      | 'Validation Schema [null] not found in [BizItemSchemas]'
        "itemType"                  | " "                   | true             | false      | 'Validation Schema [] not found in [BizItemSchemas]'
        "itemType"                  | ""                    | true             | false      | 'Validation Schema [] not found in [BizItemSchemas]'
        "itemType"                  | "notExists"           | true             | false      | 'Validation Schema [notExists] not found in [BizItemSchemas]'
        "itemType"                  | "Car"                 | false            | true       | null

        "content.stringNotNull"     | ""                    | false            | true       | null
        "content.stringNotNull"     | " "                   | false            | true       | null
        "content.stringNotNull"     | "test1"               | false            | true       | 'Invalid past year for the field [Year]'
        "content.stringNotNull"     | null                  | true             | false      | '[Car] validation failed [[Field [FNotNull] value [null] should not be null]]'

        "content.email"             | ""                    | true             | false      | '[Car] validation failed [[Field [Email] has invalid value [], not matching the regular expression [^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$]]]'
        "content.email"             | " "                   | true             | false      | '[Car] validation failed [[Field [Email] has invalid value [ ], not matching the regular expression [^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$]]]'
        "content.email"             | "test1"               | true             | false      | '[Car] validation failed [[Field [Email] has invalid value [test1], not matching the regular expression [^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$]]]'
        "content.email"             | 'test.test@gmail.com' | false            | true       | null
        "content.email"             | null                  | false            | true       | null

        "content.stringSize_100"    | null                  | false            | true       | null
        "content.stringSize_100"    | ""                    | true             | false      | '[Car] validation failed [[Field [FSize100] length should be more than [100], value []]]'
        "content.stringSize_100"    | " "                   | true             | false      | '[Car] validation failed [[Field [FSize100] length should be more than [100], value [ ]]]'
        "content.stringSize_100"    | 'g'                   | true             | true       | '[Car] validation failed [[Field [FSize100] length should be more than [100], value [g]]]'
        "content.stringSize_100"    | randomAlphabetic(99)  | true             | false      | '[Car] validation failed [[Field [FSize100] length should be more than [100], value'
        "content.stringSize_100"    | randomAlphabetic(100) | false            | true       | null
        "content.stringSize_100"    | randomAlphabetic(101) | false            | true       | null

        "content.stringSize_10_100" | null                  | false            | true       | null
        "content.stringSize_10_100" | ""                    | true             | false      | '[Car] validation failed [[Field [FSize10_100] length should be between [10] and [100], value [] ]]'
        "content.stringSize_10_100" | " "                   | true             | false      | '[Car] validation failed [[Field [FSize10_100] length should be between [10] and [100], value [ ] ]]'
        "content.stringSize_10_100" | 'x'                   | true             | false      | '[Car] validation failed [[Field [FSize10_100] length should be between [10] and [100], value [x] ]]'
        "content.stringSize_10_100" | randomAlphabetic(10)  | false            | true       | null
        "content.stringSize_10_100" | randomAlphabetic(99)  | false            | true       | null
        "content.stringSize_10_100" | randomAlphabetic(100) | false            | true       | null
        "content.stringSize_10_100" | randomAlphabetic(101) | true             | false      | '[Car] validation failed [[Field [FSize10_100] length should be between [10] and [100], value ['

        "content.year"              | ""                    | true             | false      | '[Car] validation failed [[Invalid past year for the field [Year], value []]]'
        "content.year"              | null                  | false            | false      | null
        "content.year"              | "200999"              | true             | false      | '[Car] validation failed [[Invalid past year for the field [Year], value [200999]]]'
        "content.year"              | 200999                | true             | false      | '[Car] validation failed [[Invalid past year for the field [Year], value [200999]]]'
        "content.year"              | 2000                  | false            | true       | null

        "content.futureYear"        | ""                    | true             | false      | '[Car] validation failed [[Invalid future year value [FutureYear] for the field [].]]'
        "content.futureYear"        | null                  | false            | false      | null
        "content.futureYear"        | "200998"              | false            | true       | null
        "content.futureYear"        | 200999                | false            | true       | null
        "content.futureYear"        | 2000                  | true             | false      | '[Car] validation failed [[Invalid future year value [FutureYear] for the field [2000].]]'

        "content.millage"           | "10000"               | true             | false      | '[Car] validation failed [[Invalid value [10000] for the field [Millage]. It should be equal ot less than [100]]]'
        "content.millage"           | "10"                  | false            | true       | null
        "content.millage"           | 10000                 | true             | false      | '[Car] validation failed [[Invalid value [10000] for the field [Millage]. It should be equal ot less than [100]]]'
        "content.millage"           | 10                    | false            | true       | null

        "content.millageMin"        | "10000"               | false            | true       | null
        "content.millageMin"        | 10000                 | false            | true       | '[Car] validation failed [[Invalid value [10000] for the field [Millage]. It should be equal ot less than [100]]]'
        "content.millageMin"        | "10"                  | true             | false      | '[Car] validation failed [[Invalid value [10] for the field [MillageMin]. It should be equal ot grater than [100]]]'
        "content.millageMin"        | 10                    | true             | false      | '[Car] validation failed [[Invalid value [10] for the field [MillageMin]. It should be equal ot grater than [100]]]'

        "content.futureDate"        | null                  | false            | false      | null
        "content.futureDate"        | pastDate              | true             | false      | '[Car] validation failed [[Invalid future date value [futureDate] for the field '
        "content.futureDate"        | futureDate            | false            | true       | null
        "content.futureDate"        | 10                    | true             | false      | '[Car] validation failed [[Invalid future date value [futureDate] for the field [10]]]'

        "content.pastDate"          | null                  | false            | false      | null
        "content.pastDate"          | pastDate              | false            | true       | null
        "content.pastDate"          | futureDate            | true             | false      | '[Car] validation failed [[Invalid past date for the field [PastDate], value '
        "content.pastDate"          | 10                    | true             | false      | '[Car] validation failed [[Invalid past date for the field [PastDate], value [10]]]'

        "content.maker"             | null                  | true             | false      | 'Field [Maker] has an invalid enumeration value [null]'
        "content.maker"             | " "                   | true             | false      | 'Field [Maker] has an invalid enumeration value [ ]'
        "content.maker"             | ""                    | true             | false      | 'Field [Maker] has an invalid enumeration value []'
        "content.maker"             | "invalid"             | true             | false      | 'Field [Maker] has an invalid enumeration value [invalid]'
        "content.maker"             | "honda"               | false            | true       | null

        "content.available"         | null                  | false            | true       | null
        "content.available"         | true                  | false            | true       | null
        "content.available"         | false                 | false            | true       | null
        "content.available"         | "true"                | false            | true       | null
        "content.available"         | "false"               | false            | true       | null
        "content.available"         | "yes"                 | true             | true       | 'Field [Available] has invalid value [yes]. It should be [true] or [false]'
        "content.available"         | "no"                  | true             | true       | 'Field [Available] has invalid value [no]. It should be [true] or [false]'
        "content.available"         | "invalid"             | true             | false      | 'Field [Available] has invalid value [invalid]. It should be [true] or [false]'
        "content.available"         | 10                    | true             | false      | 'Field [Available] has invalid value [10]. It should be [true] or [false]'

        "content.listOfEnums"       | null                  | false            | true       | null
        "content.listOfEnums"       | []                    | false            | true       | null
        "content.listOfEnums"       | ['civic', 'accord']   | false            | true       | null
        "content.listOfEnums"       | ['civic', 'invalid']  | true             | false      | '[Car] validation failed [[Field [Enum List] contains at list one invalid enumeration value: [[civic, invalid]]]]'
    }


    @Unroll
    def "test: model validation constraint with nested object: [#fieldName]--> [#fieldValue] "() {
        given:
        BizItemModel model = generateModel()
        BizItemModel o1 = generateFullModel()
        BizItemModel o2 = generateFullModel()
        model.set("object", o1)
        model.set("objectList", [o1, o2])

        expect:
        validateModel(model, fieldName, fieldValue, checkValue, expecteException, exceptionMsg)

        where:
        fieldName            | fieldValue                                 | expecteException | checkValue | exceptionMsg
        "content.object"     | generateFullModel()                        | false            | false      | null
        "content.object"     | null                                       | false            | false      | null
        "content.object"     | ''                                         | true             | false      | 'Validation Schema [] not found in [BizItemSchemas]'
        "content.object"     | ' '                                        | true             | false      | 'Validation Schema [ ] not found in [BizItemSchemas]'

        "content.objectList" | [generateFullModel(), generateFullModel()] | false            | false      | null
        "content.objectList" | null                                       | false            | false      | null
        "content.objectList" | []                                         | false            | false      | null
        "content.objectList" | ''                                         | true             | false      | 'Failed to parse property [objectList] from [Car]'
        "content.objectList" | ' '                                        | true             | false      | 'Failed to parse property [objectList] from [Car]'
    }


    //------------------------------------------------------------
    //---------------------Private methods (helpers)--------------
    //------------------------------------------------------------

    private static BizItemModel generateModel() {
        BizItemModel model = new BizItemModel("Car")
        model.set("stringNotNull", "test")
        model.set("stringSize_100", randomAlphabetic(100))
        model.set("stringSize_10_100", "0123455789")
        model.set("pastDate", pastDate)
        model.set("futureDate", futureDate)
        model.set("futureYear", 2999)
        model.set("year", 2018)
        model.set("millage", 100)
        model.set("millageMin", 101)
        model.set("email", "test.test@domain.com")
        model.set("available", true)
        model.set("maker", "mercedes")
        model.set("model", "civic")
        model.set("listOfEnums", ["civic", 'accord'])

        return model
    }

    private static BizItemModel generateFullModel() {
        def model = generateModel()
        model.setItemType("Car2")
        model.setTrackingId("1234")
        model.setCreatedDate(new Date())
        model.setModifiedDate(new Date())

        return model
    }

    private void validateModel(BizItemModel model,
                               String fieldName,
                               Object fieldValue,
                               boolean checkValue,
                               boolean expectException,
                               String exceptionMsg) {

        try {
            if (fieldName.startsWith("content")) {
                String key = fieldName.replace("content.", "")
                model.set(key, fieldValue)

            } else {
                BeanUtils.setProperty(model, fieldName, fieldValue)
            }

            model = bizItemService.save(model)

            assert !expectException

            if (checkValue) {
                if (fieldName.startsWith("content.")) {//content property
                    String key = fieldName.replace("content.", "");
                    assert Objects.equals(fieldValue, model.get(key))

                } else {//system property
                    assert Objects.equals(fieldValue, BeanUtils.getProperty(model, fieldName))
                }
            }

        } catch (Exception e) {
            assert e.getMessage().contains(exceptionMsg)
            assert expectException: "Expected ${exceptionMsg}"
        }
    }


}
