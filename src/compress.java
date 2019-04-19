public class compress  {
    public String comp(String str){
        int c=1;
        StringBuilder builder= new StringBuilder();

        for(int i=1; i<str.length();i++){
            if(str.charAt(i) == str.charAt(i-1)&& i<str.length()-1){
              c++;
            }
            else if (i==str.length()-1 && str.charAt(i)==str.charAt(i-1)){
                c++;
                builder.append(str.charAt(i));
                builder.append(c);
            }

            else if(i == str.length()-1 && str.charAt(i)!=str.charAt(i-1)) {
                builder.append(str.charAt(i - 1));
                builder.append(c);
                c = 1;
                builder.append(str.charAt(i));
                builder.append(c);
            }
            else {
                builder.append(str.charAt(i - 1));
                builder.append(c);
                c = 1;
            }}
        str=builder.toString();
        System.out.println(str);
        return str;


            }

    public static void main(String[] args) {
        compress t= new compress();

        t.comp("abbbbbbccccc");
        t.comp("aaabbbcccc");
    }
        }
