package nora.movlog.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class User {
    /* DB id */
    @Id @GeneratedValue
    private Long id;

    /* 회원 데이터 */
    private String loginId;
    private String password;
    private String nickname;
    private LocalDateTime createdAt;
}



