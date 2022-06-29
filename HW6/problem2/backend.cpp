//
// Created by sbunn on 12/16/2021.
//

#include "backend.h"
#include <iostream>
#include <filesystem>
#include <string>
#include <fstream>
#include "post.h"
#include <vector>

namespace fs = std::filesystem;

bool BackEnd::userExists(std::string userId){
    for(auto const& file: std::filesystem::directory_iterator{getServerStorageDir()}){
        std::string fileName = file.path().filename().string();
        if (fileName == userId) return true;
    }
    return false;
}

std::string BackEnd::getStoredPassword(std::string userId){
    std::string pwFilePath = getServerStorageDir() + userId + "/password.txt";
    std::ifstream pwFile(pwFilePath);

    std::string str;
    std::getline(pwFile, str);
    return str;
}

void BackEnd::storePost(std::string userId, std::string title, std::string content){
    // create list of all posts from all users
    std::vector<int> postIDs;

    // Iterate through each USER directory
    for(auto const& userDir: std::filesystem::directory_iterator{getServerStorageDir()}){
        // Iterate through each user's POST directory
        for(auto const& postFile: std::filesystem::directory_iterator{ userDir.path().string() + "/post"}){
            std::string fileExtension = ".txt";
            std::string fileName = postFile.path().filename().string();
            std::string postID = fileName.erase(fileName.find(fileExtension), fileExtension.length());
            postIDs.push_back(stoi(postID));
        }
    }
    int largestPostID = *std::max_element(postIDs.begin(), postIDs.end());
    Post newPost(title, content);
    newPost.setId(largestPostID+1);

    // Write to new txt file
    std::ofstream newFile(getServerStorageDir() + userId + "/post/" + std::to_string(newPost.getId()) + ".txt");
    newFile << newPost.getDate() << "\n" << newPost.getTitle() << "\n\n" << newPost.getContent();
    newFile.close();
}
