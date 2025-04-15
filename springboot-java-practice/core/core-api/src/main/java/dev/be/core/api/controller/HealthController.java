package dev.be.core.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController implements HealthControllerDocs {

    @Override
    public ResponseEntity<Void> health() {
        return ResponseEntity.ok().build();
    }
}
