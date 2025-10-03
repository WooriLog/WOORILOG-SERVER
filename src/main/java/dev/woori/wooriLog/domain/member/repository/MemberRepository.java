package dev.woori.wooriLog.domain.member.repository;

import dev.woori.wooriLog.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByProviderAndSocialId(String provider, String socialId);

    Optional<Member> findById(Long id);

    List<Member> findAllByEmailContainingAndIdNot(String email, Long id);
    
    boolean existsMemberByEmail(String email);

    boolean existsMemberByProviderAndSocialId(String provider, String socialId);
}
