import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import static java.lang.Thread.sleep;

public class ReadFile {

    public static void main(String[] args) {
        Random random = new Random();
        try (FileReader reader = new FileReader("src/reseauSocial.txt");
             BufferedReader br = new BufferedReader(reader)) {

            // read line by line
            String line;
            while ((line = br.readLine()) != null) {
                sleep(3000/(random.nextInt(3)+1));
                System.out.println(line);
            }

        } catch (IOException | InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

}
