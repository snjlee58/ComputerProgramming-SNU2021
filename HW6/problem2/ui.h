//
// Created by sbunn on 12/16/2021.
//

#ifndef PROBLEM2_UI_H
#define PROBLEM2_UI_H

#include <string>
#include "frontend.h"
#include "view.h"

class UserInterface{
public:
    UserInterface(std::istream& is, std::ostream& os, FrontEnd frontend);
    PostView postView;
    AuthView authView;
//    View mainView;
    std::istream& is;
    std::ostream& os;
    void run();
private:
    FrontEnd frontend;
    bool query(std::string command);
    void createUI(FrontEnd frontend);
    void createUITest(FrontEnd frontend, std::string authInput, std::string postInput);
    void post();
    void search(std::string command);
    void recommend(int N);


};

#endif //PROBLEM2_UI_H
