//
// Created by sbunn on 12/16/2021.
//

#ifndef PROBLEM2_VIEW_H
#define PROBLEM2_VIEW_H

#include <string>
#include <iostream>
#include "pair.h"

class View{
public:
    View(std::istream& is, std::ostream& os);
    std::istream& is;
    std::ostream& os;
};

class PostView {
public:
    std::string getUserInput(std::istream& is, std::ostream& os, std::string prompt);
    Pair<std::string, std::string> getPost(std::istream& is, std::ostream& os, std::string prompt);
};

class AuthView {
public:
    std::string getUserInput(std::istream& is, std::ostream& os, std::string prompt);

};

#endif //PROBLEM2_VIEW_H
