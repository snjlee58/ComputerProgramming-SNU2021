//
// Created by sbunn on 12/16/2021.
//

#include "frontend.h"
#include <string>
#include <iostream>
#include "user.h"
#include "pair.h"

#include <filesystem>
namespace fs = std::filesystem;

FrontEnd::FrontEnd(BackEnd backend) : backend(backend){
}

bool equalsCaseInsensitive(std::string str1, std::string str2){
    // Compare length of the two strings
    if (str1.size() != str2.size()) return false;

    // Compare character by character
    int idx = 0;
    while (idx < str1.size()){
        if (tolower(str1[idx]) != tolower(str2[idx])) return false;
        idx++;
    }
    return true;
}

bool FrontEnd::auth(std::string authInfo){
    std::string idInput = authInfo.substr(0, authInfo.find("\n"));
    std::string pwInput = authInfo.substr(authInfo.find("\n") + 1, std::string::npos);
    user = new User(idInput, pwInput);

    // Check if user with idInput exists
    if (!backend.userExists(idInput)) {
        return false;
    }

    // Authenticate user with pwInput
    if (equalsCaseInsensitive(pwInput, backend.getStoredPassword(idInput))){
        return true;
    }
    else return false;


}

User* FrontEnd::getUser(){
    return user;
}

void FrontEnd::post(Pair<std::string, std::string> titleContentPair){
    std::string title = titleContentPair.key;
    std::string content = titleContentPair.value;

    backend.storePost(user->id, title, content);
}

