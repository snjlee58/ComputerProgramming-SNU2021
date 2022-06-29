package bank;

import security.key.BankPublicKey;
import security.key.BankSymmetricKey;
import security.*;

public class MobileApp {

    private String randomUniqueStringGen(){
        return Encryptor.randomUniqueStringGen();
    }

    private final String AppId = randomUniqueStringGen();

    public String getAppId() {
        return AppId;
    }

    String id, password;
    BankSymmetricKey bankSymmetricKey;
    public MobileApp(String id, String password){
        this.id = id;
        this.password = password;

        this.bankSymmetricKey = null;
    }

    public Encrypted<BankSymmetricKey> sendSymKey(BankPublicKey publickey){
        //TODO: Problem 1.3
        String symKey = randomUniqueStringGen();
        BankSymmetricKey bankSymmetricKey = new BankSymmetricKey(symKey);
        this.bankSymmetricKey = bankSymmetricKey;
        Encrypted encryptedBankSymKey = new Encrypted(bankSymmetricKey, publickey);
        return encryptedBankSymKey;
    }

    public Encrypted<Message> deposit(int amount){
        //TODO: Problem 1.3
        Message message = new Message("deposit", id, password, amount);
        Encrypted encryptedMessage = new Encrypted(message, bankSymmetricKey);
        return encryptedMessage;
    }

    public Encrypted<Message> withdraw(int amount){
        //TODO: Problem 1.3
        Message message = new Message("withdraw", id, password, amount);
        Encrypted encryptedMessage = new Encrypted(message, bankSymmetricKey);
        return encryptedMessage;
    }

    public boolean processResponse(Encrypted<Boolean> obj) {
        //TODO: Problem 1.3
        if (obj == null) return false;
        else {
            if (obj.decrypt(bankSymmetricKey) == null) return false;
            else return obj.decrypt(bankSymmetricKey);
        }
    }
}

