package nora.movlog.service.user;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.user.Member;
import nora.movlog.domain.user.Post;
import nora.movlog.dto.user.PostCreateRequestDto;
import nora.movlog.dto.user.PostDto;
import nora.movlog.repository.movie.interfaces.MovieRepository;
import nora.movlog.repository.user.CommentRepository;
import nora.movlog.repository.user.LikesRepository;
import nora.movlog.repository.user.PostRepository;
import nora.movlog.repository.user.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;
    private final MovieRepository movieRepository;



    /* CREATE */

    @Transactional
    public long write(String body, String movieId, long userId) {
        Member member = memberRepository.findById(userId).get();
        Post post = postRepository.save(PostCreateRequestDto.builder()
                .body(body)
                .movie(movieRepository.findById(movieId).get())
                .build()
                .toEntity(member));

        return post.getId();
    }



    /* READ */
    public PostDto findOne(Long postId) {
        return postRepository.findById(postId)
                .map(PostDto::of)
                .orElseGet(() -> null);
    }



    /* UPDATE */
    @Transactional
    public Long editPost(Long postId, PostDto postDto) {
        Optional<Post> optPost = postRepository.findById(postId);

        if (optPost.isEmpty()) return null;

        Post post = optPost.get();
        post.update(postDto);

        return postId;
    }



    /* DELETE */
    public Long deletePost(Long postId) {
        Optional<Post> optPost = postRepository.findById(postId);

        if (optPost.isEmpty()) return null;

        postRepository.deleteById(postId);
        return postId;
    }
}
