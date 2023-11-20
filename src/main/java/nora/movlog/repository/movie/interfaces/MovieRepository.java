package nora.movlog.repository.movie.interfaces;

import nora.movlog.domain.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, String> {
    Optional<Movie> findById(String id);

    Page<Movie> findAllByTitleKoContains(String titleKo, PageRequest pageRequest);
}
