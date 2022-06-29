#include <string>

bool is_palindrome(std::string s) {
    // TODO: problem 1.1
    int slen = s.length();

    // create reversed string s
    std::string reversed_s = "";

    for (int i =slen-1; i>=0; i--){
        reversed_s += s[i];
    }


    if (s == reversed_s) return true;
    else return false;





    return true;
}