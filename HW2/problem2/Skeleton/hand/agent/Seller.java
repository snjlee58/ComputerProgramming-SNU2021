package hand.agent;

public class Seller extends Agent {

    public Seller(double minimumPrice) {
        super(minimumPrice);
    }

    @Override
    public boolean willTransact(double price) {
        //TODO: Problem 2.1

        return true;
    }

    @Override
    public void reflect() {
        // TODO: Problem 2.1

    }
}
