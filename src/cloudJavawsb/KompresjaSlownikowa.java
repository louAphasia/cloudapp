package cloudJavawsb;

import java.util.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

	public class KompresjaSlownikowa {

	    public static String zrobSlownik(String wejscie) {
	        // tu inna wersja robienia slownika niz kolesia
	        // przy pomocy klasy Set
	        // mozna zmienic na tamta

	        Set<Character> zbior = new HashSet();

	        for(char znak : wejscie.toCharArray()) {
	            zbior.add(znak);
	        }

	        String slownik = "";
	        for(Character znak : zbior) {
	            slownik += znak;
	        }

	        return slownik;
	    }

	    public static int ileBitowNaSymbol(String slownik) {
	        // to jakas pokretna javovska wersja log2
	        // zeby policzyc min liczbe bitow na reprezentowanie N symboli

	        return (32 - Integer.numberOfLeadingZeros(slownik.length() - 1));
	    }

	    public static int znajdzKod(String slownik, char symbol) {
	        // po prostu pozycja litery (symbolu) w slowniku

	        return slownik.indexOf(symbol);
	    }

	    public static void ustawBit(byte[] wyjscie, int bit, boolean v) {
	        // bit / 8 to numer bajtu w tablicy
	        // bit % 8 to numer bitu w bajcie (bit / 8)
	        // odwracamy kolejnosc bitow, stad (7 - xxx)
	        // 1 << ... robi maske bitowa do ustawienia
	        // pozwala to ustawic albo wyczyscic dany bit bez dotykania innych

	        int maska = (1 << (7 - (bit % 8)));

	        //System.out.println("Bajt = " + (bit/8) + ", Bit = " + (bit % 8) + ", Total Bit = " + bit + ", Maska = " + maska + ", bajt: " + wyjscie[bit/8]);

	        if(v) {
	            wyjscie[bit/8] |= (byte)maska; // jesli true
	        }
	        else {
	            wyjscie[bit/8] &= ~((byte)maska); // jesli false
	        }
	    }

	    public static boolean zwrocBit(byte[] wejscie, int bit) {
	        int maska = (1 << (7 - (bit % 8)));

	        if(((int)wejscie[bit/8] & (int)maska) == 0)
	        {
	            return false;
	        }
	        else
	        {
	            return true;
	        }
	    }

	    public static String Kompresja(String wejscie) {
	        String slownik = zrobSlownik(wejscie); // zrobmy slownik

	        int bitow_na_symbol = ileBitowNaSymbol(slownik); // po slowniku policzmy ile minimalnie bitow potrzeba na symbol w slowniku
	        int bitow_danych = wejscie.length() * bitow_na_symbol; // w ilu bitach mozemy zakodowac wejscie zatem
	        int bitow_z_intro = 3 + bitow_danych; // dodajmy 3 bity na rozmiar "paddingu" na koncu
	        int bitow_do_bajtow = (int)(Math.ceil(bitow_z_intro / 8.0) * 8.0); // zaokraglmy do 8 bitow (pelne bajty)
	        int nadmiarowe = (bitow_do_bajtow - bitow_z_intro); // ile musimy dopisac jedynek "paddingu" na koncu"

	        // nasze skompresowane wyjscie, tyle bitow ile potrzeba
	        byte[] wyjscie = new byte[bitow_do_bajtow/8];
	        int aktualny_bit = 0;

	        // licznik paddingu - pierwsze 3 bity
	        for(int bit = 2 ; bit >= 0 ; --bit)
	        {
	            boolean v = ((nadmiarowe & (1 << bit)) != 0);
	            ustawBit(wyjscie, aktualny_bit, v);
	            ++aktualny_bit;
	        }

	        // kodujemy
	        for(char znak : wejscie.toCharArray()) {
	            // kod dla symbolu
	            int kod = znajdzKod(slownik, znak);

	            // zapisz bity kodu
	            for(int bit = bitow_na_symbol - 1; bit >= 0 ; --bit)
	            {
	                // (kod & (1 << bit)) != 0 to testowanie N(bit) bitu w zmiennej kod, czy true czy false
	                boolean v = ((kod & (1 << bit)) != 0);
	                ustawBit(wyjscie, aktualny_bit, v);
	                ++aktualny_bit;
	            }
	        }

	        // dodatkowe jedynki na koncu
	        for(int i = 0 ; i < nadmiarowe ; ++i) {
	            ustawBit(wyjscie, aktualny_bit, true);
	            ++aktualny_bit;
	        }

	        try {
	            String skompresowany = "";
	            skompresowany += (char)slownik.length();
	            skompresowany += slownik;
	            skompresowany += new String(wyjscie, "ISO-8859-1");
	            return skompresowany;
	        } catch (UnsupportedEncodingException e) {
	            throw new RuntimeException(e.getMessage(), e);
	        }
	    }

	    public static String Dekompresja(String skompresowany) {
	        try {
	            int rozmiar_slownika = skompresowany.charAt(0); // rozmiar slownika na samym poczatku
	            String slownik = skompresowany.substring(1, rozmiar_slownika+1); // wyciagnij slownik z poczatu
	            byte[] slownik_bajty = slownik.getBytes("ISO-8859-1");

	            byte[] wejscie = skompresowany.getBytes("ISO-8859-1"); // konwersja na tablice bajtow
	            int aktualny_bit = (1 + rozmiar_slownika) * 8; // offset - po rozmiarze i slowniku

	            int bitow_na_symbol = ileBitowNaSymbol(slownik); // po slowniku policzmy ile minimalnie bitow potrzeba na symbol w slowniku
	            int nadmiarowe = 0;
	            int bitow_danych = (skompresowany.length() - rozmiar_slownika - 1) * 8; // w ilu bitach mamy skompresowane dane

	            // licznik paddingu - pierwsze 3 bity
	            for(int bit = 2 ; bit >= 0 ; --bit) {
	                boolean v = zwrocBit(wejscie, aktualny_bit);
	                if(v == true) {
	                    nadmiarowe |= (1 << bit);
	                }
	                ++aktualny_bit;
	                --bitow_danych;
	            }

	            bitow_danych -= nadmiarowe; // nie bedziemy ich nawet czytac

	            // prealokujmy wyjscie
	            byte[] wyjscie = new byte[bitow_danych/bitow_na_symbol];
	            int znak_idx = 0;

	            // dekodujemy
	            while(aktualny_bit < bitow_danych) {
	                int kod = 0;

	                // zapisz bity kodu
	                for(int bit = bitow_na_symbol - 1; bit >= 0 ; --bit)
	                {
	                    boolean v = zwrocBit(wejscie, aktualny_bit);
	                    if(v == true) {
	                        kod |= (1 << bit);
	                    }
	                    ++aktualny_bit;
	                }

	                // emituj znak dla kodu
	                wyjscie[znak_idx] = slownik_bajty[kod];
	                ++znak_idx;
	            }

	            return new String(wyjscie, "ISO-8859-1");

	        } catch (UnsupportedEncodingException e) {
	            throw new RuntimeException(e.getMessage(), e);
	        }
	    }

	    /**
	     * Glowny program.
	     */
	    public static void main(String[] args) {
	        try {
	            String nazwa_baza = new String("");

	            // zakladamy kodowanie "ISO-8859-1" (US ASCII)

	            // odczyt z pliku do kompresji
	            System.out.println("Odczyt pliku do skompresowania...");
	            String tekst_wej = new String(Files.readAllBytes(Paths.get(nazwa_baza + ".txt")), "ISO-8859-1");
	            System.out.println("...odczytano.");

	            // kompresuj
	            System.out.println("Kompresowanie " + tekst_wej.length() + " bajtow...");
	            String skompresowany = Kompresja(tekst_wej);
	            System.out.println("...skompresowano.");

	            // zapis pliku skompresowanego
	            System.out.println("Zapis pliku skompresowanego...");
	            Files.write(Paths.get(nazwa_baza + "skomp_.txt"), skompresowany.getBytes("ISO-8859-1"));
	            System.out.println("...zapisano.");

	            // odczyt pliku skompresowanego
	            System.out.println("Odczyt pliku skompresowanego...");
	            String nowy_skompresowany = new String(Files.readAllBytes(Paths.get(nazwa_baza + "skomp_.txt")), "ISO-8859-1");
	            System.out.println("...odczytano.");

	            // dekompresuj
	            System.out.println("Dekompresowanie " + nowy_skompresowany.length() + " bajtow...");
	            String zdekompresowany = Dekompresja(nowy_skompresowany);
	            System.out.println("...zdekompresowano.");

	            // zapisz plik zdekompresowany
	            System.out.println("Zapis pliku zdekompresowanego...");
	            Files.write(Paths.get(nazwa_baza + "_dekomp.txt"), zdekompresowany.getBytes("ISO-8859-1"));
	            System.out.println("...zapisano.");
	        } catch (Exception e) {
	            System.out.println("Blad: " + e.getMessage());
	        }
	    }

	}

