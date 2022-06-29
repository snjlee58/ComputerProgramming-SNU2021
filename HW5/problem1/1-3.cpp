#include <vector>
#include <algorithm>
#include <iostream>

void merge_arrays(int* arr1, int len1, int* arr2, int len2) {
    // TODO: problem 1.3
    std::vector<double> mergedArr;
    for (int i=0; i<len1; i++){
        mergedArr.push_back(arr1[i]);
    }
    for (int i=0; i<len2; i++){
        mergedArr.push_back(arr2[i]);
    }
    std::sort(mergedArr.begin(), mergedArr.end());


    for (int i = 0; i<(len1+len2); i++){
        arr1[i] = mergedArr[i];
    }
}

