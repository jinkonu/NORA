package nora.movlog.service;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.Genre;
import nora.movlog.repository.interfaces.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class GenreService {
    private final GenreRepository genreRepository;

    @Transactional( readOnly = true )
    public Set<Genre> fillGenres(Set<String> ids) {
        Set<Genre> genres = new HashSet<>();

        for (String id : ids) {
            Optional<Genre> genre = genreRepository.findById(id);
            if (genre.isPresent()) genres.add(genre.get());
            else {
                Genre newGenre = Genre.create(id);
                genreRepository.save(newGenre);
                genres.add(newGenre);
            }
        }

        return genres;
    }
}
