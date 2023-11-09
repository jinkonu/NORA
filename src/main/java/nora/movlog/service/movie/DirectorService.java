package nora.movlog.service.movie;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.movie.Director;
import nora.movlog.repository.interfaces.DirectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class DirectorService {
    private final DirectorRepository directorRepository;

    @Transactional( readOnly = true )
    public Set<Director> fillDirectors(Map<String, String> ids) {
        Set<Director> directors = new HashSet<>();

        for (String id : ids.keySet()) {
            Optional<Director> director = directorRepository.findById(id);
            if (director.isPresent()) directors.add(director.get());
            else {
                Director newDirector = Director.create(id);
                directorRepository.save(newDirector);
                directors.add(newDirector);
            }
        }

        return directors;
    }
}
