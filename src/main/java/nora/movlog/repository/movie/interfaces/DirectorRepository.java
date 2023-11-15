package nora.movlog.repository.movie.interfaces;

import nora.movlog.domain.movie.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorRepository extends JpaRepository<Director, String> {
}
