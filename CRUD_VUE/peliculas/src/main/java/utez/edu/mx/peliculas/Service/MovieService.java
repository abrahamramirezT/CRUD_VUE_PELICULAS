package utez.edu.mx.peliculas.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utez.edu.mx.peliculas.Model.Movie;
import utez.edu.mx.peliculas.Repository.MovieRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    @Autowired
    private MovieRepository repo;

    public List<Movie> getAllMovies() {
        return repo.findAll();
    }

    public Movie saveMovie(Movie bean){
        return repo.save(bean);
    }

    public boolean deleteMovie(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    public Movie updateMovie(Long id, Movie movie) {
        Optional<Movie> existingMovieOptional = repo.findById(id);
        if (existingMovieOptional.isPresent()) {
            Movie existingMovie = existingMovieOptional.get();
            existingMovie.setName(movie.getName());
            existingMovie.setDescription(movie.getDescription());
            existingMovie.setCategory(movie.getCategory());
            return repo.save(existingMovie);
        } else {
            return null;
        }
    }
}
