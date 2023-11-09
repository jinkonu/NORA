package nora.movlog.service;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.Actor;
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
            if (actor.isPresent()) actors.add(actor.get());
            else {
                Actor newActor = Actor.create(id);
                actorRepository.save(newActor);
                actors.add(newActor);
            }
        }

        return actors;
    }
}
