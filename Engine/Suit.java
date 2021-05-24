package Engine;

public enum Suit {
    HEARTS("hearts"), DIAMONDS("diamonds"), CLUBS("clubs"), SPADES("spades");

    final String suit;

    Suit(String suit) {
        this.suit = suit;
    }

    public String getSuit() { return this.suit; }
}
