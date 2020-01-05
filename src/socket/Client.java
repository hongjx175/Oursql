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
            String strs = (String) reader.readObject();
            while (!strs.equals("waiting")) {
                System.out.print(strs);
                strs = (String) reader.readObject();
            }
            while (true) {
                writer.writeObject(scan.nextLine());
                String str = (String) reader.readObject();
                while (!str.equals("waiting")) {
                    System.out.print(str);
                    str = (String) reader.readObject();
                }
//                System.out.println("waiting");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
