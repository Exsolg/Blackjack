package Engine;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private String name;
    private Hand hand;
    private ArrayList<Card> playercards;
    private boolean isReady = false;

    public Player(String name) {
        this.name = name;
        playercards = new ArrayList<>();
        hand = new Hand(playercards);
    }

    public String getName() {
        return this.name;
    }

    public Hand getHand() {
        return this.hand;
    }

    public ArrayList<Card> getCards() {
        return this.playercards;
    }

    public void setReady(boolean a) {
        this.isReady = a;
    }

    public boolean getReady() {
        return isReady;
    }

    public Player copy() {
        Player newbie = new Player(this.name);
        newbie.playercards = this.playercards;
        newbie.isReady = this.isReady;
        newbie.hand = this.hand.copy();
        return newbie;
    }
}
