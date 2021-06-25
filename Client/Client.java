package Client;

import NetworkTools.Message;
import Engine.Player;

import java.io.*;
import java.net.Socket;

public class Client {
    private static Socket client;
    private static ObjectInputStream input;
    private static ObjectOutputStream output;
    private static Player player;
    private static Message message;
    private static boolean launched;

    public Client(Player pl, String ip, int port) {
        player = pl;
        try {
            client = new Socket(ip, port);

            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());

            output.writeObject(pl);
            message = (Message) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage() + " error");
        }
        launched = true;
    }

    public static void WriteMsg(Object a) throws IOException {
        output.writeObject(a);
    }

    public static Object ReadObject() throws IOException, ClassNotFoundException {
        return input.readObject();
    }

    public static void closeConnection() throws IOException {
        output.writeObject(new Message("disconnect", player));

        input.close();
        output.close();
        client.close();
    }

    public static Message getMessage() {
        return message;
    }

    public static boolean isLaucnhed() {
        return launched;
    }
}
