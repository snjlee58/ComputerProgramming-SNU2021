//
// Created by sbunn on 12/16/2021.
//

#ifndef PROBLEM2_FRONTEND_H
#define PROBLEM2_FRONTEND_H

#include <string>
#include "user.h"
#include "backend.h"
#include "pair.h"

class FrontEnd{
public:

    FrontEnd(BackEnd backend);
    bool auth(std::string authInfo);
    void post(Pair<std::string, std::string> titleContentPair);
    void recommend(int N);
    void search (std::string command);
    User* getUser();
private:
//    UserInterface ui;
    BackEnd backend;
    User* user;
};

bool equalsCaseInsensitive(std::string str1, std::string str2);

#endif //PROBLEM2_FRONTEND_H
