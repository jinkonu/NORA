package nora.movlog.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nora.movlog.domain.movie.Movie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Member implements UserDetails {
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


    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Movie> seenMovies;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Movie> toSeeMovies;



    /* 도메인 로직 */
    public void edit(String password, String nickname) {
        this.password = password;
        this.nickname = nickname;
    }

    public void follows(Member follower) {
        this.followingCnt++;
        this.followings.add(follower);

        follower.followerCnt++;
        follower.followers.add(this);
    }

    public void unfollows(Member follower) {
        this.followingCnt--;
        this.followings.remove(follower);

        follower.followerCnt--;
        follower.followers.remove(this);
    }

    public void addSeen(Movie movie) {
        this.seenMovies.add(movie);
    }

    public void removeSeen(Movie movie) {
        this.seenMovies.remove(movie);
    }

    public void addToSee(Movie movie) {
        this.toSeeMovies.add(movie);
    }

    public void removeToSee(Movie movie) {
        this.seenMovies.remove(movie);
    }


    /* 보안 관련 로직 */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}



