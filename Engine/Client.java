package Engine;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Player player;

    public Client(Player pl, String ip, int port) {
        player = pl;
        try {
            client = new Socket(ip, port);

            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());

            output.writeObject(pl.getName());
            output.flush();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void WriteMsg(Object a) throws IOException {
        output.writeObject(a);
        output.flush();
    }

    public Object ReadObject() throws IOException, ClassNotFoundException {
        return input.readObject();
    }
}
