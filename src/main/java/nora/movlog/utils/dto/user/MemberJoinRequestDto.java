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
    private static final String defaultProfilePic = "https://movlog-bucket.s3.ap-northeast-2.amazonaws.com/3a8523f2-bd76-4be1-b2b7-8f2c592133ed.avif";

    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .nickname(nickname)
                .imageUrl(defaultProfilePic)
                .createdAt(LocalDateTime.now())
                .memberAuth(AUTH_UNVERIFIED)
                .followings(new HashSet<>())
                .followers(new HashSet<>())
                .seenMovies(new HashSet<>())
                .toSeeMovies(new HashSet<>())
                .build();
    }
}
