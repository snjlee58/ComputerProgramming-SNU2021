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
        this.id = id;
        this.password = password;
        this.balance = balance;

    }


    boolean authenticate(String password) {
        //TODO: Problem 1.1
        if (this.password.equals(password)) return true;
        else return false;
    }


    void deposit(int amount) {
        //TODO: Problem 1.1
        if (amount >= 0) {
            this.balance += amount;
            DepositEvent depositEvent = new DepositEvent();
            addEvent(depositEvent);
        }
    }

    boolean withdraw(int amount) {
        //TODO: Problem 1.1
        if (this.balance >= amount && amount >= 0) {
            this.balance -= amount;
            WithdrawEvent withdrawEvent = new WithdrawEvent();
            addEvent(withdrawEvent);

            return true;
        }
        else return false;
    }

    void receive(int amount) {
        //TODO: Problem 1.1
        if (amount >= 0) {
            this.balance += amount;
            ReceiveEvent receiveEvent = new ReceiveEvent();
            addEvent(receiveEvent);
        }
    }

    boolean send(int amount) {
        //TODO: Problem 1.1
        if (this.balance >= amount && amount >= 0) {
            this.balance -= amount;
            SendEvent sendEvent = new SendEvent();
            addEvent(sendEvent);
            return true;
        }
        else return false;
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
