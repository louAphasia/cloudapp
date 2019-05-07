package szyfr;

public class szyfrCezar {

	public static boolean isLetter(char znak) {
		if (('A'<= znak && znak<='Z')||('a' <= znak && znak <='z'))
			return true;
		else
			return false;
	} 
	
	public static String Cezar(String tekst) {
		String wyjsciowy="";
		for(int i=0;i<tekst.length();i++)
     {
	if(isLetter(tekst.charAt(i)))
			{
		if (tekst.charAt(i)<91)
		wyjsciowy+=((char)((tekst.charAt(i)-'A'+3))%26+'A');
		else
		wyjsciowy+=((char)(tekst.charAt(i)-'a'+3)%26+'a');
	}
   else {
		wyjsciowy+=tekst.charAt(i);
}
     }
return wyjsciowy;
	}
}

	



//(znak - c)%25