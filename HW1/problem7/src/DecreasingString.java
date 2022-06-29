public class DecreasingString {
    public static void printLongestDecreasingSubstringLength(String inputString) {
        // DO NOT change the skeleton code.
        // You can add codes anywhere you want.

        int substring = 1, longest = 1;
        for (int i=1; i<inputString.length(); i++) {
            if (inputString.charAt(i-1) > inputString.charAt((i))) {
                substring++;
            }
            else {
                if (substring > longest) {
                    longest = substring;
                }
                substring = 1;
            }
        }
        if (substring > longest) {
            longest = substring;
            substring = 1;
        }
        System.out.println(longest);


    }
}
