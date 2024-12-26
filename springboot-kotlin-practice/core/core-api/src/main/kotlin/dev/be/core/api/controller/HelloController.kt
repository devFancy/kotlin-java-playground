package dev.be.core.api.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController : HelloControllerDocs {

    override fun get(@PathVariable("id") id: Long): ResponseEntity<String> {
        val result = "조회 결과: ID = $id"
        return ResponseEntity.ok(result)
    }

}