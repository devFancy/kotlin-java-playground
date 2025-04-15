package dev.be.core.api.service;

import dev.be.core.api.controller.v1.dto.CreatePostRequest;
import dev.be.core.api.controller.v1.dto.CreatePostResponse;
import dev.be.core.api.domain.ExamplePost;
import dev.be.core.api.domain.ExamplePostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExamplePostService {

    private final ExamplePostRepository examplePostRepository;

    public ExamplePostService(final ExamplePostRepository examplePostRepository) {
        this.examplePostRepository = examplePostRepository;
    }

    @Transactional
    public CreatePostResponse create(final CreatePostRequest request) {
        final ExamplePost examplePost = new ExamplePost(request.title(), request.contents());
        return CreatePostResponse.from(examplePostRepository.save(examplePost));
    }
}
