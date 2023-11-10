package nora.movlog.service.movie;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.movie.Genre;
import nora.movlog.repository.movie.interfaces.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class GenreService {
    private final GenreRepository genreRepository;

    @Transactional( readOnly = true )
    public Set<Genre> fillGenres(Map<Integer, String> ids) {
        Set<Genre> genres = new HashSet<>();

        for (Integer id : ids.keySet()) {
            Optional<Genre> genre = genreRepository.findById(id);
            if (genre.isPresent()) genres.add(genre.get());
            else {
                genreRepository.save(Genre.builder()
                        .id(id)
                        .name(ids.get(id))
                        .build());
                genres.add(genreRepository.findById(id).get());
            }
        }

        return genres;
    }
}
