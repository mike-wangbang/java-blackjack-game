package ui;


import model.Card;
import model.CardMechanics;
import model.Player;

import java.util.ArrayList;
import java.util.Scanner;

public class BlackjackGame extends CardMechanics {

    private ArrayList<Card> deck;
    private ArrayList<Card> dealerHand;
    private Player player;
    private Scanner input;

    // EFFECTS: runs the blackjack game
    public BlackjackGame() {
        startGame();
    }

    // EFFECTS: initializes fields, displays a start screen
    public void startGame() {
        init();
        startScreen();
    }

    // MODIFIES: this
    // EFFECTS: initializes the player, deck, and scanner
    public void init() {
        player = new Player(100);
        deck = new ArrayList<>();
        dealerHand = new ArrayList<>();
        input = new Scanner(System.in);
    }

    // MODIFIES: this
    // EFFECTS: asks the player to withdraw/deposit chips, or start a game
    public void startScreen() {

        boolean keepGoing = true;
        System.out.println("\n\nWelcome to Blackjack!");

        while (keepGoing) {
            System.out.println("\nYou have " + player.getChips() + " chips");
            System.out.println("Type d to deposit, w to withdraw, anything to play");

            String command = input.next().toLowerCase();
            if (command.equals("d")) {
                depositChips();
            } else if (command.equals("w")) {
                withdrawChips("How much money to withdraw? ");
            } else {
                if (player.getChips() == 0) {
                    System.out.println("You can't start a game with 0 chips!");
                } else {
                    keepGoing = false;
                }
            }
        }
        runGame();
    }

    // MODIFIES: this
    // EFFECTS: runs the game and manages user input
    public void runGame() {
        deck.clear();
        while (player.getChips() != 0) {
            checkShuffle();
            int bet = bet();
            dealStartingCards();
            boolean isBlackjack = false;
            boolean isBust = false;

            if (checkBlackjack()) {
                isBlackjack = true;
            } else {
                isBust = doPlayerAction();
            }
            doEnding(isBust, isBlackjack, bet);
        }
        System.out.println("\nOut of chips! Game over");
        startScreen();
    }

    // MODIFIES: this
    // EFFECTS: reshuffles the deck if it has reached a specific quantity
    public void checkShuffle() {
        if (deck.size() <= CardMechanics.NUM_CARDS_BEFORE_RESHUFFLE) {
            System.out.println("\nShuffling!");
            deck = generateDeck();
        }
    }

    // REQUIRES: user input is an integer
    // EFFECTS: sets the bet to be used in a game round
    public int bet() {
        boolean keepGoing = true;
        int amount = 0;

        while (keepGoing) {
            System.out.println("\n\nYou have " + player.getChips() + " chips");
            amount = withdrawChips("Select quantity to bet: ");
            if (amount > 0) {
                keepGoing = false;
            }
        }
        return amount;
    }

    // MODIFIES: this
    // EFFECTS: deals the starting cards to the player and dealer; removes those cards from deck
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

    // EFFECTS: displays a round, processes player's input to hit or stand
    public boolean doPlayerAction() {
        boolean keepGoing = true;
        boolean isBust = false;

        while (keepGoing) {
            displayGameWithHiddenHand(dealerHand);
            keepGoing = processAction();
            if (player.getHandValue() > 21) {
                isBust = true;
                keepGoing = false;
            }
        }
        return isBust;
    }

    // MODIFIES: this
    // EFFECTS: process player's input to hit or stand on a round
    public boolean processAction() {
        System.out.println("\nh = HIT, s = STAND");
        String action = input.next().toLowerCase();
        if (action.equals("h")) {
            player.addCardToHand(deck.get(0));
            deck.remove(0);
        } else if (action.equals("s")) {
            return false;
        } else {
            System.out.println("Invalid action!!!");
        }
        return true;
    }

    // EFFECTS: computes the game's result after player has finished their actions
    public void doEnding(boolean isBust, boolean isBlackjack, int bet) {
        if (isBust) {
            System.out.println("Bust!");
        } else if (isBlackjack) {
            System.out.println("Blackjack!!!");
            compareHands(player.getHandValue(), getCardsValue(dealerHand), bet, 1.5);
        } else {
            drawDealerHand();
            compareHands(player.getHandValue(), getCardsValue(dealerHand), bet,2);
        }
        displayGame(dealerHand);
    }

    // MODIFIES: this
    // EFFECTS: draw the dealer's hand until their value is >= 17
    public void drawDealerHand() {
        while (getCardsValue(dealerHand) < 17) {
            dealerHand.add(deck.get(0));
            deck.remove(0);
        }
    }

    // REQUIRES: player and dealer's hands are complete & valid
    // MODIFIES: this
    // EFFECTS: computes a win, loss, or draw based on the player and dealer's hands
    public void compareHands(int plHand, int deHand, int bet, double mult) {
        if (deHand > 21 || plHand > deHand) {
            System.out.println("You win!");
            player.addChips(bet * mult);
        } else if (plHand == deHand) {
            System.out.println("Push");
            player.addChips(bet);
        } else {
            System.out.println("Dealer wins!");
        }
    }

    // REQUIRES: user input is an integer
    // MODIFIES: this
    // EFFECTS: adds quantity of chips read from user input
    public void depositChips() {
        System.out.println("\nHow much to deposit?");
        int amount = input.nextInt();

        if (amount >= 0) {
            player.addChips(amount);
        } else {
            System.out.println("Invalid amount to deposit");
        }
    }

    // REQUIRES: user input is an integer
    // MODIFIES: this
    // EFFECTS: subtracts quantity of chips read from user input
    public int withdrawChips(String usage) {
        System.out.println("\n" + usage);
        int amount = input.nextInt();

        if (amount > player.getChips() || amount <= 0) {
            System.out.println("Invalid amount");
        } else {
            player.subtractChips(amount);
            return amount;
        }
        return 0;
    }

    // EFFECTS: displays a round with first dealer's card hidden
    public void displayGameWithHiddenHand(ArrayList<Card> d) {
        ArrayList<Card> hiddenHand = new ArrayList<>();
        hiddenHand.add(d.get(1));
        displayGame(hiddenHand);
    }

    // EFFECTS: displays the current game state
    public void displayGame(ArrayList<Card> d) {
        System.out.println("Dealer:" + cardsToStrings(d));
        System.out.println("You:" + cardsToStrings(player.getHand()));
    }

}
