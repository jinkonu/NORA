package nora.movlog.repository.interfaces;

import nora.movlog.domain.movie.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, String> {
}
