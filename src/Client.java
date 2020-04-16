import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.io.File;

public class Client {
    public static void main(String[] args) {
        System.out.println("Lancement du client");

        try {
            //On connecte le client au serveur
            interfaceRMI remote = (interfaceRMI) Naming.lookup("rmi://192.168.56.1/interfaceRMI");
            System.out.println(remote);

            //Quand le client se connecte on lui renvoie en XML la liste des 3 meilleurs messages
            String var = remote.Best_XML();
            System.out.println(var);

            //on crée un fichier xml chez le client avec le xml
            File fichier = new File("./best.xml");
            PrintWriter out = new PrintWriter(new FileWriter(fichier));
            out.write(var);
            out.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Fin du client");
    }
}
