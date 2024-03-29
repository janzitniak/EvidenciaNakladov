package sk.tmconsulting.evidencianakladov;

import sk.tmconsulting.evidencianakladov.service.Funkcionalita;
import sk.tmconsulting.evidencianakladov.model.Vydavok;


import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class Main {
    public static void main(String[] args) throws SQLException {

        // Inicializacia funkcionality aplikacie
        Funkcionalita funkcionalitaObjekt = new Funkcionalita();
        //funkcionalitaObjekt.pripojSaMysql();


        // Spracovanie vstupu
        while (true) {
            // Zobraz menu
            funkcionalitaObjekt.vypisMenu();

            // Vstup od pouzivatela
            double cisloMenu = funkcionalitaObjekt.zadajCiselnyUdaj();

            if (cisloMenu == 1) {
                // logika, ktora je priradena cislo 1
                // System.out.println("Zadal si cislo 1");
                System.out.println("Zadaj popis");
                String popis = funkcionalitaObjekt.zadajTextovyUdaj();

                //TODO Ako vyriesit problem s desatinnou bodkou, cize ak pouzivatel zada napr. cenu 18.5 namiesto 18,5
                System.out.println("Zadaj cenu");
                double cena = funkcionalitaObjekt.zadajCiselnyUdaj();

                System.out.println("Zadaj kategóriu");
                String kategoria = funkcionalitaObjekt.zadajTextovyUdaj();

                // vygeneruje aktualny, teda dnesny datum
                Calendar currentTime = Calendar.getInstance();
                Date dnesnyDatum = new Date((currentTime.getTime()).getTime());

                //funkcionalitaObjekt.ulozMySQL(popis, cena, kategoria, dnesnyDatum);
                Vydavok vydavokObjekt = new Vydavok(popis, cena, kategoria, dnesnyDatum); // vytvorime prazdny objekt vydavok
                funkcionalitaObjekt.ulozMySQL(vydavokObjekt); // zavolame metodu ulomMySQL, cize ulozime Vydavok do databazy

            } else if (cisloMenu == 2) {
                ArrayList<Vydavok> vydavkyPole = funkcionalitaObjekt.vyberVsetkyMySQL(); // naplnili sme vydavkyPole vsetkym vydavkami
                // vypiseme obsah ArrayList
                for (Vydavok konkretnyVydavok : vydavkyPole) {
                    System.out.println(konkretnyVydavok);
                    System.out.println(); // prazdny riadok
                }
            } else if (cisloMenu == 3) {
                System.out.println("Celková suma nákladov je: " + funkcionalitaObjekt.spocitajVsetkyVydavky());
            } else if (cisloMenu == 4) {
                System.out.println("Celkový počet záznamov: " + funkcionalitaObjekt.zistiPocetVsetkychVydavkov());
            } else if (cisloMenu == 5) {
                System.out.println("Celkové náklady podľa kategórií " + funkcionalitaObjekt.ziskajVsetkyVydavkyPodlaKategorii());
            } else if (cisloMenu == 6) {
                funkcionalitaObjekt.zvolKategoriu();
            } else if (cisloMenu == 9) {
                System.out.println("Koniec aplikácie");
                System.exit(0); // ukonci aplikaciu
            }
        }

    }
}
