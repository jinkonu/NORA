package nora.movlog.repository.interfaces;

import nora.movlog.entity.Movie;

import java.util.List;

public interface MovieRepository {
    // CREATE
    void save(Movie movie);


    // READ
    Movie findById(Long id);

    List<Movie> findByName(String name);


    // UPDATE


    // DELETE
}
