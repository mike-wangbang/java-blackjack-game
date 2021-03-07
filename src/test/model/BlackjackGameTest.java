package model;

import org.json.JSONObject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Test class for the BlackjackGame class
public class BlackjackGameTest extends BlackjackGame {

    private BlackjackGame game;

    @BeforeEach
    public void runBefore() {
        game = new BlackjackGame();
    }

    @Test
    public void testConstructorInitialization() {
        assertEquals(100,game.getPlayer().getChips());
        assertEquals(0,game.getDeck().size());
        assertEquals(0,game.getDealerHand().size());
    }

    @Test
    public void testCheckShuffleEmptyDeck() {
        game.checkShuffle();
        assertEquals(104,game.getDeck().size());
    }

    @Test
    public void testCheckShuffleHalfDecks() {
        for (int i = 0; i < 53; i++) {
            game.getDeck().add(new Card("A","spade"));
        }
        assertEquals(53,game.getDeck().size());
        assertFalse(game.checkShuffle());
        game.getDeck().remove(0);
        assertTrue(game.checkShuffle());
        assertEquals(104,game.getDeck().size());
    }

    @Test
    public void testDealStartingCards() {
        for (int i = 0; i < 2; i++) {
            game.getDeck().add(new Card("A","spade"));
            game.getDeck().add(new Card("4","diamond"));
        }
        game.dealStartingCards();
        for (Card c:game.getPlayer().getHand()) {
            assertEquals("A",c.getRank());
            assertEquals("spade",c.getSuit());
        }
        for (Card c:game.getDealerHand()) {
            assertEquals("4",c.getRank());
            assertEquals("diamond",c.getSuit());
        }
        assertEquals(0,game.getDeck().size());
    }

    @Test
    public void testStartNewRound() {
        game.startNewRound();
        assertEquals(2,game.getPlayer().getHand().size());
        assertEquals(2,game.getDealerHand().size());
        assertEquals(100,game.getDeck().size());
    }

    @Test
    public void testCheckBlackjackFalse() {
        game.getPlayer().addCardToHand(new Card("A","spade"));
        game.getPlayer().addCardToHand(new Card("9","club"));
        assertEquals(20,game.getPlayer().getHandValue());
        assertFalse(game.checkBlackjack());
    }

    @Test
    public void testCheckBlackjackTrue() {
        game.getPlayer().addCardToHand(new Card("A","spade"));
        game.getPlayer().addCardToHand(new Card("Q","club"));
        assertEquals(21,game.getPlayer().getHandValue());
        assertTrue(game.checkBlackjack());
    }

    @Test
    public void testProcessActionHit() {
        game.startNewRound();
        game.processAction("h");
        assertEquals(3,game.getPlayer().getHand().size());
        assertEquals(99,game.getDeck().size());
    }

    @Test
    public void testProcessActionDoubleDownValid() {
        game.startNewRound();
        game.getPlayer().setBet(50);
        game.getPlayer().subtractChips(50);
        assertFalse(game.processAction("dd"));
        assertEquals(100,game.getPlayer().getBet());
        assertEquals(3,game.getPlayer().getHand().size());
        assertEquals(0,game.getPlayer().getChips());
    }

    @Test
    public void testProcessActionDoubleDownInvalid() {
        game.startNewRound();
        game.getPlayer().setBet(51);
        game.getPlayer().subtractChips(51);
        assertTrue(game.processAction("dd"));
        assertEquals(51,game.getPlayer().getBet());
        assertEquals(2,game.getPlayer().getHand().size());
        assertEquals(49,game.getPlayer().getChips());
    }

    @Test
    public void testProcessActionStand() {
        game.startNewRound();
        assertFalse(game.processAction("s"));
        assertEquals(2,game.getPlayer().getHand().size());
        assertEquals(100,game.getDeck().size());
    }

    @Test
    public void testProcessActionOther() {
        game.startNewRound();
        assertTrue(game.processAction("foobar"));
        assertEquals(2,game.getPlayer().getHand().size());
        assertEquals(100,game.getDeck().size());
    }

    @Test
    public void testDrawDealerHandOver17() {
        game.getDealerHand().add(new Card("10","club"));
        game.getDealerHand().add(new Card("8","heart"));
        game.getDeck().add(new Card("5","diamond"));

        game.drawDealerHand();
        assertEquals(18,game.getCardsValue(game.getDealerHand()));
        assertEquals(1,game.getDeck().size());
    }

    @Test
    public void testDrawDealerHandAt17() {
        game.getDealerHand().add(new Card("A","club"));
        game.getDealerHand().add(new Card("6","heart"));
        game.getDeck().add(new Card("5","diamond"));

        game.drawDealerHand();
        assertEquals(17,game.getCardsValue(game.getDealerHand()));
        assertEquals(1,game.getDeck().size());
    }

    @Test
    public void testDrawDealerHandOnce() {
        game.getDealerHand().add(new Card("10","club"));
        game.getDealerHand().add(new Card("4","heart"));
        game.getDeck().add(new Card("3","diamond"));

        game.drawDealerHand();
        assertEquals(17,game.getCardsValue(game.getDealerHand()));
        assertEquals(0,game.getDeck().size());
    }

    @Test
    public void testDrawDealerHandSeveral() {
        game.getDealerHand().add(new Card("7","club"));
        game.getDealerHand().add(new Card("4","heart"));
        game.getDeck().add(new Card("3","diamond"));
        game.getDeck().add(new Card("7","diamond"));

        game.drawDealerHand();
        assertEquals(21,game.getCardsValue(game.getDealerHand()));
        assertEquals(0,game.getDeck().size());
    }

    @Test
    public void testCompareHandsWin() {
        game.getPlayer().addCardToHand(new Card("9","spade"));
        game.getPlayer().addCardToHand(new Card("K","club"));
        game.getDealerHand().add(new Card ("10","heart"));
        game.getDealerHand().add(new Card ("8","heart"));
        int plHand = game.getPlayer().getHandValue();
        int deHand = game.getCardsValue(game.getDealerHand());
        game.getPlayer().setBet(30);
        assertEquals("You win!",game.compareHands(plHand,deHand,2));
        assertEquals(160,game.getPlayer().getChips());
    }

    @Test
    public void testCompareHandsDealerBust() {
        game.getPlayer().addCardToHand(new Card("9","spade"));
        game.getPlayer().addCardToHand(new Card("K","club"));
        game.getDealerHand().add(new Card ("10","heart"));
        game.getDealerHand().add(new Card ("8","heart"));
        game.getDealerHand().add(new Card ("Q","spade"));
        int plHand = game.getPlayer().getHandValue();
        int deHand = game.getCardsValue(game.getDealerHand());
        game.getPlayer().setBet(30);
        assertEquals("You win!",game.compareHands(plHand,deHand,2));
        assertEquals(160,game.getPlayer().getChips());
    }

    @Test
    public void testCompareHandsEqual() {
        game.getPlayer().addCardToHand(new Card("9","spade"));
        game.getPlayer().addCardToHand(new Card("K","club"));
        game.getDealerHand().add(new Card ("10","heart"));
        game.getDealerHand().add(new Card ("9","heart"));
        int plHand = game.getPlayer().getHandValue();
        int deHand = game.getCardsValue(game.getDealerHand());
        game.getPlayer().setBet(30);
        assertEquals("Push",game.compareHands(plHand,deHand,2));
        assertEquals(130,game.getPlayer().getChips());
    }

    @Test
    public void testCompareHandsDealerWins() {
        game.getPlayer().addCardToHand(new Card("9","spade"));
        game.getPlayer().addCardToHand(new Card("K","club"));
        game.getDealerHand().add(new Card ("10","heart"));
        game.getDealerHand().add(new Card ("A","heart"));
        int plHand = game.getPlayer().getHandValue();
        int deHand = game.getCardsValue(game.getDealerHand());
        game.getPlayer().setBet(30);
        assertEquals("Dealer wins!",game.compareHands(plHand,deHand,2));
        assertEquals(100,game.getPlayer().getChips());
    }

    @Test
    public void testDoEndingBust() {
        assertEquals("Bust!",game.doEnding(true,false));
    }

    @Test
    public void testDoEndingBlackjack() {
        game.getPlayer().addCardToHand(new Card("A","spade"));
        game.getPlayer().addCardToHand(new Card("K","club"));
        game.getDealerHand().add(new Card ("10","heart"));
        game.getDealerHand().add(new Card ("8","heart"));
        assertEquals("BLACKJACK!!! You win!",game.doEnding(false,true));
    }

    @Test
    public void testDoEndingElse() {
        game.getPlayer().addCardToHand(new Card("9","spade"));
        game.getPlayer().addCardToHand(new Card("K","club"));
        game.getDealerHand().add(new Card ("10","heart"));
        game.getDealerHand().add(new Card ("8","heart"));
        assertEquals("You win!",game.doEnding(false,false));
    }

    @Test
    public void testToJson() {
        JSONObject jsonTest = new JSONObject();
        jsonTest.put("player", playerToJson());
        jsonTest.put("dealer", cardsToJson(game.getDealerHand()));
        jsonTest.put("deck", cardsToJson(game.getDeck()));
        assertEquals(jsonTest.toString(),game.toJson().toString());
    }
}
