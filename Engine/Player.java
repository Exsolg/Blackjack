package Engine;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Player {
    String name;
    IntegerProperty bet = new SimpleIntegerProperty(0);

    public Player(String name) {
        this.name = name;
    }
}
