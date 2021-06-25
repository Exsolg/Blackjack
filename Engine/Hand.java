package Engine;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;

public class Hand implements Serializable {
    private ArrayList<Card> cards;
    private WriteableIntegerProperty value = new WriteableIntegerProperty(0);

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
        } else {
            value.set(value.get() + card.value);
        }
    }

    public void reset() {
        cards.clear();
        value.set(0);
        aces = 0;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public SimpleIntegerProperty valueProperty() {
        return value;
    }

    public Hand copy() {
        Hand newbie = new Hand(this.cards);
        newbie.value = this.value;
        newbie.aces = this.aces;
        return newbie;
    }
}

class WriteableIntegerProperty extends SimpleIntegerProperty implements Serializable {

    public WriteableIntegerProperty(int i) {
        super(i);
    }

    private void writeObject(ObjectOutputStream s) throws IOException, IOException {
        s.defaultWriteObject();
        s.writeObject(get());
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        set((int) s.readObject());
    }
}
