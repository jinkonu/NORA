package nora.movlog.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import nora.movlog.entity.Movie;
import nora.movlog.repository.interfaces.MovieRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MovieJpaRepository implements MovieRepository {
    private final EntityManager em;

    @Override
    public void save(Movie movie) {
        em.persist(movie);
    }

    @Override
    public Movie findById(Long id) {
        return em.find(Movie.class, id);
    }

    @Override
    public List<Movie> findByName(String name) {
        return em.createQuery("select m from Movie m where m.titleKo = :n")
                .setParameter("n", name)
                .getResultList();
    }

    @Override
    public Movie update(Long id) {
        Movie movie = findById(id);

        return movie;
    }
}
