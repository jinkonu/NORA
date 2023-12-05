package nora.movlog.service.user;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.user.Comment;
import nora.movlog.domain.user.Member;
import nora.movlog.domain.user.Post;
import nora.movlog.repository.user.CommentRepository;
import nora.movlog.repository.user.MemberRepository;
import nora.movlog.repository.user.PostRepository;
import nora.movlog.utils.dto.user.CommentCreateRequestDto;
import nora.movlog.utils.dto.user.CommentDto;
import nora.movlog.utils.dto.user.CommentEditDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;



    /* CREATE */
    @Transactional
    public Comment write(CommentCreateRequestDto dto, long postId, String memberLoginId) {
        Member member = memberRepository.findByLoginId(memberLoginId).get();
        Post post = postRepository.findById(postId).get();

        post.changeComment(post.getCommentCnt() + 1);
        return commentRepository.save(dto.toEntity(member, post));
    }



    /* READ */
    public CommentDto findOne(long commentId) {
        return CommentDto.of(commentRepository.findById(commentId).get());
    }


    public List<CommentDto> findAllFromPost(long postId, int page, int size) {
        return commentRepository.findAllByPostId(postId, PageRequest.of(page, size)).stream()
                .map(CommentDto::of)
                .toList();
    }



    /* UPDATE */
    public Long edit(long commentId, CommentEditDto dto, String memberLoginId) {
        Optional<Comment> optComment = commentRepository.findById(commentId);
        Optional<Member> optMember = memberRepository.findByLoginId(memberLoginId);

        if (optComment.isEmpty() || optMember.isEmpty())
            return null;

        optComment.get().update(dto);
        return commentId;
    }



    /* DELETE */
    public Long delete(long commentId, String memberLoginId) {
        Optional<Comment> optComment = commentRepository.findById(commentId);
        Optional<Member> optMember = memberRepository.findByLoginId(memberLoginId);

        if (optComment.isEmpty() || optMember.isEmpty())
            return null;

        commentRepository.deleteById(commentId);
        return commentId;
    }
}
