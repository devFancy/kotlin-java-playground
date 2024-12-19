package dev.be.springboot.kotlin.build.manager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringbootKotlinBuildManagerApplication

fun main(args: Array<String>) {
	runApplication<SpringbootKotlinBuildManagerApplication>(*args)
}
