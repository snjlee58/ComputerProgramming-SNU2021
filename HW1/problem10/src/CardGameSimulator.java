public class CardGameSimulator {
	private static final Player[] players = new Player[2];

	public static void simulateCardGame(String inputA, String inputB) {
		// DO NOT change the skeleton code.
		// You can add codes anywhere you want.

		// Initialize players
		players[0] = new Player("A");
		players[1] = new Player("B");

		// Initialize decks
		players[0].createDeck(inputA);
		players[1].createDeck(inputB);

		// Begin play; determine first card played by Player A
		Card prevCard = new Card();

		players[0].firstPlay(players[0].getDeck(), prevCard);

		boolean gameOver = false;
		int nextPlayer = 1, rounds=1;
		Player winner;
		while (!gameOver && rounds <=20) {
			gameOver = players[nextPlayer].selectCard(prevCard);
			rounds += 1;
			nextPlayer = (nextPlayer == 1)? 0: 1;
		}
		if (rounds <= 20) winner = players[nextPlayer];
		else winner = players[1];

		printWinMessage(winner);

	}

	private static void printWinMessage(Player player) {
		System.out.printf("Player %s wins the game!\n", player);
	}
}


class Player {
	private String name;
	private Card[] deck = new Card[10];

	Player(String name) {
		this.name = name;
	}

	public void createDeck(String input) {
		String[] deck = input.split(" ");
		for (int n=0; n < deck.length; n++) {
			Card card = new Card();
			card.setNumber(Character.getNumericValue(deck[n].charAt(0)));
			card.setShape(deck[n].charAt(1));

			this.addToDeck(n, card);
		}
	}

	// add cards to deck
	public void addToDeck(int index, Card card) {
		this.deck[index] = card;
	}

	// return deck
	public Card[] getDeck() {
		return this.deck;
	}

	public void firstPlay(Card[] playerAdeck, Card prevCard) {
		for (int i=9; i>=0; i--) {
			// Find largest number in deck
			for (Card card : playerAdeck) {
				if (card.getNumber() == i) {
					prevCard.setNumber(i);


					// Determine shape of card to play
					for (Card cardShape : playerAdeck) {
						if (cardShape.getNumber() == i && cardShape.getShape() == 'X') {
							prevCard.setShape('X');
							cardShape.invalid();
							this.playCard(prevCard);
							return;
						}
					}
					prevCard.setShape('O');
					this.playCard(prevCard);
					card.invalid();
					return;
				}
			}
		}
	}

	public boolean selectCard(Card prevCard) {
		// Look for card with same NUMBER as the previous player's card
		for (Card card : this.deck) {
			if (card.getNumber() == prevCard.getNumber()) {
				if (prevCard.getShape() == 'X') prevCard.setShape('O');
				else prevCard.setShape('X');

				playCard(prevCard);
				card.invalid();

				return false;
			}
		}

		// Look for card with same SHAPE, LARGEST number as the previous player's card
		for (int i = 9; i >= 0; i--) {
			for (Card card : this.deck) {
				if (card.getNumber() == i && card.getShape() == prevCard.getShape()) {
					prevCard.setNumber(i);

					playCard(card);
					card.invalid();
					return false;
				}
			}
		}

		return true;

	}


	public void playCard(Card card) {
		System.out.printf("Player %s: %s\n", name, card);
	}

	@Override
	public String toString() {
		return name;
	}
}


class Card {
	private int number;
	private char shape;

	// set card as invalid
	public void invalid() {
		this.number = -1;
		this.shape = 'I';
	}

	// initialize number
	public void setNumber(int number) {
		this.number = number;
	}

	// get number
	public int getNumber() {
		return this.number;
	}

	// initialize shape
	public void setShape(char shape) {
		this.shape = shape;
	}

	// return shape
	public char getShape() {
		return this.shape;
	}

	@Override
	public String toString() {
		return "" + number + shape;
	}
}
