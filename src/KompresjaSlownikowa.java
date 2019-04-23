import java.util.*;

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
	        if(v) {
	            wyjscie[bit/8] |= maska; // jesli true
	        }
	        else {
	            wyjscie[bit/8] &= ~maska; // jesli false
	        }
	    }
	    
	    /**
	     * Glowny program.
	     */
	    public static void main(String[] args) {
	        // testowy string wejsciowy
	        String wejscie = "aabaabbccaccacaacbaacaaaabccaacabaacaabbacccaabbaaccaaabcbacabcbcaaaacbacabaccabaaabcccaaabccaabcacac";
	        
	        String slownik = zrobSlownik(wejscie); // zrobmy slownik
	        
	        int bitow_na_symbol = ileBitowNaSymbol(slownik); // po slowniku policzmy ile minimalnie bitow potrzeba na symbol w slowniku
	        
	        int bitow_danych = wejscie.length() * bitow_na_symbol; // w ilu bitach mozemy zakodowac wejscie zatem
	        
	        int bitow_z_intro = 3 + bitow_danych; // dodajmy 3 bity na rozmiar "paddingu" na koncu
	        
	        int bitow_do_bajtow = (int)(Math.ceil(bitow_z_intro / 8.0) * 8.0); // zaokraglmy do 8 bitow (pelne bajty)
	        
	        int nadmiarowe = (bitow_do_bajtow - bitow_z_intro); // ile musimy dopisac jedynek "paddingu" na koncu"
	        
	        // popiszmy na konsole wyniki naszych obliczen
	        System.out.println("Tekst do kompresji: " + wejscie + "\n");  
	        System.out.println("Slownik: " + slownik + "\n");
	        System.out.println("Dlugosc tekstu przed kompresja: " + wejscie.length());
	        System.out.println("Unikalnych liter: " + slownik.length());
	        System.out.println("Liczba bitow na znak: " + bitow_na_symbol);
	        System.out.println("Liczba nadmiarowych 1: " + nadmiarowe + "\n");
	        
	        // nasze skompresowane wyjscie, tyle bitow ile potrzeba
	        byte[] wyjscie = new byte[bitow_do_bajtow/8];
	        int aktualny_bit = 0;
	        
	        // licznik paddingu - pierwsze 3 bity
	        {
	            for(int bit = 2 ; bit >= 0 ; --bit)
	            {
	                ustawBit(wyjscie, aktualny_bit, ((nadmiarowe & (1 << bit)) != 0)); 
	                ++aktualny_bit;
	            }
	        }
	        
	        // kodujemy
	        for(char znak : wejscie.toCharArray()) {
	            // kod dla symbolu
	            int kod = znajdzKod(slownik, znak);
	            
	            // zapisz bity kodu
	            for(int bit = bitow_na_symbol - 1; bit >= 0 ; --bit)
	            {
	                // (kod & (1 << bit)) != 0 to testowanie N(bit) bitu w zmiennej kod, czy true czy false
	                
	                ustawBit(wyjscie, aktualny_bit, ((kod & (1 << bit)) != 0));
	                ++aktualny_bit;
	            }
	        }
	        
	        // dodatkowe jedynki na koncu
	        for(int i = 0 ; i < nadmiarowe ; ++i) {
	            ustawBit(wyjscie, aktualny_bit, true);
	            ++aktualny_bit;
	        }
	        
	        // wydrukujmy co mamy, binarnie i decymalnie
	        for(byte b : wyjscie) {
	            System.out.println(Integer.toBinaryString(b & 255 | 256).substring(1) + " = " + (int)(b & 255));
	        }
	        
	        // podsumowanie
	        String wyjscie_str = new String(wyjscie);
	        System.out.println("Skompresowany tekst: " + slownik + wyjscie_str);
	        System.out.println("Dlugosc tekstu po kompresji: " + (bitow_do_bajtow / 8));
	    }

	}


