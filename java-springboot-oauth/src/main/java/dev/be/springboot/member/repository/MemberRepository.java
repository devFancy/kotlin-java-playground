package dev.be.springboot.member.repository;

import dev.be.springboot.member.domain.entity.Member;
import dev.be.springboot.member.exception.NotFoundMemberException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(final String email);

    boolean existsByEmail(final String email);

    default Member findMemberById(final Long id) {
        return findById(id)
                .orElseThrow(NotFoundMemberException::new);
    }

    default Member getByEmail(final String email) {
        return findByEmail(email)
                .orElseThrow(NotFoundMemberException::new);
    }

    default void validateExistById(final Long memberId) {
        if (!existsById(memberId)) {
            throw new NotFoundMemberException();
        }
    }
}
