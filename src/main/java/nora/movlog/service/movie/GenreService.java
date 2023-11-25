package nora.movlog.service.movie;

import nora.movlog.domain.movie.Genre;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreService {
    public Set<Genre> fillGenres(Map<Integer, String> ids) {
        return ids.entrySet().stream()
                .map(entry -> Genre.builder()
                        .id(entry.getKey())
                        .name(entry.getValue())
                        .build())
                .collect(Collectors.toSet());
    }
}
