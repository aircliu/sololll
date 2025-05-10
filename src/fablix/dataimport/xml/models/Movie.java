package fablix.dataimport.xml.models;

import java.util.List;

public class Movie {
    public String movieId;
    public String title;
    public String actor;
    public String roleType;
    public String roleDesc;
    public String roleName;
    public RoleSpecs roleSpecs;
    public Xref xref;
    public String notes;
    public String error;
    public List<MovieAward> awards;
    public Life life;
    public String episode;
    public String sings;
    public Integer rnumber;
    public String n;

    @Override
    public String toString() {
        return "Movie{" +
                "movieId='" + movieId + '\'' +
                ", title='" + title + '\'' +
                ", actor='" + actor + '\'' +
                ", roleType='" + roleType + '\'' +
                ", roleDesc='" + roleDesc + '\'' +
                ", roleName='" + roleName + '\'' +
                ", roleSpecs=" + roleSpecs +
                ", xref=" + xref +
                ", notes='" + notes + '\'' +
                ", error='" + error + '\'' +
                ", awards=" + awards +
                ", life=" + life +
                ", episode='" + episode + '\'' +
                ", sings='" + sings + '\'' +
                ", rnumber=" + rnumber +
                ", n='" + n + '\'' +
                '}';
    }
}
