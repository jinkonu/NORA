package nora.movlog.dto.user;

import lombok.Getter;
import nora.movlog.domain.user.Member;

import java.time.LocalDateTime;

@Getter
public class MemberJoinRequestDto {
    private String loginId;
    private String password;
    private String passwordCheck;
    private String nickname;

    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .nickname(nickname)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
