package dev.be.core.api.controller;

import dev.be.core.api.support.response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController implements HealthControllerDocs {

    @Override
    public ResponseEntity<CommonResponse<?>> health() {
        return ResponseEntity.ok(CommonResponse.success());
    }
}
