package nora.movlog.service.user;

import nora.movlog.domain.user.Member;
import nora.movlog.utils.dto.user.MemberDto;
import nora.movlog.utils.dto.user.MemberJoinRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static nora.movlog.domain.constant.StringConstant.*;

@SuppressWarnings("ALL")
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberServiceTest {

    private static final String TEST_CASE_MEMBER2_LOGIN_ID = "hello";
    private static final String TEST_CASE_MEMBER2_PASSWORD = "bye";
    private static final String TEST_CASE_MEMBER2_NICKNAME = "beachboys";

    @Autowired
    MemberService memberService;

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

        long followingId = memberService.findByLoginId(TEST_CASE_MEMBER_LOGIN_ID).getId();
        long followerId = memberService.findByLoginId(TEST_CASE_MEMBER2_LOGIN_ID).getId();
        memberService.follow(followingId, followerId);

        assertThat(memberService.findAllFollowings(followingId).contains(followerId));
        assertThat(memberService.findAllFollowers(followerId).contains(followingId));
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
        List<Member> members = memberService.findAllByNickname(TEST_CASE_MEMBER_NICKNAME, PageRequest.of(0, 10));

        assertThat(members.stream()
                .map(Member::getNickname)
                .anyMatch(name -> name.equals(TEST_CASE_MEMBER_NICKNAME))).isTrue();
    }


    @DisplayName("MemberDto와 id로부터 회원 수정")
    @ValueSource(strings = "pinkFloyd")
    @ParameterizedTest
    void edit_MemberDto와_id로부터_회원_수정(String input) {
        long id = memberService.findByLoginId(TEST_CASE_MEMBER_LOGIN_ID).getId();
        String newPassword = input;

        MemberDto dto = MemberDto.builder()
                .loginId(TEST_CASE_MEMBER_LOGIN_ID)
                .nickname(input)
                .nowPassword(TEST_CASE_MEMBER_PASSWORD)
                .newPassword(newPassword)
                .newPasswordCheck(newPassword)
                .build();

        memberService.edit(id, dto);
        Member member = memberService.profile(id);

        assertThat(member.getNickname()).isEqualTo(input);
        assertThat(encoder.matches(newPassword, member.getPassword())).isTrue();
    }


    @DisplayName("id와 현재 비밀번호로부터 회원 삭제")
    @Test
    void delete_id와_현재_비밀번호로부터_회원_삭제() {
        long id = memberService.findByLoginId(TEST_CASE_MEMBER_LOGIN_ID).getId();

        assertThat(memberService.delete(id, TEST_CASE_MEMBER_PASSWORD)).isTrue();
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