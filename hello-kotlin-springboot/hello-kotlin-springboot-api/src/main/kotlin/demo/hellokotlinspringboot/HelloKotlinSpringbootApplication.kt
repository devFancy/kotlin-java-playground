package demo.hellokotlinspringboot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HelloKotlinSpringbootApplication

fun main(args: Array<String>) {
    runApplication<HelloKotlinSpringbootApplication>(*args)
}
