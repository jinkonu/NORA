package nora.movlog.repository.user;

import nora.movlog.domain.user.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
