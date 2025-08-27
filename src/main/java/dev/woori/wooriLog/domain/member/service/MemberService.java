package dev.woori.wooriLog.domain.member.service;

import dev.woori.wooriLog.domain.member.dto.MemberInfoDto;
import dev.woori.wooriLog.domain.member.entity.Member;
import dev.woori.wooriLog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 파라미터로 넘어온 email을 포함하는 이메일을 가진 유저들을 반환
     * @param email 찾고자 하는 유저의 부분 email 문자열
     * @return List<MemberInfoDto> 해당하는 회원들의 정보를 담은 DTO
     */
    @Transactional
    public List<MemberInfoDto> findMembersByEmail(String email) {
        List<Member> members = memberRepository.findAllByEmailContaining(email);
        return members.stream().map(MemberInfoDto::create).toList();
    }
}
