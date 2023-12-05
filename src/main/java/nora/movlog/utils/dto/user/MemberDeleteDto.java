package nora.movlog.utils.dto.user;

import lombok.Data;

@Data
public class MemberDeleteDto {
    private String password;
    private String passwordCheck;
}
