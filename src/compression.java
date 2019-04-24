
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class compression {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File plik = new File("D:\\test.txt");
        FileInputStream inputStream = new FileInputStream(plik);
        while (inputStream.available() > 0) {
            int znak = inputStream.read();
            System.out.print((char) znak);

        }

        inputStream.close();
        System.out.println("");


    }
}

    /* liczba ze slownika pojedyncze   liczba na binarna*/

