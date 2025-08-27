package dev.woori.wooriLog.domain.member.repository;

import dev.woori.wooriLog.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByProviderAndSocialId(String provider, String socialId);

    Optional<Member> findByIdAndEmail(Long id, String email);
    
    List<Member> findAllByEmailContaining(String email);

    boolean existsMemberByEmail(String email);
}
