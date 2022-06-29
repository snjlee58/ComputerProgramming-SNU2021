package bank;

import bank.event.*;
import security.*;
import security.key.*;

public class Bank {
    private int numAccounts = 0;
    final static int maxAccounts = 100;
    private BankAccount[] accounts = new BankAccount[maxAccounts];
    private String[] ids = new String[maxAccounts];


    public void createAccount(String id, String password) {
        createAccount(id, password, 0);
    }

    public void createAccount(String id, String password, int initBalance) {
        //TODO: Problem 1.1

        // ignore request if inputted id already exists
        for (int i = 0; i < numAccounts; i++) {
            if (ids[i].equals(id)) return;
        }

        // create new bank account
        BankAccount newAccount = new BankAccount(id, password, initBalance);

        // record newly created account into 'accounts' and 'id' array
        accounts[numAccounts] = newAccount;
        ids[numAccounts] = id;
        numAccounts += 1;

    }


    public boolean deposit(String id, String password, int amount) {
        //TODO: Problem 1.1

        // authenticate id & password
        BankAccount account = find(id);

        if (account != null) {
            boolean authentication = account.authenticate(password);

            // execute event
            if (!authentication) return false;
            else {
                account.deposit(amount);
                return true;
            }
        }
        return false;
    }

    public boolean withdraw(String id, String password, int amount) {
        //TODO: Problem 1.1

        // authenticate id & password
        BankAccount account = find(id);

        if (account != null) {
            boolean authentication = account.authenticate(password);

            // execute event
            if (!authentication) return false;
            else {
                return account.withdraw(amount);
            }
        }
        return false;
    }

    public boolean transfer(String sourceId, String password, String targetId, int amount) {
        //TODO: Problem 1.1

        // find ids in BankAccounts[]
        BankAccount sourceAcc = find(sourceId);
        BankAccount targetAcc = find(targetId);

        // check if sourceId and targetId are valid
        if (sourceAcc == null || targetAcc == null) return false;
        else {
            // authentication of password of sourceAcc
            boolean authentication = sourceAcc.authenticate(password);

            // execute transfer event
            if (!authentication) return false;
            else {
                boolean success = sourceAcc.send(amount);

                if (success) {
                    targetAcc.receive(amount);
                    return true;
                }
                else return false;
            }
        }


    }

    public Event[] getEvents(String id, String password) {
        //TODO: Problem 1.1

        // authenticate id & password
        BankAccount account = find(id);
        boolean authentication = account.authenticate(password);

        // execute event
        if (!authentication) return null;
        else
            return account.getEvents();

    }

    public int getBalance(String id, String password) {
        //TODO: Problem 1.1

        // authenticate id & password
        BankAccount account = find(id);

        if (account != null) {
            boolean authentication = account.authenticate(password);
            // execute event
            if (!authentication) return -1;
            else return account.getBalance();
        }
        return -1;
    }



    private static String randomUniqueStringGen(){
        return Encryptor.randomUniqueStringGen();
    }

    // find BankAccount corresponding to id
    private BankAccount find(String id) {
        for (int i = 0; i < numAccounts; i++) {
            if(ids[i].equals(id)){return accounts[i];};
        }
        return null;
    }

    final static int maxSessionKey = 100;
    int numSessionKey = 0;
    String[] sessionKeyArr = new String[maxSessionKey];
    BankAccount[] bankAccountmap = new BankAccount[maxSessionKey];

    // generate unique sessionKey for BankAccount
    String generateSessionKey(String id, String password){
        BankAccount account = find(id);
        if(account == null || !account.authenticate(password)){
            return null;
        }
        String sessionkey = randomUniqueStringGen();
        sessionKeyArr[numSessionKey] = sessionkey;
        bankAccountmap[numSessionKey] = account;
        numSessionKey += 1;
        return sessionkey;
    }

    // find BankAccount with sessionkey
    BankAccount getAccount(String sessionkey){
        for(int i = 0 ;i < numSessionKey; i++){
            if(sessionKeyArr[i] != null && sessionKeyArr[i].equals(sessionkey)){
                return bankAccountmap[i];
            }
        }
        return null;
    }

    boolean deposit(String sessionkey, int amount) {
        //TODO: Problem 1.2
        BankAccount account = getAccount(sessionkey);
        account.deposit(amount);
        return true;
    }

    boolean withdraw(String sessionkey, int amount) {
        //TODO: Problem 1.2
        BankAccount account = getAccount(sessionkey);
        return account.withdraw(amount);
    }

    boolean transfer(String sessionkey, String targetId, int amount) {
        //TODO: Problem 1.2
        BankAccount sourceAcc = getAccount(sessionkey);
        BankAccount targetAcc = find(targetId);

        // authentication of targetId
        if (targetAcc == null) return false;

        // execute transfer event
        else {
            boolean success = sourceAcc.send(amount);
            if (success) {
                targetAcc.receive(amount);
                return true;
            }
            else return false;
        }
    }

    private BankSecretKey secretKey;
    public BankPublicKey getPublicKey(){
        BankKeyPair keypair = Encryptor.publicKeyGen(); // generates two keys : BankPublicKey, BankSecretKey
        secretKey = keypair.deckey; // stores BankSecretKey internally
        return keypair.enckey;
    }

    int maxHandshakes = 10000;
    int numSymmetrickeys = 0;
    BankSymmetricKey[] bankSymmetricKeys = new BankSymmetricKey[maxHandshakes];
    String[] AppIds = new String[maxHandshakes];

    public int getAppIdIndex(String AppId){
        for(int i=0; i<numSymmetrickeys; i++){
            if(AppIds[i].equals(AppId)){
                return i;
            }
        }
        return -1;
    }

    public void fetchSymKey(Encrypted<BankSymmetricKey> encryptedKey, String AppId){
        //TODO: Problem 1.3
        if (encryptedKey == null || encryptedKey.decrypt(secretKey) == null) {
            return;
        }
        else{
            if(getAppIdIndex(AppId) > -1) {
                bankSymmetricKeys[getAppIdIndex(AppId)] = encryptedKey.decrypt(secretKey);
            }
            else{
                bankSymmetricKeys[numSymmetrickeys] = encryptedKey.decrypt(secretKey);
                AppIds[numSymmetrickeys] = AppId;
                numSymmetrickeys += 1;
            }
        }

    }

    public Encrypted<Boolean> processRequest(Encrypted<Message> messageEnc, String AppId) {
        //TODO: Problem 1.3
        int appIdIndex = getAppIdIndex(AppId);

        if (appIdIndex == -1) return null;
        else{
            BankSymmetricKey bankSymmetricKey = bankSymmetricKeys[getAppIdIndex(AppId)];

            if (messageEnc == null || messageEnc.decrypt(bankSymmetricKey) == null) return null;
            else{
                Message message = messageEnc.decrypt(bankSymmetricKey);

                boolean success = false;

                if (message.getRequestType().equals("deposit")){
                    success = deposit(message.getId(), message.getPassword(), message.getAmount());
                }
                else if (message.getRequestType().equals("withdraw")){
                    success = withdraw(message.getId(), message.getPassword(), message.getAmount());
                }

                Encrypted encryptedResult = new Encrypted(success, bankSymmetricKey);

                return encryptedResult;
            }

        }

    }
}