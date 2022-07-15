package hand.agent;

public class Buyer extends Agent {

    public Buyer(double maximumPrice) {
        super(maximumPrice);
    }

    @Override
    public boolean willTransact(double price) {
        //TODO: Problem 2.1

        return true;
    }

    @Override
    public void reflect() {
        //TODO: Problem 2.1

    }
}
