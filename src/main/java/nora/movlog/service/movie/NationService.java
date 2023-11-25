package nora.movlog.service.movie;

import nora.movlog.domain.movie.Nation;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NationService {
    public Set<Nation> fillNations(Set<String> ids) {
        return ids.stream()
                .map(id -> Nation.builder()
                        .id(id)
                        .build())
                .collect(Collectors.toSet());
    }
}
