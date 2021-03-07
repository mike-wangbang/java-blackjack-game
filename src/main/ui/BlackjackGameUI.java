package ui;

import model.BlackjackGame;
import model.Card;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// this class handles the basic user interface and input of the game
// uses code from the JsonSerializationDemo WorkRoomApp class

public class BlackjackGameUI {

    private static final String SAVE_LOCATION = "./data/blackjack.json";
    private BlackjackGame game;
    private final Scanner input;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    // EFFECTS: initializes the game and scanner input, then starts the UI
    public BlackjackGameUI() {
        game = new BlackjackGame();
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(SAVE_LOCATION);
        jsonReader = new JsonReader(SAVE_LOCATION);
        startScreen();
    }

    // MODIFIES: this
    // EFFECTS: asks the player to withdraw/deposit chips, or start a game
    public void startScreen() {

        boolean keepGoing = true;
        System.out.println("\nWelcome to Blackjack!");

        while (keepGoing) {
            int chipCount = game.getPlayer().getChips();
            System.out.println("You have " + chipCount + " chips");
            System.out.println("\nSelect from:");
            System.out.println("\td -> deposit chips");
            System.out.println("\tw -> withdraw chips");
            System.out.println("\tplay -> start a game");
            System.out.println("\tsave -> save the current game");
            System.out.println("\tload -> load a saved game state");
            System.out.println("\tq -> quit");
            keepGoing = processCommand(chipCount);

        }
    }

    // MODIFIES: this
    // EFFECTS: processes the player's command
    public boolean processCommand(int chipCount) {
        String command = input.next().toLowerCase();

        if (command.equals("d")) {
            depositChips();
        } else if (command.equals("w")) {
            withdrawChips("How many chips to withdraw? ");
        } else if (command.equals("play") && chipCount != 0) {
            runGame();
            return false;
        } else if (command.equals("q")) {
            System.out.println("Hope you enjoyed playing!");
            return false;
        } else if (command.equals("save")) {
            saveGame();
        } else if (command.equals("load")) {
            loadGame();
        } else {
            System.out.println("Your input is invalid or you don't have enough chips to start a game");
        }
        return true;
    }

    // MODIFIES: this
    // EFFECTS: runs the game and manages user input
    public void runGame() {
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
            displayGame(game.getDealerHand(), result);
            if (!keepPlaying()) {
                break;
            }
        }
        if (game.getPlayer().getChips() == 0) {
            System.out.println("\nOut of chips! Game over");
        }
        startScreen();
    }

    public boolean keepPlaying() {
        while (true) {
            System.out.println("Keep playing? y -> YES, n -> RETURN TO MENU");
            String command = input.next().toLowerCase();

            if (command.equals("y")) {
                return true;
            } else if (command.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid action");
            }
        }
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
            System.out.println("\nYou have " + game.getPlayer().getChips() + " chips");
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
        displayGame(hiddenHand, "h = HIT, s = STAND, dd = DOUBLE DOWN");
    }

    // EFFECTS: displays the current game state
    public void displayGame(ArrayList<Card> d, String status) {
        System.out.println(status);
        System.out.println("Dealer:" + game.cardsToStrings(d));
        System.out.println("You:" + game.cardsToStrings(game.getPlayer().getHand()));
    }

    // MODIFIES: this
    // EFFECTS: saves current game to file
    public void saveGame() {
        try {
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();
            System.out.println("Saved the game to " + SAVE_LOCATION);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to save game to " + SAVE_LOCATION);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads game from file
    public void loadGame() {
        try {
            game = jsonReader.read();
            System.out.println("Loaded game from " + SAVE_LOCATION);
        } catch (IOException e) {
            System.out.println("Unable to load game from " + SAVE_LOCATION);
        }
    }
}
