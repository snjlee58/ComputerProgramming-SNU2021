public class SquareTable {
    public static void printSquareTable(int n) {
        // DO NOT change the skeleton code.
        // You can add codes anywhere you want.
        int i = 1;

        while (i*i <= n) {
            printOneSquare(i, i*i);
            i++;
        }
    }

    private static void printOneSquare(int a, int b) {
        System.out.printf("%d times %d = %d\n", a, a, b);
    }
}
