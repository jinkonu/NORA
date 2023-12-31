package nora.movlog.service.user;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.user.Likes;
import nora.movlog.domain.user.Member;
import nora.movlog.domain.user.Post;
import nora.movlog.repository.user.LikesRepository;
import nora.movlog.repository.user.MemberRepository;
import nora.movlog.repository.user.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LikesService {

    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;



    /* CREATE */
    @Transactional
    public Likes add(String memberLoginId, long postId) {
        Member member = memberRepository.findByLoginId(memberLoginId).get();
        Post post = postRepository.findById(postId).get();

        post.changeLike(post.getLikeCnt() + 1);

        return likesRepository.save(Likes.builder()
                .member(member)
                .post(post)
                .build());
    }


    /* READ */
    public boolean check(String memberLoginId, long postId) {
        return likesRepository.existsByMemberLoginIdAndPostId(memberLoginId, postId);
    }


    /* DELETE */
    @Transactional
    public void delete(String memberLoginId, long postId) {
        Post post = postRepository.findById(postId).get();
        post.changeLike(post.getLikeCnt() - 1);

        likesRepository.deleteByMemberLoginIdAndPostId(memberLoginId, postId);
    }
}
