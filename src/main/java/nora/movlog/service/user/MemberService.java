package nora.movlog.service.user;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.movie.Movie;
import nora.movlog.domain.user.Comment;
import nora.movlog.domain.user.Likes;
import nora.movlog.domain.user.Member;
import nora.movlog.repository.movie.interfaces.MovieRepository;
import nora.movlog.repository.user.CommentRepository;
import nora.movlog.repository.user.LikesRepository;
import nora.movlog.utils.dto.user.MemberEditDto;
import nora.movlog.utils.dto.user.MemberJoinRequestDto;
import nora.movlog.repository.user.MemberRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static nora.movlog.utils.constant.StringConstant.*;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final BCryptPasswordEncoder encoder;

    /* CREATE */
    @Transactional
    public void join(MemberJoinRequestDto requestDto) {
        memberRepository.save(requestDto.toEntity(encoder.encode(requestDto.getPassword())));
    }

    @Transactional
    public Map<String, Member> follow(String followingId, String followerId) {
        Member following = memberRepository.findByLoginId(followingId).get();
        Member follower = memberRepository.findByLoginId(followerId).get();

        following.follows(follower);

        return new HashMap<>(Map.of(
                FOLLOWING, following,
                FOLLOWER, follower
        ));
    }

    @Transactional
    public void addSeenMovie(String loginId, String movieId) {
        Member member = memberRepository.findByLoginId(loginId).get();
        Movie movie = movieRepository.findById(movieId).get();

        member.addSeen(movie);
    }

    @Transactional
    public void addToSeeMovie(String loginId, String movieId) {
        Member member = memberRepository.findByLoginId(loginId).get();
        Movie movie = movieRepository.findById(movieId).get();

        member.addToSee(movie);
    }


    /* READ */
    public Member profile(long id) {
        return memberRepository.findById(id).get();
    }

    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId).get();
    }

    public List<Member> findAllByNickname(String query, int page, int size) {
        return memberRepository.findAllByNicknameContains(query, PageRequest.of(page, size)).stream()
                .toList();
    }

    public Set<Member> findAllFollowings(String loginId) {
        return memberRepository.findByLoginId(loginId).get()
                .getFollowings();
    }

    public Set<Member> findAllFollowers(String loginId) {
        return memberRepository.findByLoginId(loginId).get()
                .getFollowers();
    }

    public Set<Movie> findAllSeenMovies(String loginId) {
        return memberRepository.findByLoginId(loginId).get()
                .getSeenMovies();
    }

    public Set<Movie> findAllToSeeMovies(String loginId) {
        return memberRepository.findByLoginId(loginId).get()
                .getToSeeMovies();
    }

    public boolean isFollowing(String followingLoginId, String followerLoginId) {
        Member follower = memberRepository.findByLoginId(followerLoginId).get();
        Member following = memberRepository.findByLoginId(followingLoginId).get();

        return following.getFollowings().contains(follower);
    }


    /* UPDATE */
    @Transactional
    public void edit(String loginId, MemberEditDto dto) {
        Member member = memberRepository.findByLoginId(loginId).get();

        if (dto.getNewPassword() == null) // 닉네임 수정
            member.edit(member.getPassword(), dto.getNewNickname());
        else                              // 비밀번호 수정
            member.edit(encoder.encode(dto.getNewPassword()), member.getNickname());
    }



    /* DELETE */
    @Transactional
    public void delete(String loginId) {
        Member member = memberRepository.findByLoginId(loginId).get();

        List<Likes> likes = likesRepository.findAllByMember(member);
        for (Likes like : likes) {
            like.getPost().changeLike(like.getPost().getLikeCnt() - 1);
        }

        List<Comment> comments = commentRepository.findAllByMember(member);
        for (Comment comment : comments) {
            comment.getPost().changeComment(comment.getPost().getCommentCnt() - 1);
        }

        memberRepository.delete(member);
    }

    @Transactional
    public void unfollow(String followingId, String followerId) {
        Member following = memberRepository.findByLoginId(followingId).get();
        Member follower = memberRepository.findByLoginId(followerId).get();

        following.unfollows(follower);
    }

    @Transactional
    public void removeSeenMovie(String loginId, String movieId) {
        Member member = memberRepository.findByLoginId(loginId).get();
        Movie movie = movieRepository.findById(movieId).get();

        member.removeSeen(movie);
    }

    @Transactional
    public void removeToSeeMovie(String loginId, String movieId) {
        Member member = memberRepository.findByLoginId(loginId).get();
        Movie movie = movieRepository.findById(movieId).get();

        member.removeToSee(movie);
    }
}
