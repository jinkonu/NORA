package nora.movlog.service.user;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.user.Member;
import nora.movlog.service.exception.BusinessLogicException;
import nora.movlog.service.exception.ExceptionCode;
import nora.movlog.utils.dto.user.MemberDto;
import nora.movlog.utils.dto.user.MemberJoinRequestDto;
import nora.movlog.repository.user.CommentRepository;
import nora.movlog.repository.user.LikesRepository;
import nora.movlog.repository.user.MemberRepository;
import nora.movlog.utils.dto.user.MemberLoginRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static nora.movlog.utils.constant.StringConstant.*;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;
    private final BCryptPasswordEncoder encoder;

    private final MailService mailService;
    private final RedisService redisService;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    /* CREATE */
    @Transactional
    public void join(MemberJoinRequestDto requestDto) {
        memberRepository.save(requestDto.toEntity(encoder.encode(requestDto.getPassword())));
    }

    public void sendCodeToEmail(String email) {
        String title = "MOVLOG 이메일 인증";
        String authCode = this.createCode();
        mailService.sendEmail(email, title, authCode);

        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
        redisService.setValues(AUTH_CODE_PREFIX + email, authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessLogicException(ExceptionCode.NO_SUCH_ALGORITHM);
        }
    }

    public boolean verifiedCode(String email, String authCode) {
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
        return redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
    }

    public void follow(long followingId, long followerId) {
        Member following = memberRepository.findById(followingId).get();
        Member follower = memberRepository.findById(followerId).get();

        following.follows(follower);
    }


    /* READ */
    public Member profile(long id) {
        return memberRepository.findById(id).get();
    }

    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId).get();
    }

    public List<Member> findAllByNickname(String query, int page, int size) {
        return memberRepository.findAllByNicknameContains(query, PageRequest.of(page, size)).stream()
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


    @Transactional
    public Member login(MemberLoginRequestDto dto) {
        Optional<Member> optionalMember = memberRepository.findByLoginId(dto.getLoginId());

        if (optionalMember.isEmpty())
            return null;

        Member member = optionalMember.get();

        if (!member.getPassword().equals(dto.getPassword()))
            return null;

        return member;
    }


    /* UPDATE */
    @Transactional
    public void edit(long id, MemberDto dto) {
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
