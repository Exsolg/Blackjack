package Server;

import Engine.*;
import NetworkTools.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class Server {
    private static Hand dealer;
    private static Deck deck;
    private static ArrayList<Player> players = new ArrayList<>();
    private static boolean isLaunched = false;
    private static DataBase dataBase;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        dataBase = new DataBase();
        dealer = new Hand(new ArrayList<Card>());
        deck = new Deck();

        try {
            ServerSocket server = new ServerSocket(25535);

            while (true) {
                Socket client = server.accept();

                System.out.println("Connected");

                new Thread(() -> {
                    try {
                        ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
                        ObjectInputStream input = new ObjectInputStream(client.getInputStream());
                        Player player = (Player) input.readObject();

                        if (dataBase.isPlayerExists(player)) {
                            output.writeObject(new Message("disconnect"));

                        } else {
                            output.writeObject(new Message("okay"));

                            dataBase.addPlayer(player);
                            players.add(player);

                            while (true) {
                                output.reset();
                                Message msg = (Message) input.readObject();
                                player = (Player) msg.getObj();

                                for (int i = 0; i < players.size(); i++) {
                                    if (players.get(i).getName().equals(player.getName())) {
                                        players.set(i, (Player) player.copy());
                                    }
                                }

                                boolean check;

                                switch (msg.getMsg()) {
                                    case ("start"):
                                        check = false;
                                        for (Player pl : players) {
                                            if (pl.getReady()) {
                                                check = true;
                                            }
                                        }

                                        if (!check) {
                                            start(player);
                                            output.writeObject(new Message("start", player, dealer));
                                        } else {
                                            output.writeObject(new Message("not"));
                                        }
                                        break;

                                    case ("hit"):
                                        player.getHand().takeCard(deck.drawCard());
                                        output.writeObject(new Message("hit", player));
                                        break;

                                    case ("stay"):
                                        check = false;
                                        for (Player pl : players) {
                                            if (!pl.getReady()) {
                                                check = true;
                                            }
                                        }

                                        if (!check) {
                                            isLaunched = false;
                                            while (dealer.valueProperty().getValue() < 17) {
                                                dealer.takeCard(deck.drawCard());
                                            }
                                            output.writeObject(new Message("stay", player, dealer));
                                        } else {
                                            output.writeObject(new Message("not"));
                                        }
                                        break;

                                    case ("disconnect"):
                                        for (int i = 0; i < players.size(); i++) {
                                            if (players.get(i).getName().equals(player.getName())) {
                                                players.remove(i);
                                                break;
                                            }
                                        }
                                        dataBase.deletePlayer(player);
                                        return;
                                }
                            }
                        }
                    } catch (IOException | ClassNotFoundException | SQLException e) {
                        System.out.println(e.getMessage() + " error");
                    }
                }).start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage() + " error");
        }
    }

    private static void start(Player player) {
        if (!isLaunched) {
            isLaunched = true;
            deck.refill();

            dealer.reset();
            for (Player pl : players) {
                pl.getHand().reset();
            }

            dealer.takeCard(deck.drawCard());
            dealer.takeCard(deck.drawCard());

            player.getHand().takeCard(deck.drawCard());
            player.getHand().takeCard(deck.drawCard());

            return;
        }

        player.getHand().takeCard(deck.drawCard());
        player.getHand().takeCard(deck.drawCard());
    }
}
