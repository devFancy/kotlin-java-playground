package dev.be.core.api.presentation.v1.dto;

import dev.be.core.api.domain.ExamplePost;

import java.util.UUID;

public record CreatePostResponse(UUID id, String title, String contents) {
    public static CreatePostResponse from(final ExamplePost examplePost) {
        return new CreatePostResponse(
                examplePost.getId(),
                examplePost.getTitle(),
                examplePost.getContents()
        );
    }
}
