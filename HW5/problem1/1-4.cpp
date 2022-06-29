
int** initializeOnes(int** pascalArray, int rows){
    for (int r= 0; r < rows; r++){
        pascalArray[r][0] = 1;
        pascalArray[r][r] = 1;
    }
    return pascalArray;
}

int* pascal_triangle(int N) {
    // TODO: problem 1.4
    // initialize pascal double array
    int** pascal = new int* [N];
    for(int i=0; i<N; i++) {
        pascal[i] = new int[i+1];
    }

    pascal = initializeOnes(pascal, N);

    if (N > 2){
        for (int row =2; row < N; row++){
            for (int term = 1; term < row; term++){
                pascal[row][term] = pascal[row-1][term] + pascal[row-1][term-1];
            }
        }
    }

    int* rowN = pascal[N-1];

    return rowN;
}

