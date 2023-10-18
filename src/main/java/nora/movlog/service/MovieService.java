package nora.movlog.service;

import lombok.RequiredArgsConstructor;
import nora.movlog.entity.Movie;
import nora.movlog.repository.MovieApiRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/*
영화 페이지
 */

@RequiredArgsConstructor
@Service
public class MovieService {
    private final MovieApiRepository movieApiRepository;

    public static Movie findByKobis(String kobisId) {
        return new Movie();
    }

    public HashMap<String, String> search(String searchDt) {
        return new HashMap<>();
    }
}
