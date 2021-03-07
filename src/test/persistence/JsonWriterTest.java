package persistence;

import model.BlackjackGame;
import model.Card;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// This class contains tests for the JsonWriter class
// Uses code from JsonSerializationDemo JsonWriterTest class

public class JsonWriterTest {

    @Test
    public void testWriterInvalidFile() {
        try {
            BlackjackGame game = new BlackjackGame();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testWriterEmptyBlackjackGame() {
        try {
            BlackjackGame game = new BlackjackGame();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyBlackjackGame.json");
            writer.open();
            writer.write(game);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyBlackjackGame.json");
            game = reader.read();
            assertEquals(0, game.getDeck().size());
            assertEquals(0, game.getDealerHand().size());
            assertEquals(0, game.getPlayer().getBet());
            assertEquals(100, game.getPlayer().getChips());
            assertEquals(0, game.getPlayer().getHand().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testWriterGeneralBlackjackGame() {
        try {
            BlackjackGame game = new BlackjackGame();
            game.getDeck().add(new Card("7","club"));
            game.getDealerHand().add(new Card("5","spade"));
            game.getPlayer().setBet(20);
            game.getPlayer().addChips(10);
            game.getPlayer().addCardToHand(new Card("K","heart"));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralBlackjackGame.json");
            writer.open();
            writer.write(game);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralBlackjackGame.json");
            game = reader.read();
            assertEquals(1,game.getDeck().size());
            assertEquals("5",game.getDealerHand().get(0).getRank());
            assertEquals("spade",game.getDealerHand().get(0).getSuit());

            assertEquals(1,game.getDealerHand().size());
            assertEquals("7",game.getDeck().get(0).getRank());
            assertEquals("club",game.getDeck().get(0).getSuit());

            assertEquals(20,game.getPlayer().getBet());
            assertEquals(110,game.getPlayer().getChips());
            assertEquals("K",game.getPlayer().getHand().get(0).getRank());
            assertEquals("heart",game.getPlayer().getHand().get(0).getSuit());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }


}
