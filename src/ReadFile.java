import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ReadFile {
    public String lignes[];
    public int i=0;

    public ReadFile (String fileName) throws IOException {
        BufferedReader lecteurAvecBuffer = null;
        String ligne ="debut";

        try
        {
            lecteurAvecBuffer = new BufferedReader(new FileReader(fileName));
        }
        catch(FileNotFoundException exc)
        {
            lignes[i]=("Erreur d'ouverture");
        }
        while (ligne != null) {
            System.out.println(ligne);
            ligne = lecteurAvecBuffer.readLine();
            lignes[i]= ligne;
            i++;
        }
        lecteurAvecBuffer.close();
    }
}
