import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements ClientInterface {

	public Client() throws RemoteException {
	}

	public void getMessage(String message, String nickname) throws RemoteException {
		GUI.showMessage(message, nickname);
	}
}
