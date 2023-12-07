package nora.movlog.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nora.movlog.domain.movie.Movie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static nora.movlog.utils.constant.StringConstant.*;

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
    private String memberAuth;
    private LocalDateTime createdAt;

    /* 연관관계 */
    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<Likes> likes;

    @OneToMany(mappedBy = "to", orphanRemoval = true)
    private List<Notification> notifications;


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
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.getMemberAuth()));
        return authorities;
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

    public void setVerified(Authentication auth) {
        this.memberAuth = AUTH_VERIFIED;
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>();
        updatedAuthorities.add(new SimpleGrantedAuthority(this.getMemberAuth()));
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}



