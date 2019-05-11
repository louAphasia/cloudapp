package szyfr;

import java.io.FileNotFoundException;

public class szyfrCezar {

	public static boolean isLetter(char znak) {
		if (('A'<= znak && znak<='Z')||('a' <= znak && znak <='z'))
			return true;
		else
			return false;
	} 

	public static String Cezar(String tekst,int p ) {
		String wyjsciowy="";
		for(int i=0;i<tekst.length();i++)
     {
	if(isLetter(tekst.charAt(i)))
			{
		if (tekst.charAt(i)<91)
		{
		wyjsciowy+=(char)((tekst.charAt(i)-'A'+3)%26+'A');
		}
		else
		{
		wyjsciowy+=(char)((tekst.charAt(i)-'a'+3)%26+'a');
		System.out.println((tekst.charAt(i)));
		System.out.println((tekst.charAt(i)-'a'));
		System.out.println((char)((tekst.charAt(i)+3)));}
	}
   else {
		wyjsciowy+=tekst.charAt(i);
}
     }
return wyjsciowy;
	}
	
	/*public static String reverse_Cezar(String tekst) {
		String wyjscie="";
		for (int i=0; i<tekst.length();i++) {
			if(isLetter(tekst.charAt(i))) {
				if(tekst.charAt(i)<91) wyjscie+=(char)((tekst.charAt(i)-'A'-3+26)%26+ 'A');
				else wyjscie+=(char)((tekst.charAt(i)-'a'-3+26)%26+'a');
			}
			else wyjscie+= tekst.charAt(i);
		}
		return wyjscie;
		
			}  */
	
	/*public static String reverseCezar(String tekst) {
		String zaszyfrowany="";
		for(int i=0;i<tekst.length();i++) {
			if(isLower(tekst.charAt(i)))
			{
				zaszyfrowany+=(char)('z'-('z'-tekst.charAt(i)+3)%26);
			}
			else if (isUpperCase(tekst.charAt(i)))
			{
				zaszyfrowany+=(char)('Z'-('Z'-tekst.charAt(i)+ 3)%26);
				
			}
			else zaszyfrowany+=tekst.charAt(i);
		}
		return zaszyfrowany;
	}
		*/
	
	
public static void main(String[] args) throws FileNotFoundException{
	
	 String st= "ala ma kota";
System.out.println(Cezar(st,3));
//System.out.println(reverseCezar(Cezar(st,5)));
}
}

	



//(znak - c)%25