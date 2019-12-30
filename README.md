# MaiGroup Platform Repository

This project is designed to support MongoDB persistence operations that allows to use custom Models with multiple validation constraints.
Project supports runtime model generation and update, metadata can be re-initialized trough a service call with the new field and constraints and enforced at runtime.

## Getting Started

TBD


### Design (high level)
1. A repository with the xml models should be defined (it contains enums and model/object definitions)
2. Once definition(xml model) is ready, SchemaInitializationService is invoked to init models. Schemas will be loaded in DB and used to validate future models that will be passed to be persisted.
3. After the systems has the schema initialized, we are ready to process our models. Schema and validation rules will be enforced according to the xml definition stored in the DB.
4. When we need to make a change to model schema, xml schema from repository should be updated and SchemaInitializationService to be invoked to refresh/update existing definition in DB.

### Prerequisites
Mongo DB to be installed and application.properties file configured.

### Installing


## Running the tests
At this point of time ony integration tests are created (with embedded mongodb) - coverage 100%

## Deployment
Add the jar as a dependence to your project.


## Authors
* **Andrei Maimas** - *Developer* 

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed. 
<!--
- see the [LICENSE.md](LICENSE.md) file for details
-->


