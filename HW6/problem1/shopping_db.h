#ifndef PROBLEM1_SHOPPING_DB_H
#define PROBLEM1_SHOPPING_DB_H

#include <string>
#include <vector>
#include "user.h"
#include "product.h"

class ShoppingDB {
public:
    ShoppingDB();
    void add_product(std::string name, int price);
    bool edit_product(std::string name, int price);
    bool productExists(std::string name);
    std::vector<Product*> getProducts();
    Product* findProduct(std::string name);

    void add_user(std::string username, std::string password, bool premium);
    User* authenticateUser(std::string username, std::string password);
    User* findUser(std::string username);
    std::vector<User*> getUsers();


private:
    std::vector<User*> users;
    std::vector<Product*> products;
};

#endif //PROBLEM1_SHOPPING_DB_H
