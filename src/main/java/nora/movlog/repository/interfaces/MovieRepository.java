package nora.movlog.repository.interfaces;

import nora.movlog.domain.Movie;

import java.util.List;

public interface MovieRepository {
    // CREATE
    void save(Movie movie);


    // READ
    Movie findById(String id);

    List<Movie> findByName(String name);


    // UPDATE


    // DELETE
}