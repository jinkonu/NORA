package nora.movlog.service.user;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.user.Member;
import nora.movlog.utils.dto.user.MemberDto;
import nora.movlog.utils.dto.user.MemberJoinRequestDto;
import nora.movlog.utils.dto.user.MemberLoginRequestDto;
import nora.movlog.repository.user.CommentRepository;
import nora.movlog.repository.user.LikesRepository;
import nora.movlog.repository.user.MemberRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;
import java.util.Set;

import static nora.movlog.domain.constant.StringConstant.*;
import static nora.movlog.domain.constant.NumberConstant.*;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;
    private final BCryptPasswordEncoder encoder;

    /* CREATE */

    @Transactional
    public void join(MemberJoinRequestDto requestDto) {
        memberRepository.save(requestDto.toEntity(encoder.encode(requestDto.getPassword())));
    }

    @Transactional
    public Member login(MemberLoginRequestDto memberLoginRequestDto) {
        Optional<Member> optionalMember = memberRepository.findByLoginId(memberLoginRequestDto.getLoginId());
        if(optionalMember.isEmpty()) {
            return null;
        }

        Member member = optionalMember.get();
        if(!member.getPassword().equals(memberLoginRequestDto.getPassword())) {
            return null;
        }

        return member;
    }

    public void follow(long followingId, long followerId) {
        Member following = memberRepository.findById(followingId).get();
        Member follower = memberRepository.findById(followerId).get();

        following.follow(follower);
    }

    /* READ */

    public Member profile(long id) {
        return memberRepository.findById(id).get();
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).get();
    }

    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId).get();
    }

    public List<Member> findAllByNickname(String query, PageRequest pageRequest) {
        return memberRepository.findAllByNicknameContains(query, pageRequest).stream()
                .toList();
    }

    public Set<Member> findAllFollowings(long id) {
        return memberRepository.findById(id).get()
                .getFollowings();
    }

    public Set<Member> findAllFollowers(long id) {
        return memberRepository.findById(id).get()
                .getFollowers();
    }



    /* UPDATE */

    @Transactional
    public void edit(MemberDto dto, long id) {
        Member member = memberRepository.findById(id).get();

        if (dto.getNewPassword().isBlank())
            member.edit(member.getPassword(), dto.getNickname());
        else
            member.edit(encoder.encode(dto.getNewPassword()), dto.getNickname());
    }



    /* DELETE */

    @Transactional
    public boolean delete(long id, String nowPassword) {
        Member member = memberRepository.findById(id).get();

        if (encoder.matches(nowPassword, member.getPassword())) {
            memberRepository.delete(member);
            return true;
        }

        return false;
    }
}
