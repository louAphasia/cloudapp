package szyfr;

import java.util.ArrayList;
import java.util.Arrays;

public class szyfrowanieRSAwsb {
  public static boolean czyPierwsza(int liczba)
  {
	  for (int i=2;i<=liczba/2;i++)
		  if (liczba%i==0) return false;
		 return true;
  }
  
  public static ArrayList sitoE(int N) {
	  boolean []tab =new boolean [N-1];
	  ArrayList<Integer>pierwsze=new ArrayList<Integer>();
	  for (int i=2;i<=N;i++)
	  {
		 if(tab[i-2]) {
		  pierwsze.add(i);
		  for(int j=2*i; j<=N;j+=i)tab[j-2]=false	  }
	  }
	  return pierwsze;
  }
  
  public static gdc(int a,int b) {
	  for(int i=Math.min(a,b);i>0;i--)
		  if (a%i==0&&b%i==0)return i;
	  
	  return -1;
  }
	  
	  /*while (a != b){
		  if (a > b)
		  a -= b;
		  else
		  b -= a;
		  return a;
  }
  
  /*public static int[]liczbypierwsze(int N){
	  
	  boolean[] tab=new boolean[N-1];
	  for(int i=2;i<=N;i++) tab[i-2]=czyPierwsza(i);
	  int licznik=0;
	  for(int i=0;i<N-1;i++) if (tab[i]) licznik++;
	  int[] pierwsze= new int[licznik];
	  
	  licznik=0;
	  for(int i=0;i<N-1;i++)
		  if(tab[i]) pierwsze[licznik++]=i+2;
	  return pierwsze; 
  } */

  public static int wyznacz_e(int phi, int n)
  {
	  int e=3;
	  while(NWD(e,phi)!=1)e+=2;
	  return e;
  }
  
  
  public static int wyznacz_d(int e,int phi)
  {
	  int d=1;
	  while((d*e)%phi!=1)d++;
  }
  
  
  public static void main(String[] args() {
	  
	  int p,q,n,phi
	  ArrayList<Integer>pierwsze=sitoR(200);
	  Random rand=new Random();
	  p=pierwsze.get*rand.nextInt(pierwsze.size()));
  q=pierwsze.get(rand.nextInt(pierwsze.size()));
  while(p==q) q=pierwsze.get(rand.nextInt(pierwsze.size()));
  n=p*q;
  phi=(p-1)*(q-1)
	  
	  
	  
	  
	  System.out.println(czyPierwsza(100));
	 System.out.println(sitoE(100));
System.out.print(gdc(120,144));


  }
}
	

