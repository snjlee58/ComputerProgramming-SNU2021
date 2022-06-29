#include <vector>
#include "client_ui.h"
#include "product.h"
#include "user.h"

//added by me
#include <cmath>
#include <algorithm>

ClientUI::ClientUI(ShoppingDB &db, std::ostream& os) : UI(db, os), current_user() { }

void ClientUI::signup(std::string username, std::string password, bool premium) {
    // TODO: For problem 1-2
    db.add_user(username, password, premium);
    get_os() << "CLIENT_UI: " << username << " is signed up." << std::endl;
}

void ClientUI::login(std::string username, std::string password) {
    // TODO: For problem 1-2

    // if there's a user currently logged in
    if (current_user != nullptr){
        get_os() << "CLIENT_UI: Please logout first." << std::endl;
    }

    // Attempt login
    else{
        User* targetUser = db.authenticateUser(username, password);
        if (targetUser != nullptr){
            current_user = targetUser;
            get_os() << "CLIENT_UI: " << current_user->name << " is logged in." << std::endl;
        }
        else{
            get_os() << "CLIENT_UI: Invalid username or password." << std::endl;
        }
    }
}

void ClientUI::logout() {
    // TODO: For problem 1-2

    // If there is no user logged in
    if (current_user == nullptr){
        get_os() << "CLIENT_UI: There is no logged-in user." << std::endl;
    }
    // Log out user
    else{
        get_os() << "CLIENT_UI: " << current_user->name << " is logged out." << std::endl;
        current_user = nullptr;
    }
}

bool ClientUI::loginExists(){
    if (current_user == nullptr){
        get_os() << "CLIENT_UI: Please login first." << std::endl;
        return false;
    }
    else return true;
}

void ClientUI::add_to_cart(std::string product_name) {
    // TODO: For problem 1-2

    // Checked if a user is logged in
    if (!loginExists()){
        return;
    }

    // Perform task if user exists
    Product* selectedProduct = db.findProduct(product_name);
    if (selectedProduct != nullptr){
        current_user->add_to_cart(selectedProduct);
        get_os() << "CLIENT_UI: " << selectedProduct->name << " is added to the cart." << std::endl;
    }
    else {
        get_os() << "CLIENT_UI: Invalid product name." << std::endl;
    }
}

void ClientUI::list_cart_products() {
    // TODO: For problem 1-2.

    // Checked if a user is logged in
    if (!loginExists()){
        return;
    }

    // Print list of products
    std::vector<Product*> cart = current_user->getCart();

    get_os() << "CLIENT_UI: Cart: [";

    for (Product* cartProduct : cart){
        if (cartProduct != cart.front()){
            get_os() << ", ";
        }

        std::string name = cartProduct->name;
        double price = cartProduct->price * current_user->discountRate;

        get_os() << "(" << name << ", " << std::round(price) << ")";
    }

    get_os() << "]" << std::endl;
}

void ClientUI::buy_all_in_cart() {
    // TODO: For problem 1-2

    // Checked if a user is logged in
    if (!loginExists()){
        return;
    }

    // Add each to purchase history, then buy
    std::vector<Product*> cart = current_user->getCart();
    double totalPrice = 0;

    for (Product* purchaseProduct : cart){
        current_user->add_purchase_history(purchaseProduct);
        totalPrice += purchaseProduct->price * current_user->discountRate;
    }

    get_os() << "CLIENT_UI: Cart purchase completed. Total price: " << totalPrice << "." << std::endl;
    current_user->clearCart();
    }

void ClientUI::buy(std::string product_name) {
    // TODO: For problem 1-2

    // Checked if a user is logged in
    if (!loginExists()){
        return;
    }

    // add to purchase history, then buy product
    Product* purchaseProduct = db.findProduct(product_name);
    if (purchaseProduct != nullptr){
        current_user->add_purchase_history(purchaseProduct);
        double price = purchaseProduct->price * current_user->discountRate;
        get_os() << "CLIENT_UI: Purchase completed. Price: " << price << "." << std::endl;
    }
    else{
        get_os() << "CLIENT_UI: Invalid product name." << std::endl;
    }
}

void printSimilarity(std::vector<User*> users){
    for (User* user : users){
        std::cout << user->name << ":" << user->purchaseSimilarity << " "; //delete
    }
    std::cout << std::endl;
} // //delete (test purpose)

void ClientUI::listRecommendedProducts(const std::vector<Product*> recommendedProducts){
    get_os() << "CLIENT_UI: Recommended products: [";

    for (Product* recommendedProduct : recommendedProducts){
        if (recommendedProduct != recommendedProducts.front()){
            get_os() << ", ";
        }

        std::string name = recommendedProduct->name;
        double price = recommendedProduct->price * current_user->discountRate;
        get_os() << "(" << name << ", " << std::round(price) << ")";
    }

    get_os() << "]" << std::endl;

}


void ClientUI::recommend_products() {
    // TODO: For problem 1-3.

    // Checked if a user is logged in
    if (!loginExists()){
        return;
    }

    // Recommend product for NORMAL USERS
    if (current_user->premium == false){
        std::vector<Product*> recommendedProducts;
        std::vector<Product*> purchaseHistory = current_user->getPurchaseHistory();
        std::reverse(purchaseHistory.begin(), purchaseHistory.end());
        std::vector<Product*>::iterator purchaseHistPtr = purchaseHistory.begin();

        while (purchaseHistPtr != purchaseHistory.end() && recommendedProducts.size() < 3){
            Product* purchase = *purchaseHistPtr;

            if(std::find(recommendedProducts.begin(), recommendedProducts.end(), purchase) == recommendedProducts.end()){
                recommendedProducts.push_back(purchase);
            }
            purchaseHistPtr++;

        }

        // Print list of recommended products
        listRecommendedProducts(recommendedProducts);


    }
        // Recommend product for PREMIUM USERS
    else{
        // Create list of users excluding current user
        std::vector<User*> users = db.getUsers();

        std::vector<User*>::iterator position = std::find(users.begin(), users.end(), db.findUser(current_user->name));
        if (position != users.end())
            users.erase(position);

        // Find purchase history similarity of each user
        for (auto itr = users.begin(); itr != users.end(); ++itr){
            User* user = *itr;
            std::vector<Product*> currUserPurchaseHistory = current_user->getPurchaseHistory();
            for (Product* product : user->getPurchaseHistory()){
                if(std::find(currUserPurchaseHistory.begin(), currUserPurchaseHistory.end(), product) != currUserPurchaseHistory.end()) {
                    user->purchaseSimilarity++;
                }
            }
        }

        // Sort users
        std::sort(users.begin(), users.end(), User::comparePurchaseSimilarity);
        std::reverse(users.begin(), users.end());

        // Get list of recommended products
        std::vector<Product*> recommendedProducts;

        std::vector<User*>::iterator userPtr = users.begin();
        while (userPtr != users.end() && recommendedProducts.size() < 3){
            User* user = *userPtr;

            if (user->getPurchaseHistory().size() == 0) break;

            if(std::find(recommendedProducts.begin(), recommendedProducts.end(), user->getPurchaseHistory().back()) == recommendedProducts.end()) {
                recommendedProducts.push_back(user->getPurchaseHistory().back());
            }
            userPtr++;
        }

        // Print list of recommended products
        listRecommendedProducts(recommendedProducts);

    }


}



