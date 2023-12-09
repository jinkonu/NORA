package nora.movlog.utils.dto.user;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
public class MemberEditDto {
    private String nowPassword;
    private String newPassword;
    private String newPasswordCheck;
    private String newNickname;
    private MultipartFile image;
}
