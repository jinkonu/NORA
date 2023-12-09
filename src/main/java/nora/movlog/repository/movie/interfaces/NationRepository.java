package nora.movlog.repository.movie.interfaces;

import nora.movlog.domain.movie.Nation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NationRepository extends JpaRepository<Nation, String> {
}
