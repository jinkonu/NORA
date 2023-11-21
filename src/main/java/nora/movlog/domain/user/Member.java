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
public class Member {
    /* DB id */
    @Id @GeneratedValue
    private long id;

    /* 회원 데이터 */
    private String loginId;
    private String password;
    private String nickname;
    private LocalDateTime createdAt;

    /* 연관관계 */
    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<Likes> likes;

    /* 도메인 로직 */
    public void edit(String password, String nickname) {
        this.password = password;
        this.nickname = nickname;
    }
}



