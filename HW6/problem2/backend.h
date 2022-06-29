//
// Created by sbunn on 12/16/2021.
//

#ifndef PROBLEM2_BACKEND_H
#define PROBLEM2_BACKEND_H

#include <string>
#include "ServerResourceAccessible.h"

class BackEnd : public ServerResourceAccessible{
    // Use getServerStorageDir() as a default directory
    // Create helper functions to support FrontEnd class
public:
    // problem 2-1
    bool userExists(std::string userId);
    std::string getStoredPassword(std::string userId);
    // problem 2-2
    void storePost(std::string userId, std::string title, std::string content);
    // problem 2-3

    // problem 2-4


};

#endif //PROBLEM2_BACKEND_H
