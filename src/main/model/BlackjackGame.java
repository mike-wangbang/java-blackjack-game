package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// This class runs the Blackjack game

public class BlackjackGame extends CardMechanics implements Writable {

    private ArrayList<Card> deck;
    private final ArrayList<Card> dealerHand;
    private final Player player;

    // EFFECTS: initializes the game with the player, deck, and dealer's hand
    public BlackjackGame() {
        player = new Player(100);
        deck = new ArrayList<>();
        dealerHand = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: starts a new round, shuffling deck when necessary and dealing cards
    public void startNewRound() {
        checkShuffle();
        dealStartingCards();
    }

    // MODIFIES: this
    // EFFECTS: reshuffles the deck if it has reached a specific quantity
    public boolean checkShuffle() {
        if (deck.size() <= CardMechanics.NUM_CARDS_BEFORE_RESHUFFLE) {
            deck = generateDeck();
            return true;
        }
        return false;
    }

    // REQUIRES: deck is not empty
    // MODIFIES: this
    // EFFECTS: deals the starting cards to the player and dealer and removes those cards from deck
    public void dealStartingCards() {
        player.resetHand();
        dealerHand.clear();
        int i = 0;
        while (i < 2) {
            player.addCardToHand(deck.get(0));
            deck.remove(0);
            dealerHand.add(deck.get(0));
            deck.remove(0);
            i++;
        }
    }

    // EFFECTS: returns true if the player's hand value is 21
    public boolean checkBlackjack() {
        return player.getHandValue() == 21;
    }

    // MODIFIES: this
    // EFFECTS: process player's input to hit, stand, or double down on a round
    public boolean processAction(String action) {
        if (action.equals("h")) {
            player.addCardToHand(deck.get(0));
            deck.remove(0);
        } else if (action.equals("dd") && player.getChips() >= player.getBet()) {
            int doubledBet = player.getBet() * 2;
            player.subtractChips(player.getBet());
            player.setBet(doubledBet);
            player.addCardToHand(deck.get(0));
            deck.remove(0);
            return false;
        } else {
            return !action.equals("s");
        }
        return true;
    }

    // EFFECTS: computes the game's result after player has finished their actions
    public String doEnding(boolean isBust, boolean isBlackjack) {
        String result;
        if (isBust) {
            result = "Bust!";
        } else if (isBlackjack) {
            String actualResult = compareHands(player.getHandValue(), getCardsValue(dealerHand), 1.5);
            result = "BLACKJACK!!!" + " " + actualResult;
        } else {
            drawDealerHand();
            result = compareHands(player.getHandValue(), getCardsValue(dealerHand), 2);
        }
        return result;
    }

    // REQUIRES: deck is not empty
    // MODIFIES: this
    // EFFECTS: draw the dealer's hand until their value is >= 17
    public void drawDealerHand() {
        while (getCardsValue(dealerHand) < 17) {
            dealerHand.add(deck.get(0));
            deck.remove(0);
        }
    }

    // MODIFIES: this
    // EFFECTS: computes a win, loss, or draw based on the player and dealer's hands
    public String compareHands(int plHand, int deHand, double mult) {
        String result;
        if (deHand > 21 || plHand > deHand) {
            result = "You win!";
            player.addChips(player.getBet() * mult);
        } else if (plHand == deHand) {
            result = "Push";
            player.addChips(player.getBet());
        } else {
            result = "Dealer wins!";
        }
        player.setBet(0);
        return result;
    }

    // EFFECTS: returns the player state
    public Player getPlayer() {
        return player;
    }

    // EFFECTS: returns the current deck
    public ArrayList<Card> getDeck() {
        return deck;
    }

    // EFFECTS: returns the dealer's current hand
    public ArrayList<Card> getDealerHand() {
        return dealerHand;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("player", playerToJson());
        json.put("dealer", cardsToJson(dealerHand));
        json.put("deck", cardsToJson(deck));
        return json;
    }

    // EFFECTS: returns the player as a JSON object
    private JSONObject playerToJson() {
        JSONObject json = new JSONObject();
        json.put("chips",player.getChips());
        json.put("hand",cardsToJson(player.getHand()));
        json.put("bet",player.getBet());
        return json;
    }

    // EFFECTS: returns a list of cards as a JSONArray
    private JSONArray cardsToJson(ArrayList<Card> cards) {
        JSONArray json = new JSONArray();
        for (Card c:cards) {
            json.put(cardToJson(c));
        }
        return json;
    }

    // EFFECTS: returns a card as a JSONObject
    private JSONObject cardToJson(Card card) {
        JSONObject json = new JSONObject();
        json.put("rank",card.getRank());
        json.put("suit",card.getSuit());
        return json;
    }
}
