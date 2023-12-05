package nora.movlog.repository.user;

import nora.movlog.domain.user.Comment;
import nora.movlog.domain.user.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByPostId(long postId, Pageable pageable);
    List<Comment> findAllByMember(Member member);
}
