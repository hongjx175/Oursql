package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import sql.elements.CommandMessage;

public class Server {

    static public final int port = 3540;
    ServerSocket serverSocket;
    ArrayList<CommandMessage> arrayList = new ArrayList<>();

    @SuppressWarnings("all")
    public Server() {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new ServerThread(this, socket));
                thread.start();

            }
        } catch (SocketTimeoutException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }

    public void addLog(CommandMessage message) {
        this.arrayList.add(message);
    }
}
