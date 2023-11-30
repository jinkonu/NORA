package nora.movlog.service.movie;

import nora.movlog.domain.movie.Director;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DirectorService {
    public Set<Director> fillDirectors(Map<String, String> ids) {
        return ids.entrySet().stream()
                .map(entry -> Director.builder()
                        .id(entry.getKey())
                        .name(entry.getValue())
                        .build())
                .collect(Collectors.toSet());
    }
}
