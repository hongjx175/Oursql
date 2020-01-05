package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import sql.elements.CommandMessage;
import sql.functions.Processor;

public class ServerThread implements Runnable {

    Socket socket;
    Server server;

    public ServerThread(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    public static void main(String[] args) throws IOException {
//        ObjectOutputStream writer = new ObjectOutputStream(System.out);
//        ObjectInputStream reader = new ObjectInputStream(System.in);
        Processor processor = new Processor(null, null);
        try {
            while (true) {
                String cmd = processor.process();
//            System.out.println(cmd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
            Processor processor = new Processor(reader, writer);
            String ip = socket.getRemoteSocketAddress().toString();
            while (true) {
                String date = LocalDate.now().toString();
                String time = LocalTime.now().toString();
                String user = processor.getUser();
                String cmd = processor.process();
                System.out.println(cmd);
                // TODO: 2019/12/21 log in and get user
                server.addLog(new CommandMessage(date, time, cmd, ip, user));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
