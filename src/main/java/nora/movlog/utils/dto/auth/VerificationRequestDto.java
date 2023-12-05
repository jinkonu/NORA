package nora.movlog.utils.dto.auth;

import lombok.Data;

@Data
public class VerificationRequestDto {
    private String verifyCode;
}
