package Game;

import Client.Client;
import Engine.*;
import NetworkTools.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

import java.io.IOException;

public class Controller {
    private final Text message = new Text();
    private Hand dealer;
    private static Player player;
    private static Client client;
    private static String nickname;
    private static String connectionIp;

    @FXML
    Button start;

    @FXML
    Button hit;

    @FXML
    Button stay;

    @FXML
    Label dealerScore;

    @FXML
    Label playerScore;

    @FXML
    Canvas dealerCanvas;

    @FXML
    Canvas playerCanvas;

    @FXML
    Text winMessage;

    @FXML
    TextField ip;

    @FXML
    TextField nick;

    @FXML
    Button connect;

    @FXML
    private void setScene(ActionEvent event) throws Exception {
        nickname = nick.getText();
        connectionIp = ip.getText();
        Scene scene = connect.getScene();
        Parent root = FXMLLoader.load(getClass().getResource("../View/Game.fxml"));
        scene.setRoot(root);

        player = new Player(nickname);
        client = new Client(player, connectionIp, 25535);

        if (client.getMessage() == null) {
            scene.setRoot(FXMLLoader.load(getClass().getResource("../View/Menu.fxml")));
        } else if (client.getMessage().getMsg().equals("disconnect")) {
            client.closeConnection();
            scene.setRoot(FXMLLoader.load(getClass().getResource("../View/Menu.fxml")));
        }
    }

    @FXML
    public void stayListener() {
        stay.setDisable(true);
        hit.setDisable(true);
        start.setDisable(true);

        player.setReady(true);

        new Thread(() -> {
            try {
                client.WriteMsg(new Message("stay", player));
                Message msg = (Message) client.ReadObject();

                while (msg.getMsg().equals("not")) {
                    client.WriteMsg(new Message("stay", player));
                    msg = (Message) client.ReadObject();
                }

                player = (Player) msg.getObj();
                dealer = (Hand) msg.getObj2();

                if (msg.getMsg().equals("stay")) {
                    addCard(dealerCanvas.getGraphicsContext2D(), dealer);
                    Platform.runLater(() -> {
                        dealerScore.setText("Current score: " + dealer.valueProperty().get());
                    });
                    endGame();
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage() + " error");
            }
        }).start();
    }

    @FXML
    public void hitListener() {
        new Thread(() -> {
            try {
                client.WriteMsg(new Message("hit", player));
                Message msg = (Message) client.ReadObject();

                player = (Player) msg.getObj();

                if (msg.getMsg().equals("hit")) {

                    Platform.runLater(() -> {
                        addCard(playerCanvas.getGraphicsContext2D(), player.getHand());
                        playerScore.setText("Current score: " + player.getHand().valueProperty().get());
                    });

                    if (player.getHand().valueProperty().get() >= 21) {
                        Platform.runLater(() -> {
                            stayListener();
                        });
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage() + " error");
            }
        }).start();
    }

    @FXML
    private void startNewGame() {
        start.setDisable(true);
        hit.setDisable(true);
        stay.setDisable(true);

        player.setReady(false);

        new Thread(() -> {
            try {
                client.WriteMsg(new Message("start", player));
                Message msg = (Message) client.ReadObject();

                while (msg.getMsg().equals("not")) {
                    client.WriteMsg(new Message("start", player));
                    msg = (Message) client.ReadObject();
                }

                player = (Player) msg.getObj();
                dealer = (Hand) msg.getObj2();

                if (msg.getMsg().equals("start")) {
                    Platform.runLater(() -> {
                        start.setDisable(true);
                        hit.setDisable(false);
                        stay.setDisable(false);

                        winMessage.setText("");
                        playerCanvas.getGraphicsContext2D().clearRect(0, 0, playerCanvas.getWidth(), playerCanvas.getHeight());
                        dealerCanvas.getGraphicsContext2D().clearRect(0, 0, dealerCanvas.getWidth(), dealerCanvas.getHeight());
                        message.setText("");

                        addCard(playerCanvas.getGraphicsContext2D(), player.getHand());
                        addCard(dealerCanvas.getGraphicsContext2D(), dealer);
                        addShirt(dealerCanvas.getGraphicsContext2D());

                        dealerScore.setText("Current score: " + (dealer.valueProperty().get() - dealer.getCards().get(0).value));
                        playerScore.setText("Current score: " + player.getHand().valueProperty().get());
                    });
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage() + " error");
            }
        }).start();
    }

    private void endGame() {
        start.setDisable(false);
        hit.setDisable(true);
        stay.setDisable(true);

        int dealerValue = dealer.valueProperty().get();
        int playerValue = player.getHand().valueProperty().get();

        if (dealerValue == 21 || playerValue > 21 || dealerValue == playerValue
                || (dealerValue < 21 && dealerValue > playerValue)) {
            winMessage.setText("DEALER WIN");
        } else if (playerValue == 21 || dealerValue > 21 || playerValue > dealerValue) {
            winMessage.setText("CONGRATULATE");
        }

    }

    private void addCard(GraphicsContext gc, Hand hand) {
        Image bgImage;
        gc.getTransform().appendScale(10, 30);
        int x = 0;
        for (Card card : hand.getCards()) {
            bgImage = new Image(getClass().getResourceAsStream("../Images/" + card.suit.getSuit() + card.rank.getRank() + ".png"));
            gc.drawImage(bgImage, x, 0, 75, 105);
            x += 77;
        }
    }

    private void addShirt(GraphicsContext gc) {
        Image bgImage;
        gc.getTransform().appendScale(10, 30);
        bgImage = new Image(getClass().getResourceAsStream("../Images/shirt.png"));
        gc.drawImage(bgImage, 0, 0, 75, 105);
    }
}
