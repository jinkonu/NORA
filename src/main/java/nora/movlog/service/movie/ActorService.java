package nora.movlog.service.movie;

import nora.movlog.domain.movie.Actor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActorService {
    public Set<Actor> fillActors(Map<String, String> ids) {
        return ids.entrySet().stream()
                .map(entry -> Actor.builder()
                        .id(entry.getKey())
                        .name(entry.getValue())
                        .build())
                .collect(Collectors.toSet());
    }
}
