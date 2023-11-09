package nora.movlog.repository.interfaces;

import nora.movlog.domain.movie.Nation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NationRepository extends JpaRepository<Nation, String> {
    Optional<Nation> findById(String id);
}
