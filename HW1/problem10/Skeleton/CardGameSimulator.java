public class CardGameSimulator {
    private static final Player[] players = new Player[2];

    public static void simulateCardGame(String inputA, String inputB) {
        // DO NOT change the skeleton code.
        // You can add codes anywhere you want.


    }

    private static void printWinMessage(Player player) {
        System.out.printf("Player %s wins the game!\n", player);
    }
}


class Player {
    private String name;

    Player(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}

