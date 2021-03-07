package ui;

import model.BlackjackGame;
import model.Card;

import java.util.ArrayList;
import java.util.Scanner;

// this class handles the basic user interface and input of the game

public class BlackjackGameUI {

    private BlackjackGame game;
    private Scanner input;

    // EFFECTS: initializes the game and scanner input, then starts the UI
    public BlackjackGameUI() {
        game = new BlackjackGame();
        input = new Scanner(System.in);
        startScreen();
    }

    // MODIFIES: this
    // EFFECTS: asks the player to withdraw/deposit chips, or start a game
    public void startScreen() {

        boolean keepGoing = true;
        System.out.println("\n\nWelcome to Blackjack!");

        while (keepGoing) {
            int chipCount = game.getPlayer().getChips();
            System.out.println("\nYou have " + chipCount + " chips");
            System.out.println("Type d to deposit, w to withdraw, anything to play");

            String command = input.next().toLowerCase();
            if (command.equals("d")) {
                depositChips();
            } else if (command.equals("w")) {
                withdrawChips("How much money to withdraw? ");
            } else {
                if (chipCount == 0) {
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
        game.getDeck().clear();
        while (game.getPlayer().getChips() != 0) {
            game.startNewRound();
            game.getPlayer().setBet(bet());
            boolean isBlackjack = false;
            boolean isBust = false;

            if (game.checkBlackjack()) {
                isBlackjack = true;
            } else {
                isBust = doPlayerAction();
            }
            String result = game.doEnding(isBust, isBlackjack);
            displayGame(game.getDealerHand(),result);
        }
        System.out.println("\nOut of chips! Game over");
        startScreen();
    }

    // EFFECTS: displays a round, processes player's input to hit or stand
    public boolean doPlayerAction() {
        boolean keepGoing = true;
        boolean isBust = false;

        while (keepGoing) {
            displayGameWithHiddenHand(game.getDealerHand());
            String action = input.next();
            keepGoing = game.processAction(action);
            if (game.getPlayer().getHandValue() > 21) {
                isBust = true;
                keepGoing = false;
            }
        }
        return isBust;
    }

    // REQUIRES: user input is an integer
    // EFFECTS: sets the bet to be used in a game round
    public int bet() {
        boolean keepGoing = true;
        int amount = 0;

        while (keepGoing) {
            System.out.println("\n\nYou have " + game.getPlayer().getChips() + " chips");
            amount = withdrawChips("Select quantity to bet: ");
            if (amount > 0) {
                keepGoing = false;
            }
        }
        return amount;
    }

    // REQUIRES: user input is an integer
    // MODIFIES: this
    // EFFECTS: adds quantity of chips read from user input
    public void depositChips() {
        System.out.println("\nHow much to deposit?");
        int amount = input.nextInt();

        if (amount >= 0) {
            game.getPlayer().addChips(amount);
        } else {
            System.out.println("Invalid amount");
        }
    }

    // REQUIRES: user input is an integer
    // MODIFIES: this
    // EFFECTS: subtracts quantity of chips read from user input
    public int withdrawChips(String usage) {
        System.out.println("\n" + usage);
        int amount = input.nextInt();

        if (amount > game.getPlayer().getChips() || amount <= 0) {
            System.out.println("Invalid amount");
        } else {
            game.getPlayer().subtractChips(amount);
            return amount;
        }
        return 0;
    }

    // EFFECTS: displays a round with first dealer's card hidden
    public void displayGameWithHiddenHand(ArrayList<Card> d) {
        ArrayList<Card> hiddenHand = new ArrayList<>();
        hiddenHand.add(d.get(1));
        displayGame(hiddenHand,"h = HIT, s = STAND, dd = DOUBLE DOWN");
    }

    // EFFECTS: displays the current game state
    public void displayGame(ArrayList<Card> d, String status) {
        System.out.println(status);
        System.out.println("Dealer:" + game.cardsToStrings(d));
        System.out.println("You:" + game.cardsToStrings(game.getPlayer().getHand()));
    }
}
