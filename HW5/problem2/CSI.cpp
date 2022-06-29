#include "CSI.h"
#include <sstream>
//added by me
#include <fstream>
#include <string>
#include <cmath>
#include <algorithm>
#include <vector>

Complex::Complex(): real(0), imag(0) {}

CSI::CSI(): data(nullptr), num_packets(0), num_channel(0), num_subcarrier(0) {}

CSI::~CSI() {
    if(data) {
        for(int i = 0 ; i < num_packets; i++) {
            delete[] data[i];
        }
        delete[] data;
    }
}

int CSI::packet_length() const {
    return num_channel * num_subcarrier;
}

void CSI::print(std::ostream& os) const {
    for (int i = 0; i < num_packets; i++) {
        for (int j = 0; j < packet_length(); j++) {
            os << data[i][j] << ' ';
        }
        os << std::endl;
    }
}

template<typename T>
T** allocateArray(int row, int column){
    T** doubleArray = new T* [row];

    for(int i=0; i<row; i++) {
        doubleArray[i] = new T[column];
    }
    return doubleArray;
}

std::ostream& operator<<(std::ostream &os, const Complex &c) {
    // TODO: problem 2.1
    int real = c.real;
    int imag = c.imag;

    if (imag >= 0){
        return os << real << "+" << imag << "i";
    } else{
        return os << real << "" << imag << "i";
    }
}

void read_csi(const char* filename, CSI* csi) {
    // TODO: problem 2.2
    std::ifstream dataFile(filename);
    std::string num_packets, num_channel;
    std::string num_subcarrier;
    getline(dataFile, num_packets);
    getline(dataFile, num_channel);
    getline(dataFile, num_subcarrier);

    // set csi member variables
    csi->num_packets = std::stoi(num_packets);
    csi->num_channel = std::stoi(num_channel);
    csi->num_subcarrier = std::stoi(num_subcarrier);
    csi->data = allocateArray<Complex>(csi->num_packets, csi->packet_length());


    // store data in csi array from input file
    std::string real, imag;
    for (int p = 0; p < csi->num_packets; p++){
        for (int sc = 0; sc < csi->num_subcarrier; sc++){
            for (int c = 0; c < csi->num_channel; c++){
                getline(dataFile, real);
                getline(dataFile, imag);

                // create new Complex object to add to array
                Complex newComplex;
                newComplex.real = std::stoi(real);
                newComplex.imag = std::stoi(imag);

                csi->data[p][(c*csi->num_subcarrier)+sc] = newComplex;
            }
        }
    }
    return;
}



double** decode_csi(CSI* csi) {
    // TODO: problem 2.3
    int rowNum = csi->num_packets;
    int columnNum = csi->packet_length();
    double** ampArray = allocateArray<double>(rowNum, columnNum);

    // calculate amplitude with each element in data array
    for (int row = 0; row < rowNum; row++){
        for (int column = 0; column < columnNum; column++){
            Complex currElement = csi->data[row][column];
            int a = currElement.real, b = currElement.imag;
            double aSquared = a*a, bSquared = b*b;
            ampArray[row][column] = sqrt(aSquared + bSquared);
        }
    }
    return ampArray;
}

template<typename T>
std::vector<T> ptrToVector(T* arrayPtr, int length){
    std::vector<T> newVector(length);
    for (int i = 0; i < length; i++){
        newVector[i] = *(arrayPtr + i);
    }
    return newVector;
}

double getMedian(double* packet, int length){
    // sort packet in ascending order of amplitudes
    std::vector<double> amplitudes(length);
    amplitudes = ptrToVector(packet, length);

    std::sort(amplitudes.begin(), amplitudes.end());
    double median = -1.0;

    // if there's an odd number of ratings, return the median rating
    if (amplitudes.size() % 2 == 1) {
        int medianIndex = amplitudes.size() / 2;
        median = amplitudes[medianIndex];
    }
        // if there's an even number of ratings, use the average value of the two middle values as the median rating
    else{
        int upperIndex = amplitudes.size() / 2;
        int lowerIndex = amplitudes.size() / 2 - 1;
        median = (amplitudes[lowerIndex] + amplitudes[upperIndex]) / 2;
    }
    return median;
}

double* get_med(double** decoded_csi, int num_packets, int packet_length) {
    // TODO: problem 2.4
    double* medians = new double[num_packets];
    for (int i = 0; i < num_packets; i++){
        medians[i] = getMedian(decoded_csi[i], packet_length);
    }
    return medians;
}

bool compareLarger(double a, double b){
    if ((a + 1e-10) < b) return false;
    else return true;
}

double breathing_interval(double** decoded_csi, int num_packets) {
    // TODO: problem 2.5
    // collect all breathing amplitudes
    std::vector<double> breathingAmps;
    for (int i = 0; i < num_packets; i++){
        breathingAmps.push_back(decoded_csi[i][0]);
    }

    // collect all peak values
    std::vector<double> peakValues;

    for (int i=0; i<num_packets; i++){
        bool largerThanLeft, largerThanRight;

        // size comparison with LEFT side
        if (i == 0){
            largerThanLeft = true;
        } else if (i == 1){
            largerThanLeft = compareLarger(breathingAmps[i], breathingAmps[i-1]);
        } else{
            largerThanLeft = (compareLarger(breathingAmps[i], breathingAmps[i-1]) && compareLarger(breathingAmps[i],breathingAmps[i-2]));
        }

        // size comparison with RIGHT side
        if (i == num_packets-1){
            largerThanRight = true;
        } else if(i == num_packets-2){
            largerThanRight = compareLarger(breathingAmps[i], breathingAmps[i+1]);
        } else{
            largerThanRight = (compareLarger(breathingAmps[i], breathingAmps[i+1]) && compareLarger(breathingAmps[i],breathingAmps[i+2]));
        }

        // add index to peakValues if larger than both left side and right side
        if (largerThanLeft && largerThanRight){
            peakValues.push_back(i);
        }
    }

    // if there's 1 or 0 peakValues, return num_packets
    if (peakValues.size() == 0 || peakValues.size() == 1) return num_packets;

    // find mean interval
    double sumOfIntervals = 0.0;
    for (int i = 0; i < peakValues.size()-1; i++){
        int interval = peakValues[i+1] - peakValues[i];
        sumOfIntervals += interval;
    }
    return sumOfIntervals / (peakValues.size()-1);
}
