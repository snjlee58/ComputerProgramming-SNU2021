//
// Created by sbunn on 12/16/2021.
//

#include "view.h"
#include "pair.h"
#include <string>

// PostView
std::string PostView::getUserInput(std::istream& is, std::ostream& os, std::string prompt){
    os << prompt;

    std::string command;
    getline(is, command);
    return command;
}

Pair<std::string, std::string> PostView::getPost(std::istream& is, std::ostream& os, std::string prompt){
    std::string title;
    std::string content;
    std::string entireContent = "";

    os << "-----------------------------------" << std::endl;
    os << prompt << std::endl;
    os << "* Title=";
    getline(is, title);
    os << "* Content\n>";
    getline(is, content);
    entireContent += content;
    while (!content.empty()){
        os << ">";
        getline(is, content);
        if (!content.empty())
            entireContent += "\n" + content;
    }
    Pair<std::string, std::string> titleContentPair(title, entireContent);
    return titleContentPair;
}


// AuthView
std::string AuthView::getUserInput(std::istream& is, std::ostream& os, std::string prompt){
    std::string idInput, pwInput;

    os << prompt;
    os << "id=";
    getline(is, idInput);

    os << "passwd=";
    getline(is, pwInput);

    std::string authInfo = idInput + "\n" + pwInput;

    return authInfo;
}
