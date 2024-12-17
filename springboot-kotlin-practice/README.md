## Spring Boot 3.3 & Kotlin 1.9 Multi Module Project

---


## Core

* Each submodule of this module is responsible for one domain service.

* This must make the modular structure grow with the growth of the service.

### Core-api

* It is the only executable module in the project. 

* It is structured to contain both domains and APIs to maximize initial development productivity. 

* Primary responsibilities:

    * **Providing APIs**: Offers APIs for services.

    * **Framework Setup**: Configures frameworks needed for the services.
  
* **Future Scalability** : As the service grows, the core-api module can be divided into separate modules:

    * **core-api**: Focused on API-related logic. 

    * **core-domain**: Dedicated to domain logic and business rules.

### Code-enum

* This module contains enums that are used by core-api and need to be delivered to external modules.

* It ensures reusability and separation of concerns for shared constants.

---


## Reference

* [spring-boot-kotlin-template](https://github.com/team-dodn/spring-boot-kotlin-template)