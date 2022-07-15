package bank;

import bank.event.*;
import security.method.Withdraw;

class BankAccount {
    private Event[] events = new Event[maxEvents];
    final static int maxEvents = 100;

    private String id;
    private String password;
    private int balance;
    private int numEvents = 0;

    BankAccount(String id, String password, int balance) {
        //TODO: Problem 1.1

    }


    boolean authenticate(String password) {
        //TODO: Problem 1.1

        return true;
    }


    void deposit(int amount) {
        //TODO: Problem 1.1

    }

    boolean withdraw(int amount) {
        //TODO: Problem 1.1

        return true;
    }

    void receive(int amount) {
        //TODO: Problem 1.1

    }

    boolean send(int amount) {
        //TODO: Problem 1.1

        return true;
    }

    private void addEvent(Event event) {
        events[numEvents] = event;
        numEvents += 1;

    }

    public String getId() {
        return this.id;
    }

    public Event[] getEvents() {
        int eventLength = 0;
        for (int i = 0; i < maxEvents; i++) {
            if (events[i] == null) {
                eventLength = i;
                break;
            }

        }

        Event[] eventsFinal = new Event[eventLength];
        for (int i = 0; i < eventLength; i++) {
            eventsFinal[i] = events[i];
        }
        return eventsFinal;
    }

    public int getNumEvents() {
        return numEvents;
    }

    public int getBalance() {
        return balance;
    }

}
