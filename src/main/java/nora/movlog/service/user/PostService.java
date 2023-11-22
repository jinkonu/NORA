package nora.movlog.service.user;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.user.Member;
import nora.movlog.domain.user.Post;
import nora.movlog.utils.dto.user.PostCreateRequestDto;
import nora.movlog.utils.dto.user.PostDto;
import nora.movlog.utils.dto.user.PostEditDto;
import nora.movlog.repository.movie.interfaces.MovieRepository;
import nora.movlog.repository.user.CommentRepository;
import nora.movlog.repository.user.LikesRepository;
import nora.movlog.repository.user.PostRepository;
import nora.movlog.repository.user.MemberRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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


    public List<PostDto> findAllFromOneUser(long memberId, PageRequest pageRequest) {
        return postRepository.findAllByMemberId(memberId, pageRequest).stream()
                .map(PostDto::of)
                .toList();
    }



    /* UPDATE */
    @Transactional
    public Long edit(long postId, PostEditDto dto) {
        Optional<Post> optPost = postRepository.findById(postId);

        if (optPost.isEmpty()) return null;

        Post post = optPost.get();
        post.update(dto);

        return postId;
    }



    /* DELETE */
    public Long delete(long postId) {
        Optional<Post> optPost = postRepository.findById(postId);

        if (optPost.isEmpty()) return null;

        postRepository.deleteById(postId);
        return postId;
    }
}
