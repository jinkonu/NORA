package nora.movlog.service.user;

import nora.movlog.domain.user.Notification;
import nora.movlog.domain.user.Member;
import nora.movlog.repository.user.NotificationRepository;
import nora.movlog.service.movie.MovieService;
import nora.movlog.utils.dto.user.MemberEditDto;
import nora.movlog.utils.dto.user.MemberJoinRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static nora.movlog.utils.constant.StringConstant.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("ALL")
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberServiceTest {

    private static final String TEST_CASE_MEMBER2_LOGIN_ID = "hello";
    private static final String TEST_CASE_MEMBER2_PASSWORD = "bye";
    private static final String TEST_CASE_MEMBER2_NICKNAME = "beachboys";
    private static final String TEST_CASE_MOVIE_ID = "275";

    @Autowired
    MemberService memberService;

    @Autowired
    MovieService movieService;

    @Autowired
    NotificationRepository notificationRepository;


    @Autowired
    BCryptPasswordEncoder encoder;

    @BeforeEach
    void init() {
        generateMember(
                TEST_CASE_MEMBER_LOGIN_ID,
                TEST_CASE_MEMBER_PASSWORD,
                TEST_CASE_MEMBER_NICKNAME);
    }

    @DisplayName("회원가입 요청 dto로부터 회원 등록")
    @Rollback
    @Test
    void join_회원가입_요청_dto로부터_회원_등록() {
        MemberJoinRequestDto dto = new MemberJoinRequestDto();
        dto.setLoginId(TEST_CASE_MEMBER2_LOGIN_ID);
        dto.setPassword(TEST_CASE_MEMBER2_PASSWORD);
        dto.setPasswordCheck(TEST_CASE_MEMBER2_PASSWORD);
        dto.setNickname(TEST_CASE_MEMBER2_NICKNAME);

        memberService.join(dto);
    }


    @DisplayName("다른 회원을 팔로우에 추가")
    @Test
    void follow_다른_회원을_팔로우에_추가() {
        generateMember(
                TEST_CASE_MEMBER2_LOGIN_ID,
                TEST_CASE_MEMBER2_PASSWORD,
                TEST_CASE_MEMBER2_NICKNAME
        );

        Member following = memberService.findByLoginId(TEST_CASE_MEMBER_LOGIN_ID);
        Member follower = memberService.findByLoginId(TEST_CASE_MEMBER2_LOGIN_ID);
        memberService.follow(following.getLoginId(), follower.getLoginId());

        assertThat(memberService.findAllFollowings(following.getLoginId()).contains(follower)).isTrue();
        assertThat(memberService.findAllFollowers(follower.getLoginId()).contains(following)).isTrue();
        assertThat(notificationRepository.findAllByType(Notification.Type.FOLLOW).size()).isGreaterThan(0);
    }


    @DisplayName("다른 회원을 팔로우에서 제거")
    @Test
    void unfollow_다른_회원을_팔로우에서_제거() {
        generateMember(
                TEST_CASE_MEMBER2_LOGIN_ID,
                TEST_CASE_MEMBER2_PASSWORD,
                TEST_CASE_MEMBER2_NICKNAME
        );

        Member following = memberService.findByLoginId(TEST_CASE_MEMBER_LOGIN_ID);
        Member follower = memberService.findByLoginId(TEST_CASE_MEMBER2_LOGIN_ID);
        memberService.follow(following.getLoginId(), follower.getLoginId());

        memberService.unfollow(following.getLoginId(), follower.getLoginId());

        assertThat(memberService.findAllFollowings(following.getLoginId()).isEmpty()).isTrue();
        assertThat(memberService.findAllFollowers(follower.getLoginId()).isEmpty()).isTrue();
    }


    @DisplayName("이미 본 영화에 추가")
    @Test
    void addSeen_이미_본_영화에_추가() {
        Member member = memberService.findByLoginId(TEST_CASE_MEMBER_LOGIN_ID);

        memberService.addSeenMovie(member.getLoginId(), TEST_CASE_MOVIE_ID);

        assertThat(memberService.findAllSeenMovies(member.getLoginId())
                .contains(movieService.findOne(TEST_CASE_MOVIE_ID)))
                .isTrue();
    }


    @DisplayName("이미 본 영화에서 제거")
    @Test
    void removeSeenMovie_이미_본_영화에서_제거() {
        Member member = memberService.findByLoginId(TEST_CASE_MEMBER_LOGIN_ID);
        memberService.addSeenMovie(member.getLoginId(), TEST_CASE_MOVIE_ID);

        memberService.removeSeenMovie(member.getLoginId(), TEST_CASE_MOVIE_ID);

        assertThat(memberService.findAllSeenMovies(member.getLoginId())
                .contains(movieService.findOne(TEST_CASE_MOVIE_ID)))
                .isFalse();
    }


    @DisplayName("보고 싶은 영화에 추가")
    @Test
    void addToSee_보고_싶은_영화에_추가() {
        Member member = memberService.findByLoginId(TEST_CASE_MEMBER_LOGIN_ID);

        memberService.addToSeeMovie(member.getLoginId(), TEST_CASE_MOVIE_ID);

        assertThat(memberService.findAllToSeeMovies(member.getLoginId()))
                .contains(movieService.findOne(TEST_CASE_MOVIE_ID));
    }


    @DisplayName("보고 싶은 영화에서 제거")
    @Test
    void removeToSee_보고_싶은_영화에서_제거() {
        Member member = memberService.findByLoginId(TEST_CASE_MEMBER_LOGIN_ID);
        memberService.addSeenMovie(member.getLoginId(), TEST_CASE_MOVIE_ID);

        memberService.removeToSeeMovie(member.getLoginId(), TEST_CASE_MOVIE_ID);

        assertThat(memberService.findAllToSeeMovies(member.getLoginId())
                .contains(movieService.findOne(TEST_CASE_MOVIE_ID)))
                .isFalse();
    }



    @DisplayName("id로부터 회원 조회")
    @Test
    void profile_id로부터_회원_조회() {
        Member memberByLoginId = memberService.findByLoginId(TEST_CASE_MEMBER_LOGIN_ID);
        Member memberById = memberService.profile(memberByLoginId.getId());

        assertThat(memberById.getId()).isEqualTo(memberByLoginId.getId());
        assertThat(memberById.getLoginId()).isEqualTo(TEST_CASE_MEMBER_LOGIN_ID);
        assertThat(memberById.getNickname()).isEqualTo(TEST_CASE_MEMBER_NICKNAME);
    }


    @DisplayName("login id로부터 회원 조회")
    @Test
    void findByLoginId_login_id로부터_회원_조회() {
        Member member = memberService.findByLoginId(TEST_CASE_MEMBER_LOGIN_ID);

        assertThat(member.getLoginId()).isEqualTo(TEST_CASE_MEMBER_LOGIN_ID);
        assertThat(member.getNickname()).isEqualTo(TEST_CASE_MEMBER_NICKNAME);
    }


    @DisplayName("닉네임으로부터 회원 페이지 조회")
    @Test
    void findAllByNickname_닉네임으로부터_회원_페이지_조회() {
        List<Member> members = memberService.findAllByNickname(TEST_CASE_MEMBER_NICKNAME, 0, 10);

        assertThat(members.stream()
                .map(Member::getNickname)
                .anyMatch(name -> name.equals(TEST_CASE_MEMBER_NICKNAME))).isTrue();
    }


    @DisplayName("MemberDto와 id로부터 회원 비밀번호 수정")
    @ValueSource(strings = "pinkFloyd")
    @ParameterizedTest
    void edit_MemberDto와_id로부터_회원_비밀번호_수정(String input) {
        String newPassword = input;

        MemberEditDto dto = MemberEditDto.builder()
                .newNickname(input)
                .nowPassword(TEST_CASE_MEMBER_PASSWORD)
                .newPassword(newPassword)
                .newPasswordCheck(newPassword)
                .build();

        memberService.edit(TEST_CASE_MEMBER_LOGIN_ID, dto);
        Member member = memberService.findByLoginId(TEST_CASE_MEMBER_LOGIN_ID);

//        assertThat(member.getNickname()).isEqualTo(input);
        assertThat(encoder.matches(newPassword, member.getPassword())).isTrue();
    }


    @DisplayName("id와 현재 비밀번호로부터 회원 삭제")
    @Test
    void delete_id와_현재_비밀번호로부터_회원_삭제() {
        memberService.delete(TEST_CASE_MEMBER_LOGIN_ID);
        assertThatThrownBy(() ->
                memberService.findByLoginId(TEST_CASE_MEMBER_LOGIN_ID))
                .isInstanceOf(NoSuchElementException.class);
    }


    private void generateMember(String loginId, String password, String nickname) {
        MemberJoinRequestDto dto = new MemberJoinRequestDto();
        dto.setLoginId(loginId);
        dto.setPassword(password);
        dto.setPasswordCheck(password);
        dto.setNickname(nickname);

        memberService.join(dto);
    }
}