#include "admin_ui.h"

AdminUI::AdminUI(ShoppingDB &db, std::ostream& os): UI(db, os) { }

void AdminUI::add_product(std::string name, int price) {
    // TODO: For problem 1-1
    if (price > 0){
        db.add_product(name, price);
        get_os() << "ADMIN_UI: " << name << " is added to the database." << std::endl;
    }
    else{
        get_os() << "ADMIN_UI: Invalid price." << std::endl;
    }

}

void AdminUI::edit_product(std::string name, int price) {
    // TODO: For problem 1-1

    // if product name exists in list, attempt to modify price
    if (db.productExists(name)){
        bool productEdited = db.edit_product(name, price);

        if (productEdited){
            get_os() << "ADMIN_UI: " << name << " is modified from the database." << std::endl;
        }
        else{
            get_os() << "ADMIN_UI: Invalid price." << std::endl;
        }
    }

    // if product name exists in list, attempt to modify price
    else{
        get_os() << "ADMIN_UI: Invalid product name." << std::endl;
    }


}

void AdminUI::list_products() {
    // TODO: For problem 1-1
    std::vector<Product*> products = db.getProducts();

    get_os() << "ADMIN_UI: Products: [";

    for (Product* product : products){
        if (product != products.front()){
            get_os() << ", ";
        }
        std::string name = product->name;
        int price = product->price;
        get_os() << "(" << name << ", " << price << ")";
    }

    get_os() << "]" << std::endl;
}
