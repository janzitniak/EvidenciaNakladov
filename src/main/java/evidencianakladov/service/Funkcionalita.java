package sk.tmconsulting.evidencianakladov.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import sk.tmconsulting.evidencianakladov.model.Vydavok;

public class Funkcionalita implements IFunkcionalita {
    private Connection conn;

    // Konstruktor, ktorym sa rovno pripajam na MySQL databazu
    public Funkcionalita() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/evidencianakladov_db"; // zmeňte URL a názov databázy podľa potreby
        String username = "root"; // zmeňte používateľské meno podľa potreby
        String password = "password"; // zmeňte heslo podľa potreby
        conn = DriverManager.getConnection(url, username, password);
        System.out.println("Spojenie s databázou je v poriadku!");
    }

/*    public void pripojSaMysql() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/evidencianakladov_db"; // zmeňte URL a názov databázy podľa potreby
        String username = "root"; // zmeňte používateľské meno podľa potreby
        String password = "password"; // zmeňte heslo podľa potreby
        conn = DriverManager.getConnection(url, username, password);
        System.out.println("Spojenie s databázou je v poriadku!");
    }*/

    @Override
    public String zadajTextovyUdaj() {
        Scanner scn = new Scanner(System.in); // inicializacia konzoly
        //String vstupnyText = scn.nextLine(); // vstup z konzoly
        //return vstupnyText;
        return scn.nextLine();
    }

    @Override
    public double zadajCiselnyUdaj() {
        Scanner scn = new Scanner(System.in); // inicializacia konzoly
        return scn.nextDouble();
    }

    @Override
    public double spocitajVsetkyVydavky() throws SQLException {
        String query = "SELECT ROUND(SUM(cena), 2) AS sucet FROM vydavky";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){ // Ak existuje zaznam, tak ...
            // ... ideme do vnutra podmienky
            // zaokruhlenie cez funkciu v Jave je iny sposob ako zaokruhlit
            return rs.getDouble("sucet");
        }
        return 0; // Ak nebude ziadny zaznam v tabulke, tak sucet vydavkov je 0
    }

    @Override
    public int zistiPocetVsetkychVydavkov() throws SQLException {
        String query = "SELECT COUNT(cena) AS pocet FROM vydavky";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            return rs.getInt("pocet");
        }
        return 0;
    }

    @Override
    public void vypisMenu() {
        System.out.println("Vyber si možnosť:");
        System.out.println("(1) Zadaj novú nákladovú položku");
        System.out.println("(2) Zobraz všetky nákladové položky");
        System.out.println("(3) Spočítaj sumu nákladov");
        System.out.println("(4) Vypíš počet záznamov");
        System.out.println("(5) Vypíš celkové náklady podľa kategórií");

        // Aplikacia od nas bude pozadovat zadanie kategorie, cize musime najprv zobrazit zoznam kategorii, z ktorych si mozeme vybrat.
        // Po vyberie kategorie, aplikacia zobrazi celkovy sumar
        System.out.println("(6) Vypíš celkové náklady podľa konkrétnej kategórie");
        System.out.println("(9) Koniec aplikácie");
    }

    @Override
    public void exportMySQL2PDF() throws SQLException, DocumentException, IOException {
        ArrayList<Vydavok> vydavky = vyberVsetkyMySQL(); // dynamicke pole s vydavkami naplnime metodou vyberVsetkyMySQL();
        // exportuje vsetky zaznamy s detailami a celkovym sumarom
        double celkovySumar = spocitajVsetkyVydavky(); // celkovy sumar vydavkov ziskame uz cez existuju metodu spocitajVsetkyVydavky()

        // START - generovanie PDF
        Document document = new Document(); // vytvorime prazdny PDF Dokument
        // vytvori konkretny subor HelloWorld.pdf, ktorý umiestni do priečinka \\Mac\Home\Documents\

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Report výdavkov.pdf"));

        document.open(); // dokument musime ho otvorit

        // START - vypise do PDF dokumentu text
        //Font font = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);

        // pouzijeme Arial Unicode MS font, ktory podporuje diakritiku
        BaseFont bf = BaseFont.createFont("arial.ttf", BaseFont.CP1250, BaseFont.EMBEDDED);
        //BaseFont bf = BaseFont.createFont("Times New Roman", BaseFont.CP1250, BaseFont.EMBEDDED);
        Font font = new Font(bf);


        Paragraph paragraph = new Paragraph("Report výdavkov - test Čokolády, ľščťžýáíé", font);
        document.add(paragraph); // do dokumentu vpiseme text A Hello Word PDF document
        for(Vydavok vystup:vydavky) {
            document.add(   new Paragraph( "Názov vydavku: " + vystup.getPopis() + " a výška vydavku je: " + vystup.getCena(), font )   );
        }
        // END - vypise do PDF dokumentu text

        document.close(); // zatvorime dokument
        writer.close(); // zatvorime subor
        // END - generovanie PDF
    }

    @Override
    public void ulozMySQL(Vydavok vydavokObjekt) throws SQLException {
        String sql = "INSERT INTO vydavky (id, popis, cena, kategoria, datum) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, vydavokObjekt.getId());
        statement.setString(2, vydavokObjekt.getPopis());
        statement.setDouble(3, vydavokObjekt.getCena());
        statement.setString(4, vydavokObjekt.getKategoria());
        statement.setDate(5, vydavokObjekt.getDatum());
        statement.executeUpdate(); // uskutocnime dany statement, cize uskutocnime sql query
    }


    @Override
    public void ulozMySQL(String popis, double cena, String kategoria, Date datum) throws SQLException {
        String sql = "INSERT INTO vydavky (popis, cena, kategoria, datum) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, popis);
        statement.setDouble(2, cena);
        statement.setString(3, kategoria);
        statement.setDate(4, datum);
        statement.executeUpdate(); // uskutocnime dany statement, cize uskutocnime sql query
    }

    @Override
    public ArrayList<Vydavok> vyberVsetkyMySQL() throws SQLException {
        String sql = "SELECT id, popis, cena, kategoria, datum FROM vydavky";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet result = statement.executeQuery();

        ArrayList<Vydavok> vydavky = new ArrayList<Vydavok>(); // vytvorili sme prazdny ArrayList, ktory bude obsahovat pouzivatelov, cize User

        while (result.next()) {
            Vydavok vydavokObjekt = new Vydavok(result.getInt("id"), result.getString("popis"), result.getDouble("cena"), result.getString("kategoria"), result.getDate("datum"));
            vydavky.add(vydavokObjekt); // naplname dynamicke pole, teda ArrayList vydavkami
        }
        return vydavky;
    }

    @Override
    public void aktualizujMySQL(int id, Vydavok vydavok) throws SQLException {
        String sql = "UPDATE vydavky SET popis = ?, cena = ?, kategoria=?, datum=? WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, vydavok.getPopis());
        statement.setDouble(2, vydavok.getCena());
        statement.setString(3, vydavok.getKategoria());
        statement.setDate(4, vydavok.getDatum());
        statement.setInt(5, id);
        statement.executeUpdate();
        System.out.println(statement);
    }

    @Override
    public void odstranMySQL(int id) throws SQLException {
        String sql = "DELETE FROM vydavky WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    @Override
    public HashMap<String, Double> ziskajVsetkyVydavkyPodlaKategorii() throws SQLException {
        String sql = "SELECT kategoria, SUM(cena) AS 'sumar podla kategorie' FROM vydavky GROUP BY kategoria";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet result = statement.executeQuery();

        //ArrayList<Vydavok> vydavky = new ArrayList<Vydavok>(); // vytvorili sme prazdny ArrayList, ktory bude obsahovat pouzivatelov, cize User

        // Do HashMap ukladam ako kluc kategoriu a ako hodnotu cena, resp. sumar podla kategorie
        HashMap<String, Double> vydavkySpoluPodlaKategorii = new HashMap<>();

        // bude prechadzat zaznamami, v tabulke a priradovat sumar (hodnota) do kategorie (kluc)
        while (result.next()) {
            vydavkySpoluPodlaKategorii.put(result.getString("kategoria"), result.getDouble("sumar podla kategorie"));
        }

        return vydavkySpoluPodlaKategorii;
    }

/*    public void zvolKategoriu() throws SQLException {
        HashMap<String, Double> vsetkyVydavkyPodlaKategorii = ziskajVsetkyVydavkyPodlaKategorii();
        System.out.println("Zoznam kategorií je nasledovný:");
        System.out.println("-------------------------------");
        for(String kategoria: vsetkyVydavkyPodlaKategorii.keySet()) {
            System.out.println(kategoria);
        }
        //System.out.println("Celkové náklady podľa kategórií " + vsetkyVydavkyPodlaKategorii);
        // musi poziadat od pouzivatela zadanie konkretnej kategorie
        // aplikacia zobrazi celkovy sumar podla zadanej kategorie
        System.out.println("\nNapíš konkrétnu kategóriu:");
        System.out.println("---------------------------");
        String kategoria = zadajTextovyUdaj();
        // ziada od pouzivatela zadanie textu z konzoly
        System.out.println(vsetkyVydavkyPodlaKategorii.get(kategoria));
    }*/


    public void zvolKategoriu() throws SQLException {
        System.out.println("\nNapíš konkrétnu kategóriu:");
        System.out.println("---------------------------");
        // ziada od pouzivatela zadanie textu z konzoly
        String kategoria = zadajTextovyUdaj();

        String sql = "SELECT SUM(cena) AS 'sumar podla kategorie' FROM vydavky WHERE kategoria='" + kategoria + "' GROUP BY kategoria";
        System.out.println(sql);
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet result = statement.executeQuery();

        //ArrayList<Vydavok> vydavky = new ArrayList<Vydavok>(); // vytvorili sme prazdny ArrayList, ktory bude obsahovat pouzivatelov, cize User

        System.out.println("Celková suma podľa kategórie je: ");
        // bude prechadzat zaznamami, v tabulke a priradovat sumar (hodnota) do kategorie (kluc)
        if (result.next()) {
            System.out.println(result.getDouble("sumar podla kategorie"));
        } else {
            System.out.println(0);
        }
    }
}
