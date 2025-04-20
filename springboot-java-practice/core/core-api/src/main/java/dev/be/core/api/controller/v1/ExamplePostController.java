package dev.be.core.api.controller.v1;

import dev.be.core.enums.ExamplePostStatus;
import dev.be.core.api.controller.v1.dto.CreatePostRequest;
import dev.be.core.api.controller.v1.dto.CreatePostResponse;
import dev.be.core.api.service.ExamplePostService;
import dev.be.core.api.support.error.CoreException;
import dev.be.core.api.support.error.ErrorType;
import dev.be.core.api.support.response.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RequestMapping("/api")
@RestController
public class ExamplePostController implements ExamplePostControllerDocs {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ExamplePostService examplePostService;

    public ExamplePostController(final ExamplePostService examplePostService) {
        this.examplePostService = examplePostService;
    }

    @PostMapping("/posts/new")
    @Override
    public ResponseEntity<CommonResponse<CreatePostResponse>> create(@RequestBody final CreatePostRequest request) {
        CreatePostResponse response = examplePostService.create(request);
        return ResponseEntity.created(URI.create("/api/posts/" + response.id()))
                .body(CommonResponse.success(response));
    }

    @GetMapping("/post-status")
    @Override
    public ResponseEntity<CommonResponse<List<String>>> getPostStatus() {
        List<String> codes = Arrays.stream(ExamplePostStatus.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(CommonResponse.success(codes));
    }

    @GetMapping("/error")
    @Override
    public ResponseEntity<CommonResponse<Void>> error() {
        throw new CoreException(ErrorType.DEFAULT_ERROR, "에러 테스트용 데이터");
    }

    @GetMapping("/log")
    @Override
    public ResponseEntity<CommonResponse<?>> logTest() {
        log.debug("DEBUG 레벨 로그입니다.");
        log.info("INFO 레벨 로그입니다.");
        log.warn("WARN 레벨 로그입니다.");
        log.error("ERROR 레벨 로그입니다.");
        return ResponseEntity.ok(CommonResponse.success());
    }
}
