package nora.movlog.service.user;

import nora.movlog.dto.user.MemberJoinRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @DisplayName("회원가입 요청 dto로부터 회원 등록")
    @Rollback(value = false)
    @Test
    void join_회원가입_요청_dto로부터_회원_등록() {
        MemberJoinRequestDto dto = new MemberJoinRequestDto();
        dto.setLoginId("hello");
        dto.setPassword("bye");
        dto.setPasswordCheck("bye");
        dto.setNickname("beatles");

        memberService.join(dto);
    }
}