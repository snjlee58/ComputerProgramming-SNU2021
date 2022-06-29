//
// Created by sbunn on 12/16/2021.
//

#ifndef PROBLEM2_POST_H
#define PROBLEM2_POST_H

#include <string>
#include <ctime>

#define ID_NOT_INITIATED = -1

class Post{
public:
    Post(std::string title, std::string content);
    void setId(int id);
    int getId();
    std::string getDate();
    std::string getTitle();
    std::string getContent();
private:
    int id;
    std::string title, content;
    tm *localTime;

};

#endif //PROBLEM2_POST_H
