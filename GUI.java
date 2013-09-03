import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;



public class GUI extends javax.swing.JFrame {
	private Client client; //reference to the local Client object
	private ServerInterface server; //reference to the remote Server object
	private static String nickname;
	private static String host;
	private static JTextArea History;
	private JTextField Message;
	private JScrollPane jScrollPaneHistory;

	public static void main(String[] args) {
		host = JOptionPane.showInputDialog("Enter the host of the chatserver", "localhost");
		nickname = JOptionPane.showInputDialog("Enter your nickname");
		if (host != null && nickname != null && !nickname.equals("")) {
			try {
				GUI inst = new GUI();
				inst.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		else System.exit(0);	
	}

	public GUI() throws MalformedURLException, RemoteException, NotBoundException {
		super();
		server = (ServerInterface)Naming.lookup("//" + host + "/Server");
		client = new Client();
		server.login(client, nickname);
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				jScrollPaneHistory = new JScrollPane();
				getContentPane().add(jScrollPaneHistory);
				jScrollPaneHistory.setBounds(7, 7, 378, 203);
				jScrollPaneHistory.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				{
					History = new JTextArea();
					History.setLineWrap(true);
					History.setEditable(false);
					jScrollPaneHistory.setViewportView(History);
				}
			}
			{
				Message = new JTextField();
				getContentPane().add(Message);
				Message.setBounds(7, 217, 378, 42);
				Message.addKeyListener(new KeyAdapter() {
					public void keyReleased(KeyEvent evt) {
						MessageKeyPressed(evt);
					}
				});
			}
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			this.setTitle("MyRMIChat - " + nickname);
			this.setResizable(false);
			pack();
			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showMessage(String message, String nickname) {
		if (!nickname.equals(""))
			History.append(nickname + ": " + message + "\n");
		else History.append(message + "\n");
	}

	private void MessageKeyPressed(KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_ENTER && !Message.getText().equals("")) {
			try {
				server.broadcastMessage(Message.getText(), nickname);
				Message.setText("");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
}

