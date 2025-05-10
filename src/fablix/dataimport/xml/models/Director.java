package fablix.dataimport.xml.models;

public class Director {
    public String dirid;
    public String dirstart;
    public String dirname;
    public String coverage;

    @Override
    public String toString() {
        return "Director{" +
                "dirid='" + dirid + '\'' +
                ", dirstart='" + dirstart + '\'' +
                ", dirname='" + dirname + '\'' +
                ", coverage='" + coverage + '\'' +
                '}';
    }
}