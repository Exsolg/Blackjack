package Engine;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

public class Player {
    private String name;
    private Hand hand;
    private ArrayList<Card> playercards;
    private IntegerProperty bet = new SimpleIntegerProperty(0);

    public Player(String name) {
        this.name = name;
        playercards = new ArrayList<>();
        hand = new Hand(playercards);
    }

    public String getName() { return this.name; }
    public Hand getHand() { return this.hand; }
    public ArrayList<Card> getCards() { return  this.playercards; }
}
