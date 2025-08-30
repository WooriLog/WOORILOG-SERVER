package dev.woori.wooriLog.domain.member.service;

import dev.woori.wooriLog.domain.blog.entity.Blog;
import dev.woori.wooriLog.domain.blog.repository.BlogRepository;
import dev.woori.wooriLog.domain.member.dto.MemberInfoDto;
import dev.woori.wooriLog.domain.member.dto.ProfileDto;
import dev.woori.wooriLog.domain.member.entity.Member;
import dev.woori.wooriLog.domain.member.repository.MemberRepository;
import dev.woori.wooriLog.domain.project.entity.Project;
import dev.woori.wooriLog.domain.project.repository.ProjectMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BlogRepository blogRepository;
    private final ProjectMemberRepository projectMemberRepository;

    /**
     * 파라미터로 넘어온 email을 포함하는 이메일을 가진 유저들을 반환
     * @param email 찾고자 하는 유저의 부분 email 문자열
     * @return List<MemberInfoDto> 해당하는 회원들의 정보를 담은 DTO
     */
    public List<MemberInfoDto> findMembersByEmail(String email) {
        List<Member> members = memberRepository.findAllByEmailContaining(email);
        return members.stream().map(MemberInfoDto::create).toList();
    }

    /**
     * 파라미터로 넘어온 id를 갖는 회원을 반환
     * @param userId 회원 id
     * @return MemberDTO 회원 정보를 담은 객체
     */
    public MemberInfoDto findMemberById(Long userId) {
        Member member = memberRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        return  MemberInfoDto.create(member);
    }

    /**
     * 파라미터로 넘어온 userId를 통해 프로필 페이지에 필요한 정보를 조회
     * @param userId 회원 id
     * @return ProfileDto 회원 정보 + 간략한 블로그 정보 + 간략한 프로젝트 정보를 담은 객체
     */
    public ProfileDto findProfileInfoById(Long userId) {
        Member member = memberRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        List<Blog> blogs = blogRepository.findByMemberId(userId);
        List<Project> projects = projectMemberRepository.findByMemberId(userId);
        return ProfileDto.create(member, blogs, projects);
    }
}
