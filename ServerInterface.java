import java.rmi.*;

public interface ServerInterface extends Remote {
	
	public void login(ClientInterface client, String nickname) throws RemoteException;
	
	public void broadcastMessage(String message, String nickname) throws RemoteException;
}

