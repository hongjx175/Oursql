package socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import sql.elements.CommandMessage;
import sql.functions.Charge;

public class ServerThread implements Runnable {

    Socket socket;
    Server server;

    public ServerThread(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()));
            Charge charge = new Charge(reader, writer);
            String ip = socket.getRemoteSocketAddress().toString();
            while (true) {
                String date = LocalDate.now().toString();
                String time = LocalTime.now().toString();
                String user = charge.getUser();
                String cmd = charge.process();
                // TODO: 2019/12/21 log in and get user
                server.addLog(new CommandMessage(date, time, cmd, ip, user));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
