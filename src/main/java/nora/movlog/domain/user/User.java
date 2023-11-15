package nora.movlog.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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

    /* 연관관계 */
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Likes> likes;
}


