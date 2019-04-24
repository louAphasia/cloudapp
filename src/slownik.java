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
    
    public static int zwroc_N(int X)
    {
    	return (int)Math.ceil(Math.log(X)/Math.log(2));
    }
    
    public static int zwroc_R(int N,int dlugosc)
    {
    	return (8-(3+N*dlugosc)%8)%8;
    }
    
    public static String Dec2Bin(int liczba,int N) {
    	String wynik="";
    	while(liczba>0)
    	{
    		wynik=(char)(48+liczba%2)+wynik;
    		liczba/=2;
    	}
    	for(int i=wynik.length();i<N;i++) wynik='0'+wynik;
    		
    		
    	return wynik;
    	}
    			
    
    public static int Bin2Dec(String liczba) {
    	
    int wynik=0;
    int mnoznik=1;
    for(int i=8;i>0;i--) {
    	wynik+=(liczba.charAt(i-1)-48)*mnoznik;
    	mnoznik*=2;
    }
  
    return wynik;
    }
    
    public static String Kompresja(String tekst) {
    	
    	String skompresowany="";
    	String slownik=do_slownika(tekst);
    	int X=slownik.length();
    	int N=zwroc_N(X);
    	int R=zwroc_R(N,tekst.length());
    	
    	skompresowany+=(char)X;
    	skompresowany+=slownik;
    	
    	String toBin=Dec2Bin(R,3);
    	
    	for(int i=0;i<tekst.length();i++)
    	{
    		toBin+=Dec2Bin(slownik.indexOf(tekst.charAt(i)),N);
    		if(toBin.length()>=8)
    		{
    			String tmp=toBin.substring(0,8);
    			toBin=toBin.substring(8,toBin.length());
    			//int znak=Bin2Dec(tmp);
    			//System.out.println(tmp + "-" +(char)znak+ "(ASCII "+ znak+")");
    		    skompresowany+=(char)Bin2Dec(tmp);//skompresowany+=(char)znak;	
    		}
    	}
    if(R>0) {
    	for(int i=0;i<R;i++)toBin+='1';
    	skompresowany+=(char)Bin2Dec(toBin);
    	//int znak=Bin2Dec(toBin);
    	//System.out.println(toBin+"-"+(char)znak+"ASCII"+ znak+ ")");
    	
    	
    }
    return skompresowany;
    }
 
    
public static String Dekompresja(String skompresowany) {
	
		String tekst="";
		int X =skompresowany.charAt(0);
		int N=zwroc_N(X);
		String slownik=skompresowany.substring(1,X+1);
		
		int znak=skompresowany.charAt(X+1);
		String toBin=Dec2Bin(znak);
		
		String tmp=toBin.substring(0,3);
		int R=Bin2Dec(tmp,3);
		toBin=toBin.substring(3,8);
		
	    for(int i=X+2;i<skompresowany.length()-1);	
		
	}
}
    
public static void main(String[] args) {
	
	System.out.println(do_slownika("aabfffhhhbccbb"));
	System.out.println();
}

}


