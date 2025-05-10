package fablix.dataimport.models;

public class Movie {
    public String id;
    public String title;
    public Integer year;
    public String director;

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", director='" + director + '\'' +
                '}';
    }
}
