# Java Blackjack Game

## u2s5d's CPSC 210 Project

This project is a simplified version of the popular casino game *Blackjack*. The premise of the game involves
placing a bet and drawing cards to reach a total value of 21. As the rules of *Blackjack* are fairly simple,
anyone is able to play this version of the game, and there will not be any real money used. This project interests
me because I have always been fascinated by the logic and probability found in casino games, so I wanted to 
design an implementation of one game in Java. I already know how to play *Blackjack*, so I think I will have fun
working on this project.

## User Stories:

 - As a user, I want to be able to create a game and input a quantity of money to be played.
 - As a user, I want to be able to set a bet at the beginning of each round.
 - As a user, I want to be able to receive cards as a starting hand; playing against the dealer.
 - As a user, I want to be able to perform various actions in Blackjack, such as hit, stand, double down (split might be added later)
 - As a user, I want to be able to win or lose money depending on the value of my current hand.
 - As a user, I want to be able to save a game that is in progress, including chips
 - As a user, I want to be able to load an existing game and resume it
 
 ## Phase 4: Task 2
 
 I chose to design a class to be robust. The BlackjackGame class was reworked to be robust with the drawDealerHand and doEnding methods. drawDealerHand will throw a DeckEmptyException to handle the deck emptying before the dealer has finished drawing cards.
 
 ## Phase 4: Task 3
 
 - Represent the range of ranks and suits of a card as enums
 - Use a map of possible card combinations to generate a "deck", rather than have a very large ArrayList of cards
 - Remove the extends relationship between Player and CardMechanics to reduce coupling
 - Simplify the implementation of the Blackjack (UI) class by not requiring a Card import from model, and other optimizations to move more game logic to the BlackjackGame class 