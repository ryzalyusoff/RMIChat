import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server extends UnicastRemoteObject implements ServerInterface {

	protected ArrayList<ClientInterface> clients = new ArrayList<ClientInterface>();

	public Server() throws RemoteException {
	}

	public void login(ClientInterface client, String nickname) throws RemoteException {
		broadcastMessage(" :: " + nickname + " is now online ::", "");	
		clients.add(client);
	}
	
	public void broadcastMessage(String message, String nickname) throws RemoteException {
		for (int i = 0; i < clients.size(); i++) {
			ClientInterface c = clients.get(i);
			try {
				c.getMessage(message, nickname);
			} catch (RemoteException e) {

				logout(c);
				i = i - 1;
			} 
		}
	}

	public void logout(ClientInterface client) {
		clients.remove(client);
	}
	
	public static void main(String[] args) {
		try {
			Naming.rebind("Server", new Server());
			System.out.println("Server is ready");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
