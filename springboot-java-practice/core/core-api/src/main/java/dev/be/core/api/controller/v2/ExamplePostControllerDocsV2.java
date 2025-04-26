package dev.be.core.api.controller.v2;

import dev.be.core.api.controller.v1.dto.CreatePostRequest;
import dev.be.core.api.controller.v1.dto.CreatePostResponse;
import dev.be.core.api.support.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(
        name = "도메인명 (Ver.2)",
        description = """
                    도메인명과 관련된 그룹입니다.
                    
                    @@ 기능을 제공합니다.
                """
)
public interface ExamplePostControllerDocsV2 {

    String USER_ID = "userId";

    @Operation(
            summary = "특정 게시글 등록",
            description = "요청 정보를 기반으로 새 게시글을 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "권한 부족", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "리소스 없음", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<CommonResponse<CreatePostResponse>> create(
            @Parameter(
                    description = "사용자 식별자. 임의의 값 + timestamp 조합 형식 (예: test_user_1234567890123)",
                    example = "test_user_1234567890123")
            @RequestHeader(value = USER_ID, required = false) final String userId,
            @RequestBody final CreatePostRequest request
    );
}
