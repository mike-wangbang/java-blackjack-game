package model;

// This class represents a single playing card and its rank and suit

public class Card {

    private String rank;
    private String suit;

    // REQUIRES: - rank is one of: A, 2-10, J, Q, K
    //           - suit is one of: S (spade), H (heart), C (club), D (diamond)
    // EFFECTS: Creates a new instance of Card
    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    // EFFECTS: returns the card's rank as a String
    public String getRank() {
        return rank;
    }

    // EFFECTS: returns the card's suit as a String
    public String getSuit() {
        return suit;
    }

    // EFFECTS: returns the card's integer value (aces are assumed to be 1 for simplicity)
    public int getValue() {
        if (rank.equals("A")) {
            return 1;
        } else if (rank.equals("K") || rank.equals("Q") || rank.equals("J")) {
            return 10;
        } else {
            return Integer.parseInt(rank);
        }
    }

//    // EFFECTS: returns the card as a String with a Unicode suit symbol
//    // (this method isn't used anymore in the GUI, it can be ignored)
//    public String cardToString() {
//        char suitSymbol = 0;
//
//        switch (suit) {
//            case "S":
//                suitSymbol = (char) 9824;
//                break;
//            case "H":
//                suitSymbol = (char) 9825;
//                break;
//            case "C":
//                suitSymbol = (char) 9827;
//                break;
//            case "D":
//                suitSymbol = (char) 9826;
//                break;
//        }
//
//        return rank + suitSymbol;
//    }
}
