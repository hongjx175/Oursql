package socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Port {

    public static void main(String[] args) {
        try {
            Scanner scan = new Scanner(System.in);
            Socket socket = new Socket(serverIP, port);
            ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
            int n = Integer.parseInt(scan.nextLine());
            for (int i = 0; i < n; i++) {
                writer.writeObject(scan.nextLine());
                String string = (String) reader.readObject();
                System.out.println(string);
            }
            while (true) {
                String string = (String) reader.readObject();
                System.out.println(string);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
