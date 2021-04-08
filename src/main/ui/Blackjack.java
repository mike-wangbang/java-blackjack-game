package ui;

import model.BlackjackGame;
import model.Card;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

// This class handles the graphical user interface and player inputs

public class Blackjack implements ActionListener {

    private JFrame window;
    private JPanel startScreen;
    private JPanel gameScreen;
    private JPanel chipsScreen;
    private JLabel chipCounter;
    private static final String SAVE_LOCATION = "./data/blackjack.json";
    private static final Color DEFAULT_COLOR = new Color(0, 116, 0);

    private BlackjackGame game;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;
    private boolean isDeposit;

    // EFFECTS: initializes fields and JFrame for the application
    public Blackjack() {
        game = new BlackjackGame();
        jsonWriter = new JsonWriter(SAVE_LOCATION);
        jsonReader = new JsonReader(SAVE_LOCATION);
        isDeposit = false;
        createWindow();
    }

    // MODIFIES: this
    // EFFECTS: creates the primary JFrame window for the game
    private void createWindow() {
        window = new JFrame("u2s5d's Blackjack Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(DEFAULT_COLOR);
        window.setPreferredSize(new Dimension(1200, 800));
        window.setResizable(false);
        chipCounter = new JLabel("", JLabel.CENTER);
        chipCounter.setFont(chipCounter.getFont().deriveFont(36.0f));
        chipsScreen = new JPanel(new GridLayout(3, 1));
        createChipsScreen();
        startScreen = new JPanel(new GridLayout(4, 1));
        createStartScreen();
        gameScreen = new JPanel(new BorderLayout());
        createGameScreen(createActionPanel(),true);
        window.getContentPane().add(startScreen);
        window.pack();
        window.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: creates the start menu for the game
    public void createStartScreen() {
        startScreen.setBackground(DEFAULT_COLOR);
        createTitle();
        drawImage(startScreen, "./images/image.jpg",160,160);
        chipCounter.setText("You have " + game.getPlayer().getChips() + " chips");
        startScreen.add(chipCounter);
        createStartButtons();
    }

    // MODIFIES: this
    // EFFECTS: creates a title panel for the start screen
    public void createTitle() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(DEFAULT_COLOR);
        JLabel title = new JLabel("u2s5d's Blackjack Game!", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(72.0f));
        titlePanel.add(title);
        startScreen.add(titlePanel);
    }

    // MODIFIES: this
    // EFFECTS: creates the start menu buttons to perform various actions
    public void createStartButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(DEFAULT_COLOR);
        drawButton(buttonPanel, "Play", "play");
        drawButton(buttonPanel, "Deposit Chips", "d");
        drawButton(buttonPanel, "Withdraw Chips", "w");
        drawButton(buttonPanel, "Save Game State", "save");
        drawButton(buttonPanel, "Load Game State", "load");
        drawButton(buttonPanel, "Quit", "q");
        startScreen.add(buttonPanel);
    }

    // EFFECTS: draws an image to a JPanel scaled to the appropriate x and y
    // throws IOException if the image file was not found
    public void drawImage(JPanel panel, String path, int x, int y) {
        try {
            BufferedImage readImage = ImageIO.read(new File(path));
            ImageIcon imageIcon = new ImageIcon(readImage);
            Image oldImage = imageIcon.getImage();
            Image newImage = oldImage.getScaledInstance(x,y, java.awt.Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(newImage);
            JLabel imageLabel = new JLabel(imageIcon);
            panel.add(imageLabel);
        } catch (IOException e) {
            System.err.println("Unable to find image file");
        }
    }

    // MODIFIES: panel
    // EFFECTS: draws a button to the specified JPanel and adds the ActionListener and action to it
    public void drawButton(JPanel panel, String name, String action) {
        JButton button = new JButton(name);
        button.addActionListener(this);
        button.setActionCommand(action);
        panel.add(button);
    }

    // MODIFIES: this
    // EFFECTS: creates the menu for adding/subtracting chips
    public void createChipsScreen() {
        chipsScreen.setBackground(DEFAULT_COLOR);
        JLabel prompt = new JLabel("Select quantity of chips to deposit/withdraw/bet", JLabel.CENTER);
        prompt.setFont(prompt.getFont().deriveFont(48.0f));
        chipsScreen.add(prompt);
        chipCounter.setText("You have " + game.getPlayer().getChips() + " chips");
        chipsScreen.add(chipCounter);

        JPanel chipButtons = new JPanel();
        chipButtons.setBackground(DEFAULT_COLOR);
        drawButton(chipButtons, "$1", "1");
        drawButton(chipButtons, "$10", "10");
        drawButton(chipButtons, "$100", "100");
        drawButton(chipButtons, "$1000", "1000");
        drawButton(chipButtons, "Done", "done");
        chipsScreen.add(chipButtons);
    }

    // MODIFIES: this
    // EFFECTS: creates the menu where the game is played
    public void createGameScreen(JPanel middlePanel, boolean isHidden) {
        gameScreen.setBackground(DEFAULT_COLOR);
        JPanel cardsAndActionsPanel = new JPanel(new GridLayout(3, 1));
        cardsAndActionsPanel.setBackground(DEFAULT_COLOR);
        JPanel dealerPanel = createCardPanel(game.getDealerHand(),isHidden);
        JPanel playerPanel = createCardPanel(game.getPlayer().getHand(),false);
        dealerPanel.setBackground(DEFAULT_COLOR);
        playerPanel.setBackground(DEFAULT_COLOR);
        cardsAndActionsPanel.add(dealerPanel);
        cardsAndActionsPanel.add(playerPanel);
        cardsAndActionsPanel.add(middlePanel);
        gameScreen.add(cardsAndActionsPanel, BorderLayout.CENTER);
    }

    // EFFECTS: creates a panel to display card icons in the game
    //          isHidden allows for the panel to display a single turned-over card
    public JPanel createCardPanel(ArrayList<Card> cards, boolean isHidden) {
        JPanel cardPanel = new JPanel();
        if (isHidden && cards.size() >= 2) {
            drawImage(cardPanel,"./images/blue_back.png",130,200);
            renderCard(cardPanel,cards.get(1));
            cardPanel.add(new JLabel("(" + cards.get(1).getValue() + ")"));
        } else {
            for (Card card : cards) {
                renderCard(cardPanel,card);
            }
            cardPanel.add(new JLabel("(" + game.getCardsValue(cards) + ")"));
        }
        return cardPanel;
    }

    // EFFECTS: creates a panel to display a message and buttons when the game is over
    public JPanel createGameOverPanel(String result) {
        JPanel gameOverPanel = new JPanel();
        gameOverPanel.setBackground(DEFAULT_COLOR);
        gameOverPanel.add(new JLabel(result));
        drawButton(gameOverPanel, "Play Again", "y");
        drawButton(gameOverPanel, "Quit", "n");
        return gameOverPanel;
    }

    // EFFECTS: creates a panel with buttons for various actions in a game
    public JPanel createActionPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(DEFAULT_COLOR);
        drawButton(actionPanel, "Hit", "h");
        drawButton(actionPanel, "Stand", "s");
        drawButton(actionPanel, "Double Down", "dd");
        actionPanel.add(new JLabel("Bet: " + game.getPlayer().getBet()));
        actionPanel.add(new JLabel("Chips: " + game.getPlayer().getChips()));
        return actionPanel;
    }

    // MODIFIES: this
    // EFFECTS: processes button inputs
    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("play") || action.equals("save") || action.equals("load") || action.equals("q")) {
            handleStartActions(action);
        } else if (action.equals("d") || action.equals("w") || action.equals("done")) {
            handleDepositAndWithdraw(action);
        } else if (action.equals("1") || action.equals("10") || action.equals("100") || action.equals("1000")) {
            handleDepositWithdrawButtons(action);
        } else if (action.equals("h") || action.equals("dd") || action.equals("s")) {
            boolean keepGoing = game.processAction(action);
            String result;
            if (game.getPlayer().getHandValue() > 21) {
                result = game.doEnding(true,false);
                updateGameScreen(createGameOverPanel(result),false);
            } else if (!keepGoing) {
                result = game.doEnding(false, false);
                updateGameScreen(createGameOverPanel(result),false);
            } else {
                updateGameScreen(createActionPanel(),true);
            }
        } else if (action.equals("y") || action.equals("n")) {
            handleGameContinuation(action);
        }
    }

    // MODIFIES: this
    // EFFECTS: handles actions in the start menu to start a game, save/load a game, or quit
    public void handleStartActions(String action) {
        if (action.equals("play") && game.getPlayer().getChips() != 0) {
            startScreen.setVisible(false);
            window.getContentPane().remove(startScreen);
            window.getContentPane().add(chipsScreen);
            chipsScreen.setVisible(true);
            game.getPlayer().setBet(0);
            game.setGameRunning(true);
        } else if (action.equals("save")) {
            saveGame();
        } else if (action.equals("load")) {
            loadGame();
        } else {
            window.setVisible(false);
            window.dispose();
        }
    }

    // MODIFIES: this
    // EFFECTS: handles actions to direct the user to deposit/withdraw chips, or go back to the start menu
    public void handleDepositAndWithdraw(String action) {
        if (action.equals("d") || action.equals("w")) {
            isDeposit = action.equals("d");
            startScreen.setVisible(false);
            window.getContentPane().remove(startScreen);
            updateChipsScreen();
            window.getContentPane().add(chipsScreen);
            chipsScreen.setVisible(true);

        } else if (action.equals("done") && !game.isGameRunning() || game.getPlayer().getBet() == 0) {
            isDeposit = false;
            chipsScreen.setVisible(false);
            window.getContentPane().remove(chipsScreen);
            updateStartScreen();
            window.getContentPane().add(startScreen);
            startScreen.setVisible(true);
        } else {
            chipsScreen.setVisible(false);
            window.getContentPane().remove(chipsScreen);
            window.getContentPane().add(gameScreen);
            updateGameScreen(createActionPanel(),true);
            startNewGame();
        }
    }

    // MODIFIES: this
    // EFFECTS: handles actions to add or subtract chips from the player
    public void handleDepositWithdrawButtons(String action) {
        int amount = Integer.parseInt(action);
        if (!isDeposit && game.getPlayer().getChips() >= amount) {
            game.getPlayer().subtractChips(amount);
            game.getPlayer().setBet(game.getPlayer().getBet() + amount);
        } else if (isDeposit) {
            game.getPlayer().addChips(amount);
        }
        updateChipsScreen();
    }

    // MODIFIES: this
    // EFFECTS: handles actions to continue playing the game or quit to the start menu
    public void handleGameContinuation(String action) {
        if (action.equals("y") && game.getPlayer().getChips() > 0) {
            gameScreen.setVisible(false);
            window.getContentPane().remove(gameScreen);
            updateChipsScreen();
            window.getContentPane().add(chipsScreen);
            chipsScreen.setVisible(true);
        } else {
            game.setGameRunning(false);
            gameScreen.setVisible(false);
            window.getContentPane().remove(gameScreen);
            updateStartScreen();
            window.getContentPane().add(startScreen);
            startScreen.setVisible(true);
        }
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
            updateStartScreen();
        } catch (IOException e) {
            System.out.println("Unable to load game from " + SAVE_LOCATION);
        }
    }

    // MODIFIES: this
    // EFFECTS: starts a new round, then evaluates whether the player has a blackjack that
    // warrants the game to continue
    public void startNewGame() {
        game.startNewRound();
        updateGameScreen(createActionPanel(),true);
        if (game.checkBlackjack()) {
            String result = game.doEnding(false, true);
            updateGameScreen(createGameOverPanel(result),false);
        }

    }

    // MODIFIES: this
    // EFFECTS: updates the game screen
    public void updateGameScreen(JPanel panel, boolean isHidden) {
        gameScreen.setVisible(false);
        gameScreen.removeAll();
        gameScreen.revalidate();
        createGameScreen(panel,isHidden);
        gameScreen.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: updates the start menu
    public void updateStartScreen() {
        startScreen.setVisible(false);
        startScreen.removeAll();
        startScreen.revalidate();
        createStartScreen();
        startScreen.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: updates the chip selection menu
    public void updateChipsScreen() {
        chipsScreen.setVisible(false);
        chipsScreen.removeAll();
        chipsScreen.revalidate();
        createChipsScreen();
        chipsScreen.setVisible(true);
    }

    // REQUIRES: card leads to a valid file path to the image
    // EFFECTS: draws a card icon to the given JPanel object
    public void renderCard(JPanel panel, Card card) {
        String filePath = "./images/" + card.getRank() + card.getSuit() + ".png";
        drawImage(panel,filePath,130,200);
    }
}
