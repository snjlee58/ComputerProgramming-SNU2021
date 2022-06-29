public class DrawingFigure {
    public static void drawFigure(int n) {
        // DO NOT change the skeleton code.
        // You can add codes anywhere you want.

        // Print top half (row 1 ~ row n+1)
        for (int i=1; i<=n+1; i++) {
            for (int j=1; j<=(n+1-i)*2; j++) {
                System.out.print(" ");
            }
            for (int j=1; j<=i*2-2; j++) {
                System.out.print("* ");
            }
            System.out.print("*");

            for (int j=1; j<=(n+1-i)*2; j++) {
                System.out.print(" ");
            }
            System.out.println();
        }

        // Print bottom half
        for (int i=n; 0<i; i--) {
            for (int j=1; j<=(n+1-i)*2; j++) {
                System.out.print(" ");
            }
            for (int j=1; j<=i*2-2; j++) {
                System.out.print("* ");
            }
            System.out.print("*");

            for (int j=1; j<=(n+1-i)*2; j++) {
                System.out.print(" ");
            }
            System.out.println();
        }

    }
}
