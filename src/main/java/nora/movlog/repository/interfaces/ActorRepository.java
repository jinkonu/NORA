package nora.movlog.repository.interfaces;

import nora.movlog.domain.movie.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActorRepository extends JpaRepository<Actor, String> {
    Optional<Actor> findById(String id);
}
