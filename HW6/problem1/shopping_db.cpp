#include "shopping_db.h"
#include <iostream>

ShoppingDB::ShoppingDB() {

}

void ShoppingDB::add_product(std::string name, int price){
    // TODO: For problem 1-1
    Product* newProduct = new Product(name, price);
    products.push_back(newProduct);

}

bool ShoppingDB::productExists(std::string name){
    for (Product* product : products) {
        if (product->name == name) return true;
    }
    return false;
}

bool ShoppingDB::edit_product(std::string name, int price){
    // TODO: For problem 1-1

    if (price > 0) {
        for (Product *product: products) {
            if (product->name == name) {
                product->price = price;
                return true;
            }
        }
    }
    else return false;
}

std::vector<Product*> ShoppingDB::getProducts(){
    return products;
}

Product* ShoppingDB::findProduct(std::string name){
    for (Product* product : products) {
        if (product->name == name) return product;
    }
    return nullptr;
}


User* ShoppingDB::authenticateUser(std::string username, std::string password){
    for (User* user : users) {
        if (user->name == username && user->getPassword() == password) return user;
    }
    return nullptr;
}

User* ShoppingDB::findUser(std::string username){
    for (User* user : users) {
        if (user->name == username) return user;
    }
    return nullptr;
}

void ShoppingDB::add_user(std::string username, std::string password, bool premium){
    User* newUser = new User(username, password, premium);
    users.push_back(newUser);
}

std::vector<User*> ShoppingDB::getUsers(){
    return users;
}




