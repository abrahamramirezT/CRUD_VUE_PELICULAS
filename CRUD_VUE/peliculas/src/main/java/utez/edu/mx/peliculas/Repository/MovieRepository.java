package utez.edu.mx.peliculas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utez.edu.mx.peliculas.Model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
