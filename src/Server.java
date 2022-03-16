import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    static List<String> userNames = new ArrayList<>();

    // all clients have printWriters
    static List<PrintWriter> printWriters = new ArrayList<>();

    public static void main(String[] args) throws IOException {
	// write your code here

            System.out.println("Waiting for client ... ");
            // this is for client connections, 9800 random port number
            ServerSocket testing = new ServerSocket(9811);

            while(true) {
                // accepts client connections returns a socket object.
                Socket soc = testing.accept();
                System.out.println("Connection established");
                ConversationHandler handler = new ConversationHandler(soc);
                handler.start();
            }


    }
}

class ConversationHandler extends Thread {

    Socket socket;
    BufferedReader in;
    PrintWriter out;
    String name;
    
    // writing data to file
    PrintWriter pw;
    static FileWriter fw;
    static BufferedWriter bw;

        // constructor takes a socket
    public ConversationHandler(Socket socket) throws IOException {
        this.socket = socket;
        fw = new FileWriter("C:\\Users\\Kelvin\\eclipse-workspace\\ChatApp\\ChatLogs.txt", true);
        bw = new BufferedWriter(fw);
        pw = new PrintWriter(bw, true);
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // sends to client, boolean to keep the flush going
            out = new PrintWriter(socket.getOutputStream(), true);

            int count = 0;
            while(true) {
                if (count > 0) {
                    out.println("NAMEALREADYEXISTS");
                } else {
                    out.println("NAMEREQUIRED");
                }

                // captures name
                name = in.readLine();

                if (name == null) {
                    return;
                }
                // if name doesn't exist, then it's saved otherwise the count goes up and it already exists
                if (!Server.userNames.contains(name)) {
                    Server.userNames.add(name);
                    break;
                }
                count++;
            }
            out.println("NAMEACCEPTED"+name);
            Server.printWriters.add(out);

            while (true) {

                String message = in.readLine();
                if (message == null) {
                    return;
                }
                pw.println(name + ": " + message);
                for (PrintWriter writer : Server.printWriters) {
                    writer.println(name + ": " + message);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }


    }
}
