public class FractionalNumberCalculator {
	static int[] operation(FractionalNumber frac1, FractionalNumber frac2, char op) {
		int numerator=1, denominator=1, GCF=1;
		switch(op) {
			case '+':
				numerator = (frac1.numerator * frac2.denominator) + (frac2.numerator * frac1.denominator);
				denominator = frac1.denominator * frac2.denominator;
				GCF = FractionalNumber.GCF(numerator, denominator);

				numerator /= GCF;
				denominator /= GCF;

				break;

			case '-':
				numerator = (frac1.numerator * frac2.denominator) - (frac2.numerator * frac1.denominator);
				denominator = frac1.denominator * frac2.denominator;
				GCF = FractionalNumber.GCF(numerator, denominator);

				numerator /= GCF;
				denominator /= GCF;
				break;

			case '*':
				numerator = frac1.numerator * frac2.numerator;
				denominator = frac1.denominator * frac2.denominator;
				GCF = FractionalNumber.GCF(numerator, denominator);

				numerator /= GCF;
				denominator /= GCF;
				break;

			case '/':
				numerator = frac1.numerator * frac2.denominator;
				denominator = frac1.denominator * frac2.numerator;
				GCF = FractionalNumber.GCF(numerator, denominator);

				numerator /= GCF;
				denominator /= GCF;
				break;
		}
		int[] ans = {numerator, denominator};
		return ans;
	}

	static String formatResult(int numerator, int denominator) {
		String answer = "";
		if (denominator == 1) {
			answer = Integer.toString(numerator);
		}
		else {
			answer = Integer.toString(numerator) + "/" + Integer.toString(denominator);
		}
		return answer;
	}

	public static void printCalculationResult(String equation) {
		// DO NOT change the skeleton code.
		// You can add codes anywhere you want.

		int space1 = equation.indexOf(" ");
		int space2 = equation.lastIndexOf(" ");

		String num1 = equation.substring(0, space1);
		FractionalNumber frac1 = new FractionalNumber();
		frac1.toFraction(num1);

		String num2 = equation.substring(space2+1);
		FractionalNumber frac2 = new FractionalNumber();
		frac2.toFraction(num2);

		char op = equation.charAt(space1+1);

		int result[] = operation(frac1, frac2, op);

		String answer = formatResult(result[0], result[1]);
		System.out.println(answer);

	}
}

class FractionalNumber {
	int numerator;
	int denominator;

	void toFraction(String stringFraction) {
		if (stringFraction.contains("/")) {
			int slashIndex = stringFraction.indexOf("/");
			this.numerator = Integer.parseInt(stringFraction.substring(0, slashIndex));
			this.denominator = Integer.parseInt(stringFraction.substring(slashIndex+1));
		}
		else {
			this.numerator = Integer.parseInt(stringFraction);
			this.denominator = 1;
		}

	}

	static int GCF(int numerator, int denominator) {
		if (numerator < 0) numerator *= -1; // make numerator positive to get positive GCF
		if (denominator == 0){
			return numerator;
		} else {
			return GCF(denominator, numerator%denominator);
		}

	}
}
