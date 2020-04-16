import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class Server {
    public static void main(String[] args) {
        try {
            //instanciation de notre objet
            ReadFile data = new ReadFile();
            //Enregistrement de noms RMI
            String url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + "/interfaceRMI";
            System.out.println("Enregistrement de l'objet avec l'url : " + url);
            Naming.rebind(url, data);

            new Thread(){
                @Override
                public void run(){
                    try {
                        data.launch();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            new Thread(){
                @Override
                public void run(){
                    while(true){
                        try {
                            data.Maj_score();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
            System.out.println("Serveur lancé");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}

