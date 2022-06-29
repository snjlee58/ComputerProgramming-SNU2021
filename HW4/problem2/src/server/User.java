package server;

import course.Bidding;

import java.util.*;

public class User {
    public String userId;
    private int totalMileage;
    private List<Bidding> biddingsList;
    private Map<Integer, Bidding> biddingsMap;
    public int storeCurrBidding;

    public User(String userId){
        this.userId = userId;
        this.totalMileage = 0;
        this.biddingsList = new LinkedList<>();
        this.biddingsMap = new HashMap<>();
        this.storeCurrBidding = 0;
    }

    public int calcTotalMileage(List<Bidding> biddingList){
        this.totalMileage = 0;
        ListIterator<Bidding> bidIterator = biddingList.listIterator();
        while (bidIterator.hasNext()){
            this.totalMileage += bidIterator.next().mileage;
        }
        return this.totalMileage;
    }

    public int calcTotalMileage(){
        return calcTotalMileage(this.biddingsList);
    }

    public void setBiddings(List<Bidding> biddingsList){
        // set biddingsList
        this.biddingsList = biddingsList;

        // set biddingsMap
        Map<Integer, Bidding> biddingsMap = new HashMap<>();
        ListIterator<Bidding> biddingIterator = biddingsList.listIterator();
        while (biddingIterator.hasNext()){
            Bidding currBidding = biddingIterator.next();
            biddingsMap.put(currBidding.courseId, currBidding);
        }
        this.biddingsMap = biddingsMap;
    } // sets biddingsList and biddingsMap

    public Map<Integer, Bidding> getBiddingsMap(){ return this.biddingsMap; }

}
