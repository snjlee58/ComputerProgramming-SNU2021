public class CharacterCounter {
    public static void countCharacter(String str) {
        // DO NOT change the skeleton code.
        // You can add codes anywhere you want.

        char[] alphabet = {'A', 'a', 'B', 'b', 'C', 'c', 'D', 'd', 'E', 'e', 'F', 'f', 'G', 'g', 'H', 'h', 'I', 'i',
                'J', 'j', 'K', 'k', 'L', 'l', 'M', 'm', 'N', 'n', 'O', 'o', 'P', 'p', 'Q', 'q', 'R', 'r', 'S', 's', 'T', 't', 'U', 'u',
                'V', 'v', 'W', 'w', 'X', 'x', 'Y', 'y', 'Z', 'z'};
        int[] count = new int[alphabet.length];

        // Count frequency of each alphabet
        for (int j = 0; j < str.length(); j++) {
            for (int i = 0; i < alphabet.length; i++) {
                if (str.charAt(j) == alphabet[i]) {
                    count[i] += 1;
                    break;
                }
            }
        }

        // Find index of first alphabet count
        int startIndex = 0;
        for (int i = 0; i < alphabet.length; i++) {
            if (count[i] == 0) continue;
            else {
                printCount(alphabet[i], count[i]);
                startIndex = i;
                break;
            }
        }

        // Print frequency of the rest of the alphabets
        for (int i=startIndex+1; i<alphabet.length; i++) {
            if (count[i] == 0) continue;
            else if ((int)alphabet[i] - alphabet[startIndex] == 32) {
                System.out.print(", ");
                printCount(alphabet[i], count[i]);
            }
            else {
                System.out.println();
                printCount(alphabet[i], count[i]);
            }
            startIndex = i;
        }

    }

    private static void printCount(char character, int count){
        System.out.printf("%c: %d times", character, count);
    }

}
