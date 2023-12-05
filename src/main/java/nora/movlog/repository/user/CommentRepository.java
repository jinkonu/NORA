package nora.movlog.repository.user;

import nora.movlog.domain.user.Comment;
import nora.movlog.domain.user.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByPostId(long postId, Pageable pageable);
    List<Comment> findAllByMember(Member member);
}
