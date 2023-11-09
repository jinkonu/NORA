package nora.movlog.repository.interfaces;

import nora.movlog.domain.movie.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DirectorRepository extends JpaRepository<Director, String> {
    Optional<Director> findById(String id);
}
