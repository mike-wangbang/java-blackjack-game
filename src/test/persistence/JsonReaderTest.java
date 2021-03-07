package persistence;

import model.BlackjackGame;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// This class contains tests for the JsonReader class
// Uses code from JsonSerializationDemo JsonReaderTest class

public class JsonReaderTest {

    @Test
    public void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/randomFile.json");
        try {
            BlackjackGame game = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testReaderEmptyBlackjackGame() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyBlackjackGame.json");
        try {
            BlackjackGame game = reader.read();
            assertEquals(0,game.getDeck().size());
            assertEquals(0,game.getDealerHand().size());
            assertEquals(0,game.getPlayer().getBet());
            assertEquals(0,game.getPlayer().getChips());
            assertEquals(0,game.getPlayer().getHand().size());
        } catch (IOException e) {
            fail ("Couldn't read from file");
        }
    }

    @Test
    public void testReaderGeneralBlackjackGame() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralBlackjackGame.json");
        try {
            BlackjackGame game = reader.read();
            assertEquals(2,game.getDeck().size());
            assertEquals("4",game.getDealerHand().get(0).getRank());
            assertEquals("diamond",game.getDealerHand().get(0).getSuit());
            assertEquals("Q",game.getDealerHand().get(1).getRank());
            assertEquals("heart",game.getDealerHand().get(1).getSuit());

            assertEquals(2,game.getDealerHand().size());
            assertEquals("6",game.getDeck().get(0).getRank());
            assertEquals("club",game.getDeck().get(0).getSuit());
            assertEquals("A",game.getDeck().get(1).getRank());
            assertEquals("spade",game.getDeck().get(1).getSuit());

            assertEquals(10,game.getPlayer().getBet());
            assertEquals(90,game.getPlayer().getChips());
            assertEquals("K",game.getPlayer().getHand().get(0).getRank());
            assertEquals("diamond",game.getPlayer().getHand().get(0).getSuit());
            assertEquals("2",game.getPlayer().getHand().get(1).getRank());
            assertEquals("spade",game.getPlayer().getHand().get(1).getSuit());
        } catch (IOException e) {
            fail ("Couldn't read from file");
        }
    }
}
