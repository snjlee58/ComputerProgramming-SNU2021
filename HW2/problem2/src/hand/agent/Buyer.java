package hand.agent;

public class Buyer extends Agent {

    public Buyer(double maximumPrice) {
        super(maximumPrice);
    }

    @Override
    public boolean willTransact(double price) {
        //TODO: Problem 2.1
        if (!hadTransaction && price <= expectedPrice) return true;
        else return false;
    }

    @Override
    public void reflect() {
        //TODO: Problem 2.1
        if (hadTransaction) {
            // adjust expectedPrice
            expectedPrice -= adjustment;

            // update adjustment
            if (adjustment+5 <= adjustmentLimit) adjustment += 5;
            else adjustment = adjustmentLimit;
        }
        else {
            if (expectedPrice+adjustment <= priceLimit) {
                expectedPrice += adjustment;
                if (adjustment-5 >= 0) adjustment -= 5;
                else adjustment = 0;
            }
            else expectedPrice = priceLimit;
        }

        //reset hadTransaction for the day next
        hadTransaction = false;
    }
}
