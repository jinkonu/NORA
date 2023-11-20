package nora.movlog.dto.user;

import lombok.Builder;
import lombok.Getter;
import nora.movlog.domain.user.Member;

@Getter
@Builder
public class MemberDto {
    private String loginId;
    private String nickname;
    private String nowPassword;
    private String newPassword;
    private String newPasswordCheck;

    public static MemberDto of(Member member) {
        return MemberDto.builder()
                .loginId(member.getLoginId())
                .nickname(member.getNickname())
                .build();
    }
}
