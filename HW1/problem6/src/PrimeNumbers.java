public class PrimeNumbers {
    public static void printPrimeNumbers(int m, int n) {
        // DO NOT change the skeleton code.
        // You can add codes anywhere you want.
        boolean prime = true;
        for (int i=m; i<=n; i++) {
            prime = true;
            for (int k=2; k < i; k++) {
                if (i % k == 0) {
                    prime = false;
                    break;
                }
            }
            if (prime == true) {
                System.out.print(i + " ");
            }
        }

    }
}
