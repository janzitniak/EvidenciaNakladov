package sk.tmconsulting.evidencianakladov.model;
import java.sql.Date;

public class Vydavok {
    private int id;
    private String popis;
    private double cena;
    private String kategoria;
    private Date datum;

    public Vydavok(String popis, double cena, String kategoria, Date datum) {
        this.popis = popis;
        this.cena = cena;
        this.kategoria = kategoria;
        this.datum = datum;
    }

    public Vydavok(int id, String popis, double cena, String kategoria, Date datum) {
        this.id = id;
        this.popis = popis;
        this.cena = cena;
        this.kategoria = kategoria;
        this.datum = datum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public String getKategoria() {
        return kategoria;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    @Override
    public String toString() {
        return "Vydavok{" +
                "id=" + id +
                ", popis='" + popis + '\'' +
                ", cena=" + cena +
                ", kategoria='" + kategoria + '\'' +
                ", datum=" + datum +
                '}';
    }
}
