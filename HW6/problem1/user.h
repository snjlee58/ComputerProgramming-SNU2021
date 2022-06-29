#ifndef PROBLEM1_USER_H
#define PROBLEM1_USER_H

#include <string>
#include <vector>
#include "product.h"

class User {
public:
    User(std::string name, std::string password);
    User(std::string name, std::string password, bool premium);
    const std::string name;
    bool premium;
    double discountRate;
    std::string getPassword();
    std::vector<Product*> getCart();
    void add_to_cart(Product* selectedProduct);
    void add_purchase_history(Product* product);
    void clearCart();
    std::vector<Product*> getPurchaseHistory();
    int purchaseSimilarity;
    static bool comparePurchaseSimilarity(User* user1, User* user2);
private:
    std::string password;
    std::vector<Product*> cart;
    std::vector<Product*> purchaseHistory;


};

class NormalUser : public User {
};

class PremiumUser : public User {
};

#endif //PROBLEM1_USER_H
