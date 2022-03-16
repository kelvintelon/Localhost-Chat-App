
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    // swing components
    static JFrame chatWindow = new JFrame("Chat application");
    static JTextArea chatArea = new JTextArea(22, 40);
    static JTextField textField = new JTextField(40);
    // space between chat area and text field
    static JLabel blankLabel = new JLabel("             ");
    static JButton sendButton = new JButton("Send");
    static BufferedReader in;
    static PrintWriter out;
    static JLabel nameLabel = new JLabel("		");


    Client() {
        chatWindow.setLayout(new FlowLayout());
        
        chatWindow.add(nameLabel);
        // scroll for space
        chatWindow.add(new JScrollPane(chatArea));
        chatWindow.add(blankLabel);
        chatWindow.add(textField);
        chatWindow.add(sendButton);

        // this will stop the application
        chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatWindow.setSize(475, 500);
        // to display
        chatWindow.setVisible(true);

        // default false, then server needs to permit client
        textField.setEditable(false);
        chatArea.setEditable(false);

        // action
        sendButton.addActionListener(new Listener());
        textField.addActionListener(new Listener());
    }

    void startChat() throws Exception {
        // captures ip address
        String ipAddress = JOptionPane.showInputDialog(
                chatWindow,
                "Enter IP address",
                "IP Address Required!",
                JOptionPane.PLAIN_MESSAGE);

        // ip address, and port number of the server as parameters
        Socket soc = new Socket(ipAddress, 9811);
        in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        out = new PrintWriter(soc.getOutputStream(), true);

        while(true) {
            // reads name required message first
            String str = in.readLine();
            if (str.equals("NAMEREQUIRED")) {
                String name = JOptionPane.showInputDialog(
                        chatWindow,
                        "Enter a unique name:",
                        "Name Required!",
                        JOptionPane.PLAIN_MESSAGE);

                out.println(name);
            } // already exists
            else if (str.equals("NAMEALREADYEXISTS")) {
                String name = JOptionPane.showInputDialog(
                        chatWindow,
                        "Enter another name:",
                        "Name Already exists!",
                        JOptionPane.WARNING_MESSAGE);

                out.println(name);
            }
            else if (str.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
                // extracts name
                nameLabel.setText("You are logged in as: " +str.substring(12));
            } else {
                chatArea.append(str + "\n");
            }
        }
    }

    public static void main(String[] args) throws Exception {
	// write your code here
        Client clients = new Client();
        // runs
        clients.startChat();



    }
}

// listener to be bound to send button
class Listener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {

        // message is sent out then cleared
        Client.out.println(Client.textField.getText());
        Client.textField.setText("");
    }
}
