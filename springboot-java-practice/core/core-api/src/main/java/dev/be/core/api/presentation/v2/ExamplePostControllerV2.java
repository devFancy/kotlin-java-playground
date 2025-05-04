package dev.be.core.api.presentation.v2;

import dev.be.core.api.presentation.v1.dto.CreatePostRequest;
import dev.be.core.api.presentation.v1.dto.CreatePostResponse;
import dev.be.core.api.application.ExamplePostService;
import dev.be.core.api.common.support.response.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequestMapping("/api/v2")
@RestController
public class ExamplePostControllerV2 implements ExamplePostControllerDocsV2 {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ExamplePostService examplePostService;
    private static final String USER_ID = "userId";

    public ExamplePostControllerV2(final ExamplePostService examplePostService) {
        this.examplePostService = examplePostService;
    }

    @PostMapping("/posts/new")
    @Override
    public ResponseEntity<CommonResponse<CreatePostResponse>> create(
            @RequestHeader(value = USER_ID, required = false) String userId,
            @RequestBody final CreatePostRequest request) {

        // This is just a sample, so userId handling is done here.
        // In real cases, this should be handled in the service or domain layer.
        if (userId == null || userId.isBlank()) {
            userId = generateUserId();
            log.info("[Generated] userId: {}", userId);
        } else {
            log.info("[Received] userId: {}", userId);
        }

        log.info("[START] create post. userId: {}", userId);

        // Business Logic
        CreatePostResponse response = examplePostService.create(request);

        log.info("[END] create post. userId: {}", userId);
        return ResponseEntity.created(URI.create("/api/v1/posts/" + response.id()))
                .body(CommonResponse.success(response));
    }

    private String generateUserId() {
        String prefix = "test_user_";
        long timestamp = System.currentTimeMillis();
        return prefix + timestamp;
    }
}
