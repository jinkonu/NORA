package nora.movlog.repository.user;

import nora.movlog.domain.user.Likes;
import nora.movlog.domain.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    void deleteByMemberLoginIdAndPostId(String memberLoginId, long postId);
    boolean existsByMemberLoginIdAndPostId(String memberId, long postId);
    List<Likes> findAllByMember(Member member);
}
