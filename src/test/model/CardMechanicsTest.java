package model;

// Test class for the CardMechanics class

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CardMechanicsTest extends CardMechanics {

    private ArrayList<Card> cards;
    private static final Card CARD1 = new Card("9","club");
    private static final Card CARD2 = new Card("Q","diamond");
    private static final Card ACE = new Card("A","spade");

    @BeforeEach
    public void runBefore() {
        cards = new ArrayList<>();
    }

    @Test
    public void testAddCardEmpty() {
        cards = addCard(cards,CARD1);
        assertEquals(1,cards.size());
        assertEquals("9",cards.get(0).getRank());
    }

    @Test
    public void testAddCardNotEmpty() {
        cards = addCard(cards,CARD2);
        cards = addCard(cards,CARD1);
        assertEquals(2,cards.size());
        assertEquals("Q",cards.get(0).getRank());
    }

    @Test
    public void testGetCardsValueWithAce() {
        cards = addCard(cards,ACE);
        cards = addCard(cards,CARD2);
        assertEquals(21,getCardsValue(cards));
    }

    @Test
    public void testGetCardsValueWithTwoAces() {
        cards = addCard(cards,ACE);
        cards = addCard(cards,CARD2);
        cards = addCard(cards,ACE);
        assertEquals(12,getCardsValue(cards));
    }

    @Test
    public void testCardsToStrings() {
        cards = addCard(cards,ACE);
        cards = addCard(cards,CARD1);

        String cardStringAce = ACE.cardToString();
        String cardString1 = CARD1.cardToString();
        int total = getCardsValue(cards);

        String result = " " + cardStringAce + " " + cardString1 + " (" + total + ")";

        assertEquals(result,cardsToStrings(cards));
    }

    @Test
    public void testGenerateDeck() {
        cards = generateDeck();
        assertEquals(104,cards.size());

    }
}
