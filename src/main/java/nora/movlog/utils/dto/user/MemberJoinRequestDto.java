package nora.movlog.utils.dto.user;

import lombok.Data;
import nora.movlog.domain.user.Member;

import java.time.LocalDateTime;
import java.util.HashSet;

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
                .isVerified(false)
                .createdAt(LocalDateTime.now())
                .followings(new HashSet<>())
                .followers(new HashSet<>())
                .seenMovies(new HashSet<>())
                .toSeeMovies(new HashSet<>())
                .build();
    }
}
