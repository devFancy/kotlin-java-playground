# Java Spring Boot Practice (Multi-Module)

This project is not meant to be a perfect design.
It is a basic structure for practicing and learning how to build Spring Boot projects in a better way.

I started this project because I’m interested in clean and sustainable software design.
I was inspired by the [spring-boot-java-template](https://github.com/team-dodn/spring-boot-java-template) and wanted to create my own version for learning.


# Project Information

* Java Version: 21
* Spring Boot Version: 3.2.5
* Database: H2/MySQL (example implementation)
* Build Tool: Gradle


# Module

## Core

The main executable module of this project.  
It contains the API layer and integrates with other internal modules.

### core:core-api

This is the main module that runs the application.  
It connects to other modules in the project.

### core:core-enum

This module provides shared enumerations used throughout the `core-api`.


## Support

### support:logging

This module handles common logging functionality.  

It can be used across services to apply consistent logging.  
Sentry integration can be added if needed.


## Reference

Special thanks to [geminiKim](https://github.com/geminiKim) for the original [spring-boot-java-template](https://github.com/team-dodn/spring-boot-java-template), which inspired the creation of this project.

If you'd like to check the latest versions of Kotlin, Spring Boot, or additional resources, I recommend visiting the spring-boot-kotlin-template repository.
