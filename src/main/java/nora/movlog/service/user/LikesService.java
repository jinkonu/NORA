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
    public void add(String memberLoginId, long postId) {
        Member member = memberRepository.findByLoginId(memberLoginId).get();
        Post post = postRepository.findById(postId).get();

        likesRepository.save(Likes.builder()
                .member(member)
                .post(post)
                .build());
        post.addLike();
    }


    /* READ */
    public boolean check(long memberId, long postId) {
        return likesRepository.existsByMemberIdAndPostId(memberId, postId);
    }


    /* DELETE */
    @Transactional
    public void delete(String memberLoginId, long postId) {
        likesRepository.deleteByMemberLoginIdAndPostId(memberLoginId, postId);
    }
}
