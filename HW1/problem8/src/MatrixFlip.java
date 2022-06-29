public class MatrixFlip {
    public static void printFlippedMatrix(char[][] matrix) {
        // DO NOT change the skeleton code.
        // You can add codes anywhere you want.
        int rows = matrix.length;
        int columns = matrix[0].length;

        for (int i=rows-1; i>=0; i--) {
            for (int j=columns-1; j>=0; j--) {
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }


    }
}
