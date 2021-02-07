package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    private Card testCard1;
    private Card testCard2;
    private Card testCard3;

    @BeforeEach
    public void runBefore() {
        this.testCard1 = new Card("9","heart");
        this.testCard2 = new Card("Q","diamond");
        this.testCard3 = new Card("A","spade");
    }

    @Test
    public void testGetCardRankAndSuit() {
        assertEquals("9",testCard1.getRank());
        assertEquals("heart",testCard1.getSuit());

        assertEquals("Q",testCard2.getRank());
        assertEquals("diamond",testCard2.getSuit());

        assertEquals("A",testCard3.getRank());
        assertEquals("spade",testCard3.getSuit());

    }

    @Test
    public void testNumberGetValue() {
        assertEquals(9,testCard1.getValue());

    }

    @Test
    public void testFaceGetValue() {
        assertEquals(10,testCard2.getValue());
        assertEquals(1,testCard3.getValue());
    }

    @Test
    public void testCardToString() {

        Card testCard4 = new Card("4","club");

        assertEquals("\u26619",testCard1.cardToString());
        assertEquals("\u2662Q",testCard2.cardToString());
        assertEquals("\u2660A",testCard3.cardToString());
        assertEquals("\u26634",testCard4.cardToString());

    }
}
