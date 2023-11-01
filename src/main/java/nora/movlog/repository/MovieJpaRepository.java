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

    public void flush() {
        em.flush();
    }

    @Override
    public Movie findById(String id) {
        return em.find(Movie.class, id);
    }

    @Override
    public List<Movie> findByName(String name) {
        return em.createQuery("select m from Movie m where m.titleKo like :n", Movie.class)
                .setParameter("n", "%" + name + "%")
                .getResultList();
    }
}
