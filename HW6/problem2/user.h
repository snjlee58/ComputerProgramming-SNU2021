//
// Created by sbunn on 12/16/2021.
//

#ifndef PROBLEM2_USER_H
#define PROBLEM2_USER_H

#include <string>

class User{
public:
    std::string id;
    User(std::string id, std::string password);
private:
    std::string password;
};

#endif //PROBLEM2_USER_H
