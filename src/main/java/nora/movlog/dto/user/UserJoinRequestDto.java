package nora.movlog.dto.user;

import lombok.Getter;
import nora.movlog.domain.user.User;

import java.time.LocalDateTime;

@Getter
public class UserJoinRequestDto {
    private String loginId;
    private String password;
    private String passwordCheck;
    private String nickname;

    public User toEntity(String encodedPassword) {
        return User.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .nickname(nickname)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
