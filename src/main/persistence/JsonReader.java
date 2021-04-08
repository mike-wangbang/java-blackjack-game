package persistence;

import model.BlackjackGame;
import model.Card;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.json.*;

// This class reads a game state from JSON data stored in .data folder
// uses code from JsonSerializationDemo JsonReader class
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: Reads BlackjackGame from file and returns it;
    // throws IOException if an error occurs reading data from the file

    public BlackjackGame read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseBlackjackGame(jsonObject);
    }

    // EFFECTS: reads source file as as string and returns it
    private String readFile(String source)  throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses workroom from JSON object and returns it
    private BlackjackGame parseBlackjackGame(JSONObject jsonObject) {
        BlackjackGame game = new BlackjackGame();
        addDealerHandAndDeck(game,jsonObject);
        addPlayer(game,jsonObject);
        return game;
    }

    // MODIFIES: game
    // EFFECTS: parses player from JSON object and adds it to game
    private void addPlayer(BlackjackGame game, JSONObject jsonObject) {
        JSONObject jsonObjectPlayer = jsonObject.getJSONObject("player");
        game.getPlayer().subtractChips(game.getPlayer().getChips());
        game.getPlayer().addChips(jsonObjectPlayer.getInt("chips"));
        game.getPlayer().setBet(jsonObjectPlayer.getInt("bet"));

        JSONArray jsonArrayHand = jsonObjectPlayer.getJSONArray("hand");
        for (Object json:jsonArrayHand) {
            JSONObject card = (JSONObject) json;
            game.getPlayer().addCardToHand(addCard(card));
        }
    }

    // MODIFIES: game
    // EFFECTS: parses deck and dealer from JSON object and adds it to game
    private void addDealerHandAndDeck(BlackjackGame game, JSONObject jsonObject) {
        JSONArray jsonArrayDeck = jsonObject.getJSONArray("deck");
        for (Object json:jsonArrayDeck) {
            JSONObject card = (JSONObject) json;
            game.getDeck().add(addCard(card));
        }
        JSONArray jsonArrayDealer = jsonObject.getJSONArray("dealer");
        for (Object json:jsonArrayDealer) {
            JSONObject card = (JSONObject) json;
            game.getDealerHand().add(addCard(card));
        }
    }

    // MODIFIES: game
    // EFFECTS: parses card from JSON object and returns it
    private Card addCard(JSONObject jsonObject) {
        Card card = null;
        String rank = jsonObject.getString("rank");
        String suit = jsonObject.getString("suit");
        try {
            card = new Card(rank,suit);
        } catch (Exception e) {
            System.err.println("Unable to create card from " + rank + suit);
        }
        return card;
    }
}
