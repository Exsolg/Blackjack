package Engine;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static ObjectInputStream input;
    private static ObjectOutputStream output;
    private static Hand dealer;
    private static ArrayList<Card> dealercards;
    private static Socket client;

    public static void main(String[] args) {
        dealercards = new ArrayList<>();
        dealer = new Hand(dealercards);
        ServerSocket server = null;

        try {
            server = new ServerSocket(1111);

            while(true) {
                client = server.accept();
                System.out.println("Connected");

                new Thread(() -> {
                    try {
                        output = new ObjectOutputStream(client.getOutputStream());
                        input = new ObjectInputStream(client.getInputStream());
                        System.out.println((String) input.readObject());

                        output.writeObject(dealer);
                        output.flush();
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println(e.getMessage() + " error");
                    }
                }).start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
