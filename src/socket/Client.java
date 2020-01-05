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
            System.out.print(reader.readObject());
            while (true) {
                writer.writeObject(scan.nextLine());
                System.out.print(reader.readObject());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
