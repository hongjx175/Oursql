package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import sql.elements.CommandMessage;
import sql.elements.Mysql;
import sql.functions.Processor;

public class ServerThread implements Runnable {

    Socket socket;
    Server server;
    ObjectInputStream reader;
    ObjectOutputStream writer;
    Mysql sql = Mysql.getInstance();

    public ServerThread(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    public static void main(String[] args) throws IOException {
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
            writer = new ObjectOutputStream(socket.getOutputStream());
            reader = new ObjectInputStream(socket.getInputStream());
            Processor processor = new Processor(reader, writer);
            String ip = socket.getRemoteSocketAddress().toString();
            this.login();
            while (true) {
                String date = LocalDate.now().toString();
                String time = LocalTime.now().toString();
                String user = processor.getUser();
                String cmd = processor.process();
                System.out.println(cmd);
                server.addLog(new CommandMessage(date, time, cmd, ip, user));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void login() throws IOException {
        try {
            String tip = "", username, password;
            boolean ok;
            do {
                writer.writeObject("请输入用户名\n");
                writer.writeObject("waiting");
                writer.writeObject(tip);
                username = (String) reader.readObject();
                writer.writeObject("请输入密码\n");
                writer.writeObject("waiting");
                writer.writeObject(tip);
                password = (String) reader.readObject();
                ok = sql.login(username, password);
                if (ok) {
                    writer.writeObject("登录成功\n");
                    writer.writeObject("waiting");
                } else {
                    writer.writeObject("用户名或密码错误，请重新登录\n");
                }
            } while (!ok);
        } catch (ClassNotFoundException ignored) {
        }
    }
}
