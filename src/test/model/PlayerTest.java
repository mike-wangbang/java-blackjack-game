package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player testPlayer;
    private static final int STARTING_CHIPS = 100;
    private static final Card CARD1 = new Card("9","club");
    private static final Card CARD2 = new Card("Q","diamond");

    @BeforeEach
    public void runBefore() {
        testPlayer = new Player(STARTING_CHIPS);
    }

    @Test
    public void testGetChips() {
        assertEquals(STARTING_CHIPS,testPlayer.getChips());
    }

    @Test
    public void testAddChips() {
        testPlayer.addChips(STARTING_CHIPS);
        assertEquals(STARTING_CHIPS * 2,testPlayer.getChips());
    }

    @Test
    public void testSubtractChips() {
        testPlayer.subtractChips(STARTING_CHIPS / 2);
        assertEquals(STARTING_CHIPS / 2,testPlayer.getChips());
    }

    @Test
    public void testAddCardToEmptyHand() {
        testPlayer.addCardToHand(CARD1);
        assertEquals(1,testPlayer.getHand().size());
        assertEquals(9,testPlayer.getHandValue());
    }

    @Test
    public void testAddCardToHand() {
        testPlayer.addCardToHand(CARD2);
        testPlayer.addCardToHand(CARD1);
        assertEquals(2,testPlayer.getHand().size());
        assertEquals(19,testPlayer.getHandValue());
    }

    @Test
    public void testResetHand() {
        testPlayer.addCardToHand(CARD1);
        testPlayer.addCardToHand(CARD2);
        assertEquals(2,testPlayer.getHand().size());
        testPlayer.resetHand();
        assertEquals(0,testPlayer.getHand().size());
        assertEquals(0,testPlayer.getHandValue());
    }


}
