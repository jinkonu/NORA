package nora.movlog.repository.user;

import nora.movlog.domain.user.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    void deleteByMemberLoginIdAndPostId(String memberLoginId, long postId);
    boolean existsByMemberLoginIdAndPostId(String memberId, long postId);
}
