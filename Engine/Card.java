package Engine;

public class Card {
    public final Suit suit;
    public final Rank rank;
    public final int value;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.value = rank.value;
    }

    @Override
    public String toString() {
        return rank.toString() + " of " + suit.toString();
    }
}
