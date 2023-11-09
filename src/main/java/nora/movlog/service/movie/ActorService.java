package nora.movlog.service.movie;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.movie.Actor;
import nora.movlog.repository.interfaces.ActorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ActorService {
    private final ActorRepository actorRepository;

    @Transactional( readOnly = true )
    public Set<Actor> fillActors(Map<String, String> ids) {
        Set<Actor> actors = new HashSet<>();

        for (String id : ids.keySet()) {
            Optional<Actor> actor = actorRepository.findById(id);

            if (actor.isPresent())
                actors.add(actor.get());
            else {
                actorRepository.save(Actor.builder()
                        .id(id)
                        .name(ids.get(id))
                        .build());
                actors.add(actorRepository.findById(id).get());
            }
        }

        return actors;
    }
}
