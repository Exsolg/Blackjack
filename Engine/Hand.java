package Engine;

import javafx.beans.property.SimpleIntegerProperty;
import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> cards;
    private SimpleIntegerProperty value = new SimpleIntegerProperty(0);

    private int aces = 0;

    public Hand(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void takeCard(Card card) {
        cards.add(card);

        if (card.rank == Rank.ACE) {
            aces++;
        }

        if (value.get() + card.value > 21 && aces > 0) {
            value.set(value.get() + card.value - 10);
            aces = 0;
        }

        else {
            value.set(value.get() + card.value);
        }
    }

    public void reset() {
        cards.clear();
        value.set(0);
        aces = 0;
    }

    public ArrayList<Card> getCards() { return cards; }

    public SimpleIntegerProperty valueProperty() {
        return value;
    }
}
