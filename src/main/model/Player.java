package model;

import java.util.ArrayList;


public class Player {

    private int chips;
    private ArrayList<Card> hand;

    // EFFECTS: Creates the player with no chips and an empty hand of cards
    public Player(int c) {

        this.chips = c;
        this.hand = new ArrayList<Card>();

    }

    // EFFECTS: Returns the amount of chips in the possession of the player
    public int getChips() {
        return chips;
    }

    // REQUIRES: c > 0
    // MODIFIES: this
    // EFFECTS: Adds the given quantity c chips to the player's count
    public void addChips(int c) {
        chips += c;
    }

    // REQUIRES: c <= chips
    // MODIFIES: this
    // EFFECTS: Subtracts the given quantity c chips from the player's count
    public void subtractChips(int c) {
        chips -= c;
    }

    // EFFECTS: Returns the current hand of cards
    public ArrayList<Card> getHand() {
        return hand;
    }

    // REQUIRES: card is valid and pulled from the deck
    // MODIFIES: this
    // EFFECTS: Adds a card to the hand, then computes the new value of the hand.
    public void addCardToHand(Card card) {
        hand.add(card);
    }

    // EFFECTS: returns the value of player's hand
    public int getHandValue() {
        int count = 0;
        for (Card c:hand) {
            count += c.getValue();
        }
        return count;
    }

    // MODIFIES: this
    // EFFECTS: resets hand to an empty ArrayList
    public void resetHand() {
        hand.clear();
    }
}
