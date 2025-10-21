package dev.woori.woorilog.domain.member.service;

import dev.woori.woorilog.domain.blog.entity.Blog;
import dev.woori.woorilog.domain.blog.repository.BlogRepository;
import dev.woori.woorilog.domain.member.dto.MemberInfoDto;
import dev.woori.woorilog.domain.member.dto.MemberUpdateReq;
import dev.woori.woorilog.domain.member.dto.ProfileDto;
import dev.woori.woorilog.domain.member.entity.Member;
import dev.woori.woorilog.domain.member.repository.MemberRepository;
import dev.woori.woorilog.domain.project.entity.Project;
import dev.woori.woorilog.domain.project.repository.ProjectMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.woori.woorilog.global.response.error.ErrorMessage.USER_NOT_FOUND;

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
     * @param memberId 요청한 클라이언트 id
     * @return List<MemberInfoDto> 해당하는 회원들의 정보를 담은 DTO
     */
    public List<MemberInfoDto> findMembersByEmail(String email, Long memberId) {
        List<Member> members = memberRepository.findAllByEmailContainingAndIdNot(email, memberId);
        return members.stream().map(MemberInfoDto::create).toList();
    }

    /**
     * 파라미터로 넘어온 id를 갖는 회원을 반환
     * @param userId 회원 id
     * @return MemberDTO 회원 정보를 담은 객체
     */
    public MemberInfoDto getMemberInfo(Long userId) {
        Member member = findMemberByIdOrThrow(userId);
        return  MemberInfoDto.create(member);
    }

    /**
     * 파라미터로 넘어온 userId를 통해 프로필 페이지에 필요한 정보를 조회
     * @param userId 회원 id
     * @return ProfileDto 회원 정보 + 간략한 블로그 정보 + 간략한 프로젝트 정보를 담은 객체
     */
    public ProfileDto getProfileInfoById(Long userId) {
        Member member = findMemberByIdOrThrow(userId);
        List<Blog> blogs = blogRepository.findByMemberId(userId);
        List<Project> projects = projectMemberRepository.findByMemberId(userId);
        return ProfileDto.create(member, blogs, projects);
    }

    /**
     * 회원 id 및 request 정보를 바탕으로 회원 정보를 수정
     * @param userId 회원 id
     * @param request 수정될 회원 정보를 담은 request 객체
     * @return MemberInfoDto 수정된 회원 정보를 담은 dto 객체
     */
    @Transactional
    public MemberInfoDto updateMemberInfo(Long userId, MemberUpdateReq request) {
        Member member = findMemberByIdOrThrow(userId);
        member.update(request);
        return MemberInfoDto.create(member);
    }

    private Member findMemberByIdOrThrow(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    }
}
