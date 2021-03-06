package model;

// Tests for the Card class

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

        assertEquals("9\u2661",testCard1.cardToString());
        assertEquals("Q\u2662",testCard2.cardToString());
        assertEquals("A\u2660",testCard3.cardToString());
        assertEquals("4\u2663",testCard4.cardToString());

    }
}
