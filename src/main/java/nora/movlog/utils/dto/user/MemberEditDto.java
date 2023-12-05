package nora.movlog.utils.dto.user;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberEditDto {
    private String nowPassword;
    private String newPassword;
    private String newPasswordCheck;
    private String newNickname;

//    public static MemberDto of(Member member) {
//        return MemberDto.builder()
//                .loginId(member.getLoginId())
//                .nickname(member.getNickname())
//                .build();
//    }
}