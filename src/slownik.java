public class slownik {
    public static String do_slownika(String tekst) {


        int[] tab = new int[256];
        for (int i = 0; i < 256; i++) 
        	tab[i] = 0;
        for (int i = 0; i < tekst.length();i++)
        	tab[tekst.charAt(i)] = 1;

      
        
        String slownik = "";
        for (int i = 0; i<256;i++)
        	if (tab[i] != 0) 
        		slownik += (char) i;
        return slownik;


    }
public static void main(String[] args) {
	System.out.println(do_slownika("aabfffhhhbccbb"));
}

}


