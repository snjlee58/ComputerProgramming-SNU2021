package bank;

public class Session {

    private String sessionKey;
    private Bank bank;
    private boolean valid;
    private int transLimit = 3;

    private int numEvents = 0;

    Session(String sessionKey,Bank bank){
        this.sessionKey = sessionKey;
        this.bank = bank;
        valid = true;
    }

    public boolean deposit(int amount) {
        //TODO: Problem 1.2

        if (!valid) return false;
        else {
            numEvents += 1;
            boolean success = bank.deposit(sessionKey, amount);
            if (getNumEvents() >= transLimit) valid = false;
            return success;
        }
    }

    public boolean withdraw(int amount) {
        //TODO: Problem 1.2
        if (!valid) return false;

        else {
            numEvents += 1;
            boolean success = bank.withdraw(sessionKey, amount);
            if (getNumEvents() >= transLimit) valid = false;
            return success;
        }
    }

    public boolean transfer(String targetId, int amount) {
        //TODO: Problem 1.2
        if (!valid) return false;
        else {
            numEvents += 1;
            boolean success = bank.transfer(sessionKey, targetId, amount);
            if (getNumEvents() >= transLimit) valid = false;
            return success;
        }
    }

    public void setInvalid() {
        valid = false;
    }

    public int getNumEvents() {
        return numEvents;
    }
}
