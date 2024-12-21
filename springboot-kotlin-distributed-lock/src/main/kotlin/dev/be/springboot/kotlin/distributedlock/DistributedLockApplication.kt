package dev.be.springboot.kotlin.distributedlock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DistributedLockApplication

fun main(args: Array<String>) {
	runApplication<DistributedLockApplication>(*args)
}
