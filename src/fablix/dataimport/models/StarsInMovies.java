package fablix.dataimport.models;

import java.util.Objects;

public class StarsInMovies {
    public String starId;
    public String movieId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StarsInMovies that = (StarsInMovies) o;
        return Objects.equals(starId, that.starId) && Objects.equals(movieId, that.movieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(starId, movieId);
    }
}
