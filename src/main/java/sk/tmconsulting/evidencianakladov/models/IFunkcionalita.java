package sk.tmconsulting.evidencianakladov.models;
import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface IFunkcionalita {

    // Vstupne udaje z konzoly cez Scanner
    String zadajTextovyUdaj();
    double zadajCiselnyUdaj();

    // Agregacne funkcie
    double spocitajVsetkyVydavky() throws SQLException; // scitanie nechame na MySQL
    int zistiPocetVsetkychVydavkov() throws SQLException;

    void vypisMenu();
    void exportMySQL2PDF() throws SQLException, DocumentException, IOException; // exportuje vsetky zaznamy s detailami a celkovym sumarom

    // MySQL CRUD
    void ulozMySQL(Vydavok vydavok) throws SQLException; // ulozi Vydavok do DB a nevrati nic

    void ulozMySQL(String popis, double cena, String kategoria, Date datum) throws SQLException;

    ArrayList<Vydavok> vyberVsetkyMySQL() throws SQLException; // vyberie vsetky zaznamy z databazy a vrati ArrayList
    // Ako bude reagovat aplikacia, ked zvolime menu polozku Uprav zaznam. Aplikacia sa nasledne opyta na ID zaznamu, cize bude pracovat s nim
    void aktualizujMySQL(int id, Vydavok vydavok) throws SQLException;
    void odstranMySQL(int id) throws SQLException;

    HashMap<String, Double> spocitajVsetkyVydavkyPodlaKategorii() throws SQLException;
}
