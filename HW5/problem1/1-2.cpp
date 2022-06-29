
int hamming_distance(int x, int y) {
    // TODO: problem 1.2

    int XxorY = x ^ y;
    int hammingDist = 0;

    // convert XxorY to binary
    int binary[32] = {};
    int i =0;
    while (XxorY > 0){
        binary[i] = XxorY % 2;
        XxorY /= 2;
        i++;
    }

    // count the number of 1s in the binary conversion (1 indicates different bit in that position)
    for (int i =0; i<32; i++){
        if (binary[i] == 1) hammingDist++;
    }

    return hammingDist;
}


