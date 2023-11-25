package nora.movlog.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(
            name = "follow",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private Set<Member> followings;
    private int followingCnt;

    @ManyToMany(mappedBy = "followings")
    private Set<Member> followers;
    private int followerCnt;

    /* 도메인 로직 */
    public void edit(String password, String nickname) {
        this.password = password;
        this.nickname = nickname;
    }

    public void follow(Member follower) {
        this.followingCnt++;
        this.followings.add(follower);

        follower.followerCnt++;
    }
}



