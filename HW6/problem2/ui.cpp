//
// Created by sbunn on 12/16/2021.
//

#include <iostream>
#include <string>
#include "ui.h"
#include "view.h"
#include <vector>

UserInterface::UserInterface(std::istream& is, std::ostream& os, FrontEnd frontend) : frontend(frontend), is(is), os(os){
}


void UserInterface::run(){
    std::string command;
    std::string authInfo = authView.getUserInput(is, os, "------ Authentication ------\n");
    if (frontend.auth(authInfo)){
        do {
            command = postView.getUserInput(is, os, "-----------------------------------\n" +
                                                    frontend.getUser()->id + "@sns.com\n" +
                                                    "post : Post contents\n" +
                                                    "recommend <number> : recommend <number> interesting posts\n" +
                                                    "search <keyword> : List post entries whose contents contain <keyword>\n" +
                                                    "exit : Terminate this program\n" +
                                                    "-----------------------------------\n" +
                                                    "Command=");
        } while (query(command));

    }
    else{
        os << "Failed Authentication." << std::endl;
    }

}

bool UserInterface::query(std::string command){
    // Split command string
    std::vector<std::string> commandSlices;
    int idx = 0;
    while ((idx = command.find(" ")) != std::string::npos) {
        commandSlices.push_back(command.substr(0, idx));
        command.erase(0, idx + 1);
    }
    commandSlices.push_back(command);

    // Execute instruction
    std::string instruction = commandSlices.at(0);
    if (instruction == "exit") return false;
    else if (instruction == "post"){
        post();
    }
//    else if (instruction == "search"){
//        search(command);
//    } else if (instruction == "recommend"){
//        recommend(std::stoi(commandSlices[1]));
//    }
    else{
        os << "Illegal Command Format : " << command << std::endl;
    }
    return true;

}

void UserInterface::post(){
    frontend.post(postView.getPost(is, os, "New Post"));
}

