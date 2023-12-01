package nora.movlog.utils.dto.user;

import lombok.Data;
import nora.movlog.domain.user.Member;

import java.time.LocalDateTime;
import java.util.HashSet;

import static nora.movlog.utils.constant.StringConstant.*;

@Data
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
                .memberAuth(AUTH_UNVERIFIED)
                .followings(new HashSet<>())
                .followers(new HashSet<>())
                .seenMovies(new HashSet<>())
                .toSeeMovies(new HashSet<>())
                .build();
    }
}
