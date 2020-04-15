import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static void main(String[] args) {
        try {
            //LocateRegistry.createRegistry(1199);

            /*System.out.println("Mise en place du Security Manager ...");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new RMISecurityManager());
            }*/
            //instanciation de notre objet
            ReadFile data = new ReadFile();
            //Enregistrement de noms RMI
            String url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + "/interfaceRMI";
            System.out.println("Enregistrement de l'objet avec l'url : " + url);
            Naming.rebind(url, data);

            //lancement de la lecture du fichier reseauSocial.txt
            data.launch();
            System.out.println("Serveur lancé");
            data.Maj_score();

            //System.out.println("Serveur lancé");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}

