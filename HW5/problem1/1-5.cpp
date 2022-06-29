
struct CashBank{
    int fiveDollar = 0;
    int tenDollar = 0;
    int twentyDollar = 0;

    void receivePayment(int bill){
        switch(bill){
            case 5:
                fiveDollar++;
                break;
            case 10:
                tenDollar++;
                break;
            case 20:
                twentyDollar++;
                break;
        }
    }
    bool hasFiveDollarBills(int quantityNeeded){
        if (fiveDollar >= quantityNeeded) return true;
        else return false;
    }
    bool hasTenDollarBills(int quantityNeeded){
        if (tenDollar >= quantityNeeded) return true;
        else return false;
    }
    void giveChange(int amount){
        switch(amount){
            case 5:
                fiveDollar--;
                break;
            case 10:
                tenDollar--;
                break;
            case 20:
                twentyDollar--;
                break;
        }
    }
};


bool bibimbap_change(int* bills, int N) {

    // TODO: problem 1.5
    CashBank cashBank;
    bool hasEnoughChange = true;
    for (int i = 0; i < N; i++){
        cashBank.receivePayment(bills[i]);
        int change = bills[i] - 5;

        // if paid with $10: change = $5
        if (change == 0) continue;
        else if (change == 5){
            hasEnoughChange = cashBank.hasFiveDollarBills(1);
            if (hasEnoughChange) cashBank.giveChange(5);
        } else if (change == 15){
            bool option1 = (cashBank.hasFiveDollarBills(1) && cashBank.hasTenDollarBills(1));
            bool option2 = cashBank.hasFiveDollarBills(3);

            if (option1){
                hasEnoughChange = true;
                cashBank.giveChange(10);
                cashBank.giveChange(5);
            } else if (option2){
                hasEnoughChange = true;
                cashBank.giveChange(5);
                cashBank.giveChange(5);
                cashBank.giveChange(5);
            } else{
                hasEnoughChange = false;
            }
        }


        if (!hasEnoughChange) break;
    }
    return hasEnoughChange;
}

