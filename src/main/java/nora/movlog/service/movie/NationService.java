package nora.movlog.service.movie;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.movie.Nation;
import nora.movlog.repository.movie.interfaces.NationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NationService {

    private final NationRepository nationRepository;



    /* UPDATE */
    @Transactional
    public String edit(String id, String name) {
        Nation nation = nationRepository.findById(id).get();
        nation.edit(name);

        return id;
    }
}
