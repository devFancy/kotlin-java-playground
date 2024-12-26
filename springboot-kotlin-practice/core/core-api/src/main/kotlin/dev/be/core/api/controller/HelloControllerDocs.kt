package dev.be.core.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Tag(
    name = "도메인명",
    description = """
        도메인명과 관련된 그룹입니다.
        
        @@ 기능을 제공합니다.
    """
)

interface HelloControllerDocs {


    @Operation(summary = "특정 ID에 대한 조회",
        description = "지정된 ID를 사용하여 데이터를 조회합니다."
        )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "조회 성공"),
            ApiResponse(responseCode = "401", description = "인증 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "403", description = "권한 부족", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "404", description = "리소스 없음", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    @GetMapping("/{id}/board")
    fun get(
        @PathVariable("id") id: Long
    ): ResponseEntity<String>
}