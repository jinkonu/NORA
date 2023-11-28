package nora.movlog.repository.user;

import nora.movlog.domain.user.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByMemberId(long memberId, PageRequest pageRequest);
    Page<Post> findAllByMemberLoginId(String memberLoginId, PageRequest pageRequest);
    List<Post> findAllByMovieId(String id);
    Long countAllByMovieId(String id);
}
