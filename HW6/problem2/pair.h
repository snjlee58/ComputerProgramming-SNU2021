//
// Created by sbunn on 12/17/2021.
//

#ifndef PROBLEM2_PAIR_H
#define PROBLEM2_PAIR_H

template<class K, class V>
class Pair{
public:
    K key;
    V value;
    Pair(K key, V value) : key(key), value(value){};
};

#endif //PROBLEM2_PAIR_H
