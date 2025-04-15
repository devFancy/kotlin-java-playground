package dev.be.core.api.infrastructure;

import dev.be.core.api.domain.ExamplePost;
import dev.be.core.api.domain.ExamplePostRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaExamplePostRepository extends ExamplePostRepository, JpaRepository<ExamplePost, UUID> {
}
