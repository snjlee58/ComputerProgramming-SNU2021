public class NumberCounter {
    public static void countNumbers(String str0, String str1, String str2) {
        // DO NOT change the skeleton code.
        // You can add codes anywhere you want.
        int i = Integer.parseInt(str0);
        int j = Integer.parseInt(str1);
        int k = Integer.parseInt(str2);

        int[] sumArray = new int[10];
        for (int a=0; a<10; a++){
            sumArray[a]=0;
        }

        int result = i * j * k;

        String multi = Integer.toString(result);

        for (int a=0; a<10; a++){
            for (int b=0; b<multi.length(); b++){
                int numberAt = (int)multi.charAt(b)-48;
                if(numberAt==a){
                    sumArray[a]=sumArray[a]+1;
                }
            }
            if (sumArray[a]!=0){
                printNumberCount(a, sumArray[a]);
            }
        }
        System.out.println("length: "+ multi.length());

    }

    private static void printNumberCount(int number, int count) {
        System.out.printf("%d: %d times\n", number, count);
    }

}

