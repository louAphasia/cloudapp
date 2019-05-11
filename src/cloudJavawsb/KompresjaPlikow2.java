package cloudJavawsb;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


	public class KompresjaPlikow2 {

		 public static String do_slownika(String tekst) {
		        int [] tab = new int[256];
		        for (int i = 0; i < 256; i++) tab[i] = 0;
		        for (int i = 0 ; i < tekst.length(); i++) tab[tekst.charAt(i)] = 1;

		        String slownik = "";
		        for (int i = 0; i < 256; i++) if (tab[i] != 0) slownik += (char)i;
		        return slownik;
		    }

		    public static int zwroc_N(int X) {
		        return (int)Math.ceil( Math.log(X) / Math.log(2));
		    }

		    public static int zwroc_R(int N, int dlugosc) {
		        return (8 - (3 + N*dlugosc)%8)%8;
		    }

		    public static String Dec2Bin(int liczba, int N) {
		        String wynik = "";
		        while (liczba > 0) {
		            wynik = (char)(48 + liczba%2) + wynik;
		            liczba /= 2;
		        }
		        for (int i = wynik.length(); i < N; i++) wynik = '0' + wynik;
		        return wynik;
		    }

		    public static String Dec2Bin(int liczba) {
		        String wynik = "";
		        while (liczba > 0) {
		            wynik = (char)(48 + liczba%2) + wynik;
		            liczba /= 2;
		        }
		        for (int i = wynik.length(); i < 8; i++) wynik = '0' + wynik;
		        return wynik;
		    }

		    public static int Bin2Dec(String liczba, int N) {
		        int wynik = 0;
		        int mnoznik = 1;
		        for (int i = N; i > 0; i-- ) {
		            wynik += (liczba.charAt(i-1) - 48) * mnoznik;
		            mnoznik *= 2;
		        }
		        return wynik;
		    }

		    public static int Bin2Dec(String liczba) {
		        int wynik = 0;
		        int mnoznik = 1;
		        for (int i = 8; i > 0; i-- ) {
		            wynik += (liczba.charAt(i-1) - 48) * mnoznik;
		            mnoznik *= 2;
		        }
		        return wynik;
		    }

		    public static String Kompresja(String tekst) throws UnsupportedEncodingException {
		        String skompresowany = "";
		        String slownik = do_slownika(tekst);
		        int X = slownik.length();
		        int N = zwroc_N(X);
		        int R = zwroc_R(N, tekst.length());

		        skompresowany += (char)X;
		        skompresowany += slownik;

		        // tablica wyjsciowa danych skompresowanych
		        // rozmiar to 3 bity poczatkowe +
		        // rozmiar tekstu wejsciowego razy ile bitow na znak po kompresji (N)
		        // plus bity nadmiarowe (R)
		        // wszystko dzielone przez 8 aby powrocic z bitow do bajtow
		        byte[] wyjscie = new byte[(3 + tekst.length() * N + R)/8];
		        int idx = 0;

		        String toBin = Dec2Bin(R, 3);
		        for (int i = 0; i < tekst.length(); i++) {
		            toBin += Dec2Bin(slownik.indexOf(tekst.charAt(i)), N);
		            if (toBin.length() >= 8) {
		                String tmp = toBin.substring(0, 8);
		                toBin = toBin.substring(8, toBin.length());
		                wyjscie[idx] = (byte)Bin2Dec(tmp); // wychodzi pelny bajt
		                ++idx; // przesun indeks wyjscia
		            }
		        }
		        if (R > 0) {
		            for (int i = 0; i < R; i++) toBin += '1';
		            wyjscie[idx] = (byte)Bin2Dec(toBin); // wychodzi pelny bajt
		        }

		        skompresowany += new String(wyjscie, "ISO-8859-1");

		        return skompresowany;
		    }

		    public static String Dekompresja(String skompresowany) throws UnsupportedEncodingException {
		        int X = skompresowany.charAt(0);
		        int N = zwroc_N(X);
		        String slownik = skompresowany.substring(1, X+1);

		        int znak = skompresowany.charAt(X+1);
		        String toBin = Dec2Bin(znak);

		        String tmp = toBin.substring(0, 3);
		        int R = Bin2Dec(tmp, 3);
		        toBin = toBin.substring(3, 8);

		        // tablica wyjsciowa dla danych zdekompresowanych
		        // rozmiar to rozmiar danych skompresowanych (length() minus rozmiar slownika minus znak poczatkowy)
		        // pomnozony razy 8 bitow (na bajt) i podzielony przez N - ile bitow na symbol
		        // to daje w efekcie ile znakow bedzie po dekompresji
		        byte[] wyjscie = new byte[((skompresowany.length() - X - 1) * 8) / N];
		        int idx = 0;

		        for (int i = X + 2; i < skompresowany.length() - 1; i++) {
		            znak = skompresowany.charAt(i);
		            toBin += Dec2Bin(znak);
		            while(toBin.length() >= N) {
		                tmp = toBin.substring(0, N);
		                int index = Bin2Dec(tmp, N);
		                wyjscie[idx] = (byte)slownik.charAt(index); // wstaw znak ze slownika
		                ++idx;
		                toBin = toBin.substring(N, toBin.length());
		            }
		        }
		        znak = skompresowany.charAt(skompresowany.length() - 1);
		        tmp = Dec2Bin(znak);
		        toBin += tmp.substring(0, 8 - R);

		        while(toBin.length() >= N) {
		            tmp = toBin.substring(0, N);
		            int index = Bin2Dec(tmp, N);
		            wyjscie[idx] = (byte)slownik.charAt(index);
		            toBin = toBin.substring(N, toBin.length());
		        }

		        return new String(wyjscie, "ISO-8859-1");
		    }

		    public static void main(String[] args) throws FileNotFoundException, IOException, UnsupportedEncodingException {
		        String nazwa_baza = new String("D:/Pliki/test1");

		        // zakladamy kodowanie "ISO-8859-1"

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
		        Files.write(Paths.get(nazwa_baza + "_skomp.txt"), skompresowany.getBytes("ISO-8859-1"));
		        System.out.println("...zapisano.");

		        // odczyt pliku skompresowanego
		        System.out.println("Odczyt pliku skompresowanego...");
		        String nowy_skompresowany = new String(Files.readAllBytes(Paths.get(nazwa_baza + "_skomp.txt")), "ISO-8859-1");
		        System.out.println("...odczytano.");

		        // dekompresuj
		        System.out.println("Dekompresowanie " + nowy_skompresowany.length() + " bajtow...");
		        String zdekompresowany = Dekompresja(nowy_skompresowany);
		        System.out.println("...zdekompresowano.");

		        // zapisz plik zdekompresowany
		        System.out.println("Zapis pliku zdekompresowanego...");
		        Files.write(Paths.get(nazwa_baza + "_dekomp.txt"), zdekompresowany.getBytes("ISO-8859-1"));
		        System.out.println("...zapisano.");
		    }
		}