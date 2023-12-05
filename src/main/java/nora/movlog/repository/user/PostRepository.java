package nora.movlog.repository.user;

import nora.movlog.domain.user.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByMemberId(long memberId, Pageable pageable);
    Page<Post> findAllByMemberLoginId(String memberLoginId, Pageable pageable);
    List<Post> findAllByMemberLoginIdAndCreatedAtAfter(String memberLoginId, LocalDateTime fromDate);
    Page<Post> findAllByMovieId(String id, Pageable pageable);
    Long countAllByMovieId(String id);
}
