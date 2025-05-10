package fablix.dataimport.xml.models;

public class DirectorFilms {
    public Director director;
    public Films films;

    @Override
    public String toString() {
        return "DirectorFilms{" +
                "director=" + director +
                ", films=" + films +
                '}';
    }
}