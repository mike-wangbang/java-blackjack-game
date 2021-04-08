package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// This class handles some complex algorithms with card handling

public class CardMechanics {

    public static final List<String> SUITS = Arrays.asList("S", "H", "C", "D");
    public static final List<String> FACE_RANKS = Arrays.asList("J", "Q", "K", "A");
    private static final int NUM_DECKS = 2;
    private static final int NUM_SHUFFLES = 15;
    public static final int NUM_CARDS_BEFORE_RESHUFFLE = NUM_DECKS * 52 / 2;

    // MODIFIES: this
    // EFFECTS: creates a new deck of cards and shuffles it with NUM_DECKS decks used
    public ArrayList<Card> generateDeck() {
        ArrayList<Card> deck = new ArrayList<>();

        for (int i = 0; i < NUM_DECKS; i++) {
            for (String s : SUITS) {
                int n = 2;
                while (n <= 10) {
                    String rank = Integer.toString(n);
                    deck.add(new Card(rank, s));
                    n++;
                }
                for (String rank : FACE_RANKS) {
                    deck.add(new Card(rank, s));
                }
            }
        }
        return shuffleDeck(deck);
    }

    // MODIFIES: this
    // EFFECTS: shuffles deck NUM_SHUFFLES times
    public ArrayList<Card> shuffleDeck(ArrayList<Card> deck) {
        for (int i = 0; i < NUM_SHUFFLES; i++) {
            Collections.shuffle(deck);
        }
        return deck;
    }

//    // MODIFIES: this
//    // EFFECTS: returns a list of cards as a string with the cards' value
//    // (this method isn't used anymore in the GUI, it can be ignored)
//    public String cardsToStrings(ArrayList<Card> cards) {
//        String result = "";
//
//        for (Card c : cards) {
//            result = result + " " + c.cardToString();
//        }
//        result = result + " (" + getCardsValue(cards) + ")";
//        return result;
//    }

    // MODIFIES: this
    // EFFECTS: adds a card to a hand and returns it
    public ArrayList<Card> addCard(ArrayList<Card> cards, Card card) {
        cards.add(card);
        return cards;
    }

    // REQUIRES: aces can be soft (1 or 11)
    // MODIFIES: this
    // EFFECTS: computes the optimal value of a card hand and returns it (aces are reduced where possible)
    public int getCardsValue(ArrayList<Card> cards) {
        int count = 0;
        boolean hasAce = false;
        for (Card c : cards) {
            if (c.getRank().equals("A") && !hasAce) {
                count += 11;
                hasAce = true;
            } else {
                count += c.getValue();
            }
        }
        if (count > 21 && hasAce) {
            return count - 10;
        } else {
            return count;
        }
    }
}
