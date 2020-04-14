import java.rmi.*;
import java.util.ArrayList;

public interface interfaceRMI extends Remote {
    void launch() throws RemoteException;
    String bestOf() throws RemoteException;
    void Maj_score() throws RemoteException;
    void updateScore() throws RemoteException;
}
