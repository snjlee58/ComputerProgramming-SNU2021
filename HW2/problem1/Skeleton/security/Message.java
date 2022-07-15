package security;

public class Message {
    private String id;
    private String password;
    private String requestType;
    private int amount;

    public Message(String requestType, String id, String password, int amount){
        this.requestType = requestType; // "deposit" or "withdraw"
        this.id = id; // authentication info of customer
        this.password = password; // authentication info of customer
        this.amount = amount; // argument for DEPOSIT and WITHDRAW calls
    }

    public int getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getRequestType() {
        return requestType;
    }
}
