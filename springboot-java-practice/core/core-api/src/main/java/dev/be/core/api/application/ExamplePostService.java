package dev.be.core.api.application;

import dev.be.core.api.presentation.v1.dto.CreatePostRequest;
import dev.be.core.api.presentation.v1.dto.CreatePostResponse;
import dev.be.core.api.domain.ExamplePost;
import dev.be.core.api.domain.ExamplePostRepository;
import org.springframework.stereotype.Service;

/**
 *  현재는 규모가 크지 않기 때문에 단일 클래스로 유지하지만,
 *  추후 기능이 많아질 경우 Reader, Writer 등 역할 기반 구현체로 분리할 수 있습니다.
 *
 *  Transactional 이 필요한 곳에서만 사용합니다.
 */
@Service
public class ExamplePostService {

    private final ExamplePostRepository examplePostRepository;

    public ExamplePostService(final ExamplePostRepository examplePostRepository) {
        this.examplePostRepository = examplePostRepository;
    }

    public CreatePostResponse create(final CreatePostRequest request) {
        final ExamplePost examplePost = new ExamplePost(request.title(), request.contents());
        return CreatePostResponse.from(examplePostRepository.save(examplePost));
    }
}
