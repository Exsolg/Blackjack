package Game;

import Engine.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    private final Deck deck = new Deck();
    private final Text message = new Text();
    private final SimpleBooleanProperty playable = new SimpleBooleanProperty(false);
    private static Hand dealer;
    private static Player player;
    private static Client client;
    private static String nickname;
    private static String connectionip;

    @FXML
    Button start;
    @FXML
    Button hit;
    @FXML
    Button stay;
    @FXML
    Label nick1;
    @FXML
    Label nick2;
    @FXML
    Label score1;
    @FXML
    Label score2;
    @FXML
    Canvas dealercan;
    @FXML
    Canvas playercan;
    @FXML
    ImageView background = new ImageView(new Image(getClass().getResourceAsStream("../Images/background.png")));
    @FXML
    Text winmessage;
    @FXML
    TextField ip;
    @FXML
    TextField nick;
    @FXML
    Button connect;

    @FXML
    private void setScene(ActionEvent event) throws Exception {
        nickname = nick.getText();
        connectionip = ip.getText();
        Scene Scene = connect.getScene();
        Parent Root = FXMLLoader.load(getClass().getResource("View.fxml"));
        Scene.setRoot(Root);

        player = new Player(nickname);
        client = new Client(player, connectionip, 1111);
    }

    @FXML
    public void start_game(){
        new Thread(() -> {
            try {
                dealer = (Hand) client.ReadObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }

            startNewGame();

            player.getHand().valueProperty().addListener((obs, old, newValue) -> {
                if (newValue.intValue() >= 21) {
                    endGame();
                }
            });

            dealer.valueProperty().addListener((obs, old, newValue) -> {
                if (newValue.intValue() >= 21) {
                    endGame();
                }
            });

            start.setOnAction(event -> startNewGame());

            hit.setOnAction(event -> {
                player.getHand().takeCard(deck.drawCard());

                int x = 77 * (player.getHand().getCards().size() - 1);
                addCard(playercan.getGraphicsContext2D(), player.getHand(), x, 0, 75, 105);

                score2.setText("Current score: " + player.getHand().valueProperty().get());
            });

            stay.setOnAction(event -> {

                while (dealer.valueProperty().get() < 17) {
                    dealer.takeCard(deck.drawCard());

                    int x = 77 * (dealer.getCards().size() - 1);
                    addCard(dealercan.getGraphicsContext2D(), dealer, x, 0, 75, 105);

                    score1.setText("Current score: " + dealer.valueProperty().get());
                }

                endGame();
            });
        }).start();
    }

    private void startNewGame() {
        start.setDisable(true);
        hit.setDisable(false);
        stay.setDisable(false);

        winmessage.setText("");
        playercan.getGraphicsContext2D().clearRect(0, 0, playercan.getWidth(), playercan.getHeight());
        dealercan.getGraphicsContext2D().clearRect(0, 0, dealercan.getWidth(), dealercan.getHeight());
        playable.set(true);
        message.setText("");

        deck.refill();

        dealer.reset();
        player.getHand().reset();

        dealer.takeCard(deck.drawCard());
        addCard(dealercan.getGraphicsContext2D(), dealer, 0, 0, 75, 105);
        dealer.takeCard(deck.drawCard());
        addCard(dealercan.getGraphicsContext2D(), dealer, 77, 0, 75, 105);
        score1.setText("Current score: " + dealer.valueProperty().get());

        player.getHand().takeCard(deck.drawCard());
        addCard(playercan.getGraphicsContext2D(), player.getHand(), 0, 0, 75, 105);
        player.getHand().takeCard(deck.drawCard());
        addCard(playercan.getGraphicsContext2D(), player.getHand(), 77, 0, 75, 105);
        score2.setText("Current score: " + player.getHand().valueProperty().get());
    }

    private void endGame() {
        playable.set(false);
        start.setDisable(false);
        hit.setDisable(true);
        stay.setDisable(true);

        int dealerValue = dealer.valueProperty().get();
        int playerValue = player.getHand().valueProperty().get();
        String winner = "Exceptional case: d: " + dealerValue + " p: " + playerValue;

        if (dealerValue == 21 || playerValue > 21 || dealerValue == playerValue
                || (dealerValue < 21 && dealerValue > playerValue)) {
            winner = "PLAYER1";
        }
        else if (playerValue == 21 || dealerValue > 21 || playerValue > dealerValue) {
            winner = "PLAYER2";
        }

        winmessage.setText(winner + " WON");
    }

    private void addCard(GraphicsContext gc, Hand hand, int v, int v1, int v2, int v3) {
        Image bgImage = null;
        gc.getTransform().appendScale(10, 30);
        if (hand.getCards().size() != 0) {
            bgImage = new Image(getClass().getResourceAsStream("../Images/" + hand.getCards().get(hand.getCards().size() - 1).suit.getSuit() + hand.getCards().get(hand.getCards().size() - 1).rank.getRank() + ".png"));
        }
        gc.drawImage(bgImage, v, v1, v2, v3);
    }
}
