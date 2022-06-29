#include "user.h"

User::User(std::string name, std::string password): name(name), password(password), purchaseSimilarity(0) {

}

User::User(std::string name, std::string password, bool premium) : User(name, password){
    this->premium = premium;
    if (premium){
        discountRate = 0.9;
    }
    else{
        discountRate = 1.0;
    }
}


std::string User::getPassword(){
    return password;
}

std::vector<Product*> User::getCart(){
    return cart;
}

void User::add_to_cart(Product* selectedProduct){
    cart.push_back(selectedProduct);
}

void User::add_purchase_history(Product* product){
    purchaseHistory.push_back(product);
}

void User::clearCart(){
    cart.clear();
}

std::vector<Product*> User::getPurchaseHistory(){
    return purchaseHistory;
}

bool User::comparePurchaseSimilarity(User* user1, User* user2){
    return user1->purchaseSimilarity < user2->purchaseSimilarity;
}



