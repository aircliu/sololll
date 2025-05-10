package fablix.dataimport.xml.models;

public class Actor {
    public String stagename;
    public Integer dowstart;
    public Integer dowend;
    public String familyname;
    public String firstname;
    public String alias;
    public Gender gender;
    public Integer dob;
    public String dod;
    public String roletype;
    public String origin;
    public String picref;
    public Relationships relationships;
    public Awards awards;
    public Notes notes;
    public String error;

    @Override
    public String toString() {
        return "Actor{" +
                "stagename='" + stagename + '\'' +
                ", dowstart=" + dowstart +
                ", dowend=" + dowend +
                ", familyname='" + familyname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", alias='" + alias + '\'' +
                ", gender=" + gender +
                ", dob=" + dob +
                ", dod='" + dod + '\'' +
                ", roletype='" + roletype + '\'' +
                ", origin='" + origin + '\'' +
                ", picref='" + picref + '\'' +
                ", relationships=" + relationships +
                ", awards=" + awards +
                ", notes=" + notes +
                ", error='" + error + '\'' +
                '}';
    }
}