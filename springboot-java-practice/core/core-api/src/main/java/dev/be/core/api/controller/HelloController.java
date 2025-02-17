package dev.be.core.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerDocs {

    @Override
    public ResponseEntity<String> get(@PathVariable("id")final Long id) {
        String result = "조회 결과: ID = " + id;
        return ResponseEntity.ok(result);

    }
}
