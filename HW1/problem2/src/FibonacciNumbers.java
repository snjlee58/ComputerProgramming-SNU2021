public class FibonacciNumbers {
    public static void printFibonacciNumbers(int n) {
        // DO NOT change the skeleton code.
        // You can add codes anywhere you want.


        // Calculate sum of first N Fibonacci Numbers
        int fibonacci[] = new int[n];
        int sum = 0;

        if (n == 1) {
            fibonacci[0] = 0;
            sum = 0;
        }
        if (n >= 2) {
            fibonacci[1] = 1;
            sum = 1;
        }
        if (n > 2) {
            for (int i=2; i < n; i++) {
                fibonacci[i] = fibonacci[i-2] + fibonacci[i-1]; //current term
                sum += fibonacci[i];
            }
        }


        // Print first N Fibonacci Numbers
        for (int i=0; i<fibonacci.length-1; i++) {
            System.out.print(fibonacci[i] + " ");
        }
        System.out.println(fibonacci[fibonacci.length-1]);


        // Print Sum or Last 5 digits of sum
        if (sum < 100000) {
            System.out.println("sum = " + sum);
        } else {
            int fiveDigits = sum % 100000;
            System.out.println("last five digits of sum = " + String.format("%05d", fiveDigits));
        }

    }
}
