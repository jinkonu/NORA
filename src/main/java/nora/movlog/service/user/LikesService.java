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

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikesService {

    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;



    /* CREATE */
    @Transactional
    public void add(long memberId, long postId) {
        Post post = postRepository.findById(postId).get();
        Member member = memberRepository.findById(memberId).get();

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
    public void delete(long memberId, long postId) {
        likesRepository.deleteByMemberIdAndPostId(memberId, postId);
    }
}
