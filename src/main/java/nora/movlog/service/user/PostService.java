package nora.movlog.service.user;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.user.Member;
import nora.movlog.domain.user.Post;
import nora.movlog.dto.user.PostCreateRequestDto;
import nora.movlog.dto.user.PostDto;
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



    /* CREATE */

    @Transactional
    public Long write(String body, long userId) {
        Member member = memberRepository.findById(userId).get();
        Post savedPost = postRepository.save(PostCreateRequestDto
                .builder().body(body).build()
                .toEntity(member));

        return savedPost.getId();
    }



    /* READ */
    public PostDto findOne(Long boardId) {
        return postRepository.findById(boardId)
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
