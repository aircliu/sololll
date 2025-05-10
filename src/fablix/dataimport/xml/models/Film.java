package fablix.dataimport.xml.models;

import java.util.List;

public class Film {
    public String fid;
    public String t;
    public Alts alts;
    public Integer year;
    public Integer yearReleased;
    public Integer yearRereleased;
    public String date;
    public Dirs dirs;
    public Prods prods;
    public String studios;
    public String prcs;
    public Cats cats;
    public Awards awards;
    public Loc loc;
    public Integer period;
    public List<String> notes;
    public String error;
    public Serial serial;
    public People people;

    @Override
    public String toString() {
        return "Film{" +
                "fid='" + fid + '\'' +
                ", t='" + t + '\'' +
                ", alts=" + alts +
                ", year=" + year +
                ", yearReleased=" + yearReleased +
                ", yearRereleased=" + yearRereleased +
                ", date='" + date + '\'' +
                ", dirs=" + dirs +
                ", prods=" + prods +
                ", studios='" + studios + '\'' +
                ", prcs='" + prcs + '\'' +
                ", cats=" + cats +
                ", awards=" + awards +
                ", loc=" + loc +
                ", period=" + period +
                ", notes=" + notes +
                ", error='" + error + '\'' +
                ", serial=" + serial +
                ", people=" + people +
                '}';
    }
}