package model;

public class DeckEmptyException extends Exception {
    public DeckEmptyException() {
        super("Deck was empty when accessed");
    }
}
