package nora.movlog.service.user;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.movie.Movie;
import nora.movlog.domain.user.Image;
import nora.movlog.domain.user.Member;
import nora.movlog.domain.user.Post;
import nora.movlog.utils.dto.user.PostCreateRequestDto;
import nora.movlog.utils.dto.user.PostDto;
import nora.movlog.repository.movie.interfaces.MovieRepository;
import nora.movlog.repository.user.PostRepository;
import nora.movlog.repository.user.MemberRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static nora.movlog.utils.constant.NumberConstant.*;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;
    private final ImageService imageService;

    private static final LocalDateTime homePostsDate = LocalDateTime.now().minusDays(MIN_LATEST_DAYS);


    /* CREATE */
    @Transactional
    public long write(PostCreateRequestDto dto, String userLoginId) throws IOException {
        Member member = memberRepository.findByLoginId(userLoginId).get();
        Movie movie = movieRepository.findById(dto.getMovieId()).get();

        Post post = postRepository.save(dto.toEntity(member, movie));

        if (dto.getImage() != null) {
            Image image = imageService.save(dto.getImage(), post);
            post.setImage(image);
        }

        return post.getId();
    }



    /* READ */
    public PostDto findOne(Long postId) {
        return postRepository.findById(postId)
                .map(PostDto::of)
                .orElseGet(() -> null);
    }

    public List<PostDto> findAllFromMember(long memberId, int page, int size) {
        return postRepository.findAllByMemberId(memberId, PageRequest.of(page, size)).stream()
                .map(PostDto::of)
                .sorted(PostDto::compareTo)
                .toList();
    }

    public List<PostDto> findAllFromMemberLoginId(String memberLoginId, int page, int size) {
        return postRepository.findAllByMemberLoginId(memberLoginId, PageRequest.of(page, size)).stream()
                .map(PostDto::of)
                .sorted(PostDto::compareTo)
                .toList();
    }

    public List<PostDto> findAllFromMovie(String movieId, int page, int size) {
        return postRepository.findAllByMovieId(movieId, PageRequest.of(page, size)).stream()
                .map(PostDto::of)
                .sorted(PostDto::compareTo)
                .toList();
    }

    public List<PostDto> findHomePosts(String memberLoginId) {
        List<PostDto> homePosts = new ArrayList<>();

        Set<Member> followings = memberRepository.findByLoginId(memberLoginId).get().getFollowings();
        followings.forEach(following ->
                homePosts.addAll(findLatestPostsFromFollowing(following.getLoginId()))
        );

        Collections.sort(homePosts);
        return homePosts;
    }

    private List<PostDto> findLatestPostsFromFollowing(String followingLoginId) {
        return postRepository.findAllByMemberLoginIdAndCreatedAtAfter(followingLoginId, PostService.homePostsDate).stream()
                .map(PostDto::of)
                .toList();
    }



    /* UPDATE */
    @Transactional
    public Long edit(long postId, PostDto dto) throws IOException {
        Optional<Post> optPost = postRepository.findById(postId);

        if (optPost.isEmpty()) return null;

        Post post = optPost.get();
        post.update(dto);

        if (dto.getNewImage() != null) {
            imageService.delete(post.getImage());
            post.setImage(null);

            Image image = imageService.save(dto.getNewImage(), post);
            post.setImage(image);
        }

        return postId;
    }



    /* DELETE */
    @Transactional
    public Long delete(long postId) throws IOException {
        Optional<Post> optPost = postRepository.findById(postId);

        if (optPost.isEmpty()) return null;

        if (optPost.get().getImage() != null)
            imageService.delete(optPost.get().getImage());

        postRepository.deleteById(postId);
        return postId;
    }
}
