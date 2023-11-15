package nora.movlog.repository.user;

import nora.movlog.domain.user.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // FOR USER
    List<Post> findAllByUserLoginId(String loginId);

    // FOR MOVIE
    List<Post> findAllByMovieId(String id);

    Long countAllByMovieId(String id);
}