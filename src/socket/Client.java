package socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Port {

    public static void main(String[] args) {
        try {
            Scanner scan = new Scanner(System.in);
            Socket socket = new Socket(serverIP, port);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()));
            int n = scan.nextInt();
            for (int i = 0; i < n; i++) {
                writer.write(scan.nextLine());
            }
            n = Integer.parseInt(reader.readLine());
            for (int i = 0; i < n; i++) {
                System.out.println(reader.readLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
