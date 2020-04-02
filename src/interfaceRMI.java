import java.rmi.*;

public interface interfaceRMI extends Remote {
    void launch() throws RemoteException;
}
