import java.rmi.*;
import java.util.ArrayList;

public interface interfaceRMI extends Remote {
    void launch() throws RemoteException;
    void Maj_score() throws RemoteException;
    void Score_total() throws RemoteException;
    String Best_XML() throws RemoteException;
}
