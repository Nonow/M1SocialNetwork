import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) {
        System.out.println("Lancement du client");
        /*
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }*/

        try {
            interfaceRMI remote = (interfaceRMI) Naming.lookup("rmi://192.168.56.1/interfaceRMI");
            System.out.println(remote);
            //on appel nos function a partir du client ici
            while(true){
                remote.Maj_score();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        System.out.println("Fin du client");
    }
}
