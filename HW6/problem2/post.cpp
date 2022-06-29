//
// Created by sbunn on 12/16/2021.
//

#include "post.h"
#include <string>
#include <cstdlib>
#include <ctime>

Post::Post(std::string title, std::string content) : title(title), content(content){
    time_t curr_time;
    curr_time = time(NULL);
    localTime = localtime(&curr_time);
}

void Post::setId(int id){
    this->id = id;
}

int Post::getId(){
    return id;
}

std::string Post::getDate(){
    int year = 1900 + localTime->tm_year;
    int month = 1 + localTime->tm_mon;
    int day = localTime->tm_mday;
    int hour = localTime->tm_hour, min = localTime->tm_min, sec = localTime->tm_sec;

    char buf[20];
    sprintf(buf, "%d/%02d/%02d %02d:%02d:%02d", year, month, day, hour, min, sec);
    return std::string(buf);
}

std::string Post::getTitle(){ return title;}
std::string Post::getContent(){ return content;}

