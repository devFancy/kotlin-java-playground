package dev.be.core.api.controller.v1;

import dev.be.core.api.controller.v1.dto.CreatePostRequest;
import dev.be.core.api.controller.v1.dto.CreatePostResponse;
import dev.be.core.api.support.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(
        name = "도메인명",
        description = """
        도메인명과 관련된 그룹입니다.
        
        @@ 기능을 제공합니다.
    """
)
@RequestMapping("/api")
public interface ExamplePostControllerDocs {

    @Operation(
            summary = "특정 게시글 등록",
            description = "게시글을 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "권한 부족", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "리소스 없음", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/posts/new")
    ResponseEntity<CommonResponse<CreatePostResponse>> create(
            @RequestBody final CreatePostRequest request
    );

    @Operation(
            summary = "전체 게시글 상태 조회",
            description = "전체 게시글 상태를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @GetMapping("/post-status")
    ResponseEntity<CommonResponse<List<String>>>  getPostStatus();

    @GetMapping("/error")
    ResponseEntity<CommonResponse<Void>> error();
}
