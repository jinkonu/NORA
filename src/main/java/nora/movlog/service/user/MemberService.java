package nora.movlog.service.user;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.movie.Movie;
import nora.movlog.domain.user.Member;
import nora.movlog.repository.movie.interfaces.MovieRepository;
import nora.movlog.service.exception.BusinessLogicException;
import nora.movlog.service.exception.ExceptionCode;
import nora.movlog.utils.dto.user.MemberDto;
import nora.movlog.utils.dto.user.MemberJoinRequestDto;
import nora.movlog.repository.user.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static nora.movlog.utils.constant.StringConstant.*;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;
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

    public void sendCodeToEmail(String loginId) {
        String title = "MOVLOG 이메일 인증";
        String authCode = this.createCode();
        mailService.sendEmail(loginId, title, authCode);

        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
        redisService.setValues(AUTH_CODE_PREFIX + loginId, authCode, Duration.ofMillis(this.authCodeExpirationMillis));
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

    @Transactional
    public boolean verifiedCode(String loginId, String authCode) {
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + loginId);
        boolean verified = redisAuthCode.equals(authCode);
        if (verified) {
            Member member = memberRepository.findByLoginId(loginId).get();
            member.setVerified();
        }
        return verified;
    }

    @Transactional
    public void follow(String followingId, String followerId) {
        Member following = memberRepository.findByLoginId(followingId).get();
        Member follower = memberRepository.findByLoginId(followerId).get();

        following.follows(follower);
    }

    @Transactional
    public void addSeenMovie(String loginId, String movieId) {
        Member member = memberRepository.findByLoginId(loginId).get();
        Movie movie = movieRepository.findById(movieId).get();

        member.addSeen(movie);
    }

    @Transactional
    public void addToSeeMovie(String loginId, String movieId) {
        Member member = memberRepository.findByLoginId(loginId).get();
        Movie movie = movieRepository.findById(movieId).get();

        member.addToSee(movie);
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

    public Set<Member> findAllFollowings(String loginId) {
        return memberRepository.findByLoginId(loginId).get()
                .getFollowings();
    }

    public Set<Member> findAllFollowers(String loginId) {
        return memberRepository.findByLoginId(loginId).get()
                .getFollowers();
    }

    public Set<Movie> findAllSeenMovies(String loginId) {
        return memberRepository.findByLoginId(loginId).get()
                .getSeenMovies();
    }

    public Set<Movie> findAllToSeeMovies(String loginId) {
        return memberRepository.findByLoginId(loginId).get()
                .getToSeeMovies();
    }


    /* UPDATE */
    @Transactional
    public void edit(String loginId, MemberDto dto) {
        Member member = memberRepository.findByLoginId(loginId).get();

        if (dto.getNewPassword().isBlank())
            member.edit(member.getPassword(), dto.getNickname());
        else
            member.edit(encoder.encode(dto.getNewPassword()), dto.getNickname());
    }



    /* DELETE */
    @Transactional
    public boolean delete(String loginId, String nowPassword) {
        Member member = memberRepository.findByLoginId(loginId).get();

        if (encoder.matches(nowPassword, member.getPassword())) {
            memberRepository.delete(member);
            return true;
        }

        return false;
    }

    @Transactional
    public void unfollow(String followingId, String followerId) {
        Member following = memberRepository.findByLoginId(followingId).get();
        Member follower = memberRepository.findByLoginId(followerId).get();

        following.unfollows(follower);
    }

    @Transactional
    public void removeSeenMovie(String loginId, String movieId) {
        Member member = memberRepository.findByLoginId(loginId).get();
        Movie movie = movieRepository.findById(movieId).get();

        member.removeSeen(movie);
    }

    @Transactional
    public void removeToSeeMovie(String loginId, String movieId) {
        Member member = memberRepository.findByLoginId(loginId).get();
        Movie movie = movieRepository.findById(movieId).get();

        member.removeToSee(movie);
    }
}
