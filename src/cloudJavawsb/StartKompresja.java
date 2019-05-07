package cloudJavawsb;


	import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.zip.*;

	public class StartKompresja {
	    
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
	    
	    public static String Kompresja(String tekst) {
	        String skompresowany = "";
	        String slownik = do_slownika(tekst);
	        int X = slownik.length();
	        int N = zwroc_N(X);
	        int R = zwroc_R(N, tekst.length());
	        
	        skompresowany += (char)X;
	        skompresowany += slownik;
	        
	        String toBin = Dec2Bin(R, 3);
	        for (int i = 0; i < tekst.length(); i++) {
	            toBin += Dec2Bin(slownik.indexOf(tekst.charAt(i)), N);
	            if (toBin.length() >= 8) {
	                String tmp = toBin.substring(0, 8);
	                toBin = toBin.substring(8, toBin.length());
	                skompresowany += (char)Bin2Dec(tmp);   
	            }
	        }
	        if (R > 0) {
	            for (int i = 0; i < R; i++) toBin += '1';      
	            skompresowany += (char)Bin2Dec(toBin);
	        }
	        return skompresowany;
	    }
	    
	    public static String Dekompresja(String skompresowany) {
	        String tekst = "";
	        int X = skompresowany.charAt(0);
	        int N = zwroc_N(X);
	        String slownik = skompresowany.substring(1, X+1);
	        
	        int znak = skompresowany.charAt(X+1);       
	        String toBin = Dec2Bin(znak);
	        
	        String tmp = toBin.substring(0, 3);
	        int R = Bin2Dec(tmp, 3);
	        toBin = toBin.substring(3, 8);
	        
	        for (int i = X + 2; i < skompresowany.length() - 1; i++) {
	            znak = skompresowany.charAt(i); 
	            toBin += Dec2Bin(znak);            
	            while(toBin.length() >= N) {
	                tmp = toBin.substring(0, N);
	                int index = Bin2Dec(tmp, N);
	                tekst += slownik.charAt(index);
	                toBin = toBin.substring(N, toBin.length()); 
	            }  
	        }
	        znak = skompresowany.charAt(skompresowany.length() - 1); 
	        tmp = Dec2Bin(znak);
	        toBin += tmp.substring(0, 8 - R);
	        
	        while(toBin.length() >= N) {
	                tmp = toBin.substring(0, N);
	                int index = Bin2Dec(tmp, N);
	                tekst += slownik.charAt(index);
	                toBin = toBin.substring(N, toBin.length()); 
	        }        
	        return tekst;
	    }
	    
	    public static void kompresjaplik(File plik, File plik2) throws IOException {
	        byte [] buffer = new byte[1024];
	        FileInputStream fis = new FileInputStream(plik);
	        FileOutputStream fos = new FileOutputStream(plik2);
	      
	        while (fis.available() > 0) {
	            int znak = fis.read();
	     
	        }
	  
	        fos.close();
	        fis.close();
	    }
	    
	   /* static String readFile(String path, Charset encoding) 
	    		  throws IOException 
	    		{
	    		  byte[] encoded = Files.readAllBytes(Paths.get(path));
	    		  return new String(encoded, encoding);
	    		}*/
	    
	    public String readFile(String pathname) throws IOException {

	        File file = new File(pathname);
	        StringBuilder fileContents = new StringBuilder((int)file.length());        

	        try (Scanner scanner = new Scanner(file)) {
	            while(scanner.hasNextLine()) {
	                fileContents.append(scanner.nextLine() + System.lineSeparator());
	            }
	            return fileContents.toString();
	        }
	    }

	    public static void main(String[] args) throws FileNotFoundException, IOException {
	        
	        String tekst = "aabbbcc";        
	        System.out.println("Długość tekstu przed kompresją: " + tekst.length());
	        System.out.println("Test: " + tekst);
	        
	        String skompresowany = Kompresja(tekst);
	        System.out.println("Długość tekstu po kompresji: " + skompresowany.length());
	        System.out.println("Tekst skompresowany: " + skompresowany);
	         
	        String dekompresowany = Dekompresja(skompresowany);
	        System.out.println("Tekst po dekompresacji: " + dekompresowany);
	        
	        //Wyswietlenie zawartości  pliku tekst.txt
	        FileInputStream inputStream = new FileInputStream("D:/Pliki/tekst.txt");
	        System.out.println("Zawartość pliku test: ");
	        while (inputStream.available() > 0) {
	            int znak = inputStream.read();
	            
	            System.out.print((char)znak);
	        }
	        inputStream.close();
	        System.out.println("");
	        
	        
	        // odczyt z pliku do kompresji 
	        StartKompresja d=new StartKompresja();
	        String r=d.readFile("D:/Pliki/tekst.txt");
	        System.out.println(r);
	        
	        System.out.println(Kompresja(r));
	        
	        String text =Kompresja(r);

	        try {
	            Files.write(Paths.get("D:/Pliki/test2.txt"), text.getBytes());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	          
	
	        
	       
	}
	

