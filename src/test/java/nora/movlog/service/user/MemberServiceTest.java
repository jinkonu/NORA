package nora.movlog.service.user;

import nora.movlog.domain.user.Member;
import nora.movlog.dto.user.MemberDto;
import nora.movlog.dto.user.MemberJoinRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ALL")
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberServiceTest {

    private static final String loginId = "hello3";
    private static final String nickname = "beatles";
    private static final String password = "bye";

    private static final long id_402 = 402;
    private static final String loginId_402 = "hello";
    private static final String nickname_402 = "beatles";
    private static final String password_402 = "bye";

    @Autowired
    MemberService memberService;

    @Autowired
    BCryptPasswordEncoder encoder;

    @BeforeEach
    void init() {
        MemberJoinRequestDto dto = new MemberJoinRequestDto();
        dto.setLoginId(loginId);
        dto.setPassword(password);
        dto.setPasswordCheck(password);
        dto.setNickname(nickname);

        memberService.join(dto);
    }

    @DisplayName("회원가입 요청 dto로부터 회원 등록")
    @Rollback()
    @Test
    void join_회원가입_요청_dto로부터_회원_등록() {
        MemberJoinRequestDto dto = new MemberJoinRequestDto();
        dto.setLoginId("hello2");
        dto.setPassword("bye");
        dto.setPasswordCheck("bye");
        dto.setNickname("beatles");

        memberService.join(dto);
    }


    @DisplayName("id로부터 회원 조회")
    @Test
    void profile_id로부터_회원_조회() {
        Member member = memberService.profile(id_402);

        assertThat(member.getId()).isEqualTo(id_402);
        assertThat(member.getLoginId()).isEqualTo(loginId_402);
        assertThat(member.getNickname()).isEqualTo(nickname_402);
    }


    @DisplayName("login id로부터 회원 조회")
    @ValueSource(strings = "hello3")
    @ParameterizedTest
    void findByLoginId_login_id로부터_회원_조회(String input) {
        Member member = memberService.findByLoginId(input);

        assertThat(member.getLoginId()).isEqualTo(loginId);
        assertThat(member.getNickname()).isEqualTo(nickname);
    }


    @DisplayName("닉네임으로부터 회원 페이지 조회")
    @ValueSource(strings = "beatles")
    @ParameterizedTest
    void findAllByNickname_닉네임으로부터_회원_페이지_조회(String input) {
        Page<Member> members = memberService.findAllByNickname(input, PageRequest.of(0, 10));

        assertThat(members.stream()
                .map(Member::getNickname)
                .anyMatch(name -> name.equals(nickname))).isTrue();
    }


    @DisplayName("MemberDto와 id로부터 회원 수정")
    @ValueSource(strings = "pinkFloyd")
    @ParameterizedTest
    void edit_MemberDto와_id로부터_회원_수정(String input) {
        String newPassword = password + " paul";

        MemberDto dto = MemberDto.builder()
                .loginId(loginId_402)
                .nickname(input)
                .nowPassword(password_402)
                .newPassword(newPassword)
                .newPasswordCheck(newPassword)
                .build();

        memberService.edit(dto, id_402);
        Member member = memberService.profile(id_402);

        assertThat(member.getNickname()).isEqualTo(input);
        assertThat(encoder.matches(newPassword, member.getPassword())).isTrue();
    }
}