import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SlownikFile {

public static String do_slownika() throws FileNotFoundException, IOException
{
	int[]tab=new int[256];
	for(int i =0; i<256;i++)tab[i]=0;
	
	File plik=new File("D:\\test.txt");
	FileInputStream inputStream= new FileInputStream(plik);
	while(inputStream.available()>0){
		int znak=inputStream.read();
		tab[znak]=1;
		
	}
	inputStream.close();
	String slownik="";
	for(int i=0; i<256;i++) if(tab[i]!=0) slownik+=(char)i;
	
}
}
