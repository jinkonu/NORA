package nora.movlog.repository.user;

import nora.movlog.domain.user.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
