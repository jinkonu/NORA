package nora.movlog.service.movie;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.movie.Nation;
import nora.movlog.repository.movie.interfaces.NationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class NationService {
    private final NationRepository nationRepository;

    @Transactional( readOnly = true )
    public Set<Nation> fillNations(Set<String> ids) {
        Set<Nation> nations = new HashSet<>();

        for (String id : ids) {
            Optional<Nation> nation = nationRepository.findById(id);

            if (nation.isPresent()) nations.add(nation.get());
            else {
                Nation savedNation = nationRepository.save(Nation.builder()
                        .id(id)
                        .build());
                nations.add(savedNation);
            }
        }

        return nations;
    }
}
