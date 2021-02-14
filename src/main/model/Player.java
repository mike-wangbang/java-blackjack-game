package model;

import java.util.ArrayList;


public class Player extends CardMechanics {

    private int chips;
    private ArrayList<Card> hand;

    // EFFECTS: Creates the player with no chips and an empty hand of cards
    public Player(int c) {

        this.chips = c;
        this.hand = new ArrayList<>();

    }

    // EFFECTS: Returns the amount of chips in the possession of the player
    public int getChips() {
        return chips;
    }

    // REQUIRES: c > 0
    // MODIFIES: this
    // EFFECTS: Adds the given quantity c chips to the player's count
    public void addChips(double c) {
        chips += (int) c;
    }

    // REQUIRES: c <= chips
    // MODIFIES: this
    // EFFECTS: Subtracts the given quantity c chips from the player's count
    public void subtractChips(double c) {
        chips -= c;
    }

    // EFFECTS: Returns the current hand of cards
    public ArrayList<Card> getHand() {
        return hand;
    }

    // REQUIRES: card is valid and pulled from the deck
    // MODIFIES: this
    // EFFECTS: Adds a card to the hand
    public void addCardToHand(Card card) {
        hand = addCard(hand, card);
    }

    // EFFECTS: returns the value of player's hand
    public int getHandValue() {
        return getCardsValue(hand);
    }

    // MODIFIES: this
    // EFFECTS: resets hand to an empty ArrayList
    public void resetHand() {
        hand.clear();
    }
}
