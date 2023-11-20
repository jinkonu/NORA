package nora.movlog.dto.user;

import lombok.Builder;
import lombok.Getter;
import nora.movlog.domain.user.User;

@Getter
@Builder
public class UserDto {
    private String loginId;
    private String nickname;
    private String nowPassword;
    private String newPassword;
    private String newPasswordCheck;

    public static UserDto of(User user) {
        return UserDto.builder()
                .loginId(user.getLoginId())
                .nickname(user.getNickname())
                .build();
    }
}
