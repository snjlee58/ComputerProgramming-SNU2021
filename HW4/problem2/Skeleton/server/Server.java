package server;

import course.*;
import utils.Config;
import utils.ErrorCode;
import utils.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Server {
    private static final String coursesDirPath = "data/Courses/2020_Spring/";
    private static final String usersDirPath = "data/Users/";
    private static final String regCoursesFilename = "registered.txt";

    public List<Course> search(Map<String,Object> searchConditions, String sortCriteria){
        // TODO Problem 2-1

        return null;
    }

    public int bid(int courseId, int mileage, String userId) {
        // TODO Problem 2-2

        return 0;
    }

    public Pair<Integer,List<Bidding>> retrieveBids(String userId){
        // TODO Problem 2-2

        return null;
    }

    public boolean confirmBids(){
        // TODO Problem 2-3

        return true;
    }

    public Pair<Integer,List<Course>> retrieveRegisteredCourse(String userId){
        // TODO Problem 2-3

        return null;
    }
}
