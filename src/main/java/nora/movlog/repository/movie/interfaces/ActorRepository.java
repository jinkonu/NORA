package nora.movlog.repository.movie.interfaces;

import nora.movlog.domain.movie.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, String> {
}
