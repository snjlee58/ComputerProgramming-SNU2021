package hand.market;

import hand.agent.Buyer;
import hand.agent.Seller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class Pair<K,V> {
    public K key;
    public V value;
    Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
}

public class Market {
    public ArrayList<Buyer> buyers;
    public ArrayList<Seller> sellers;

    private int numExchanges = 0;
    private double sumExchangePrices = 0.0;

    public Market(int nb, ArrayList<Double> fb, int ns, ArrayList<Double> fs) {
        buyers = createBuyers(nb, fb);
        sellers = createSellers(ns, fs);
    }
    
    private ArrayList<Buyer> createBuyers(int n, ArrayList<Double> f) {
        //TODO: Problem 2.3

        ArrayList<Buyer> newBuyers = new ArrayList<Buyer>();

        // for each new buyer
        for (int i=1; i<=n; i++){
            double priceLimit = 0.0;
            double f_x = (double)i/n;

            // calculate price limit using polynomial
            for (int j=0; j < f.size(); j++){
                priceLimit += f.get(j)*Math.pow(f_x, j);
            }

            // add new buyer to list
            Buyer newBuyer = new Buyer(priceLimit);
            newBuyers.add(newBuyer);
        }


        return newBuyers;
    }


    private ArrayList<Seller> createSellers(int n, ArrayList<Double> f) {
        //TODO: Problem 2.3
        ArrayList<Seller> newSellers = new ArrayList<Seller>();

        // for each new buyer
        for (int i=1; i<=n; i++){
            double priceLimit = 0;
            double f_x = (double)i/n;

            // calculate price limit using polynomial
            for (int j=0; j < f.size(); j++){
                priceLimit += f.get(j)*Math.pow(f_x, j);
            }

            // add new buyer to list
            Seller newSeller = new Seller(priceLimit);
            newSellers.add(newSeller);
        }


        return newSellers;
    }

    private ArrayList<Pair<Seller, Buyer>> matchedPairs(int day, int round) {
        ArrayList<Seller> shuffledSellers = new ArrayList<>(sellers);
        ArrayList<Buyer> shuffledBuyers = new ArrayList<>(buyers);
        Collections.shuffle(shuffledSellers, new Random(71 * day + 43 * round + 7));
        Collections.shuffle(shuffledBuyers, new Random(67 * day + 29 * round + 11));
        ArrayList<Pair<Seller, Buyer>> pairs = new ArrayList<>();
        for (int i = 0; i < shuffledBuyers.size(); i++) {
            if (i < shuffledSellers.size()) {
                pairs.add(new Pair<>(shuffledSellers.get(i), shuffledBuyers.get(i)));
            }
        }
        return pairs;
    }

    public double simulate() {
        //TODO: Problem 2.2 and 2.3
        for (int day = 1; day <= 3000; day++) { // do not change this line
            for (int round = 1; round <= 5; round++) { // do not change this line
                ArrayList<Pair<Seller, Buyer>> pairs = matchedPairs(day, round); // do not change this line

                // for each matched pair
                for (int i = 0; i < pairs.size(); i++) {
                    Seller seller = pairs.get(i).key;
                    Buyer buyer = pairs.get(i).value;

                    boolean buyerTransact = buyer.willTransact(seller.getExpectedPrice());

                    // make transaction if price satisfies buyer & seller hasn't transacted
                    if (buyerTransact && seller.willTransact(seller.getExpectedPrice())) {
                        buyer.makeTransaction();
                        seller.makeTransaction();

                        // update data to calculate average (TODO 2.3)
                        if (day == 3000){
                            numExchanges += 1;
                            sumExchangePrices += seller.getExpectedPrice();
                        }
                    }
                }
            }
            // reflect
            for (Buyer buyer : buyers) buyer.reflect();
            for (Seller seller : sellers) seller.reflect();
        }

        return sumExchangePrices/numExchanges;
    }
}

