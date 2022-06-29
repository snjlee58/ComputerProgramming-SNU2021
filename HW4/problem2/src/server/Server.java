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

        // load courses from directory
        List<Course> courses = loadCourses();

        // when searchConditions is null or empty
        if (searchConditions == null || searchConditions.isEmpty()){
            sortCourses(courses, sortCriteria);
            return courses;
        }

        // search by dept
        if (searchConditions.containsKey("dept")){
            String desiredDept = ((String)searchConditions.get("dept"));
            courses = deptSearch(desiredDept, courses);
        }

        // search by ay
        if (searchConditions.containsKey("ay")){
            int desiredAy = ((Integer)searchConditions.get("ay"));
            courses = aySearch(desiredAy, courses);
        }

        // search by name
        if (searchConditions.containsKey("name")){
            String searchString = ((String)searchConditions.get("name"));
            courses = keywordSearch(searchString, courses);
        }


        // sort matched courses
        sortCourses(courses, sortCriteria);

        return courses;
    }

    //for debugging purpose
    private void printCoursesList(List<Course> courses){
        for (Course course : courses){
            System.out.println(course.courseName + "| id(" + course.courseId + ") ay(" + course.academicYear + ")  dept(" + course.department + ")");
        }
        System.out.println();
    }

    private List<Course> loadCourses(){
        LinkedList<Course> courses = new LinkedList<>();

        File coursesDir = new File(coursesDirPath);
        File[] colleges = coursesDir.listFiles();
        for (File college : colleges){
            File collegeDir = new File(coursesDirPath + college.getName() + "/");
            File[] courseFiles = collegeDir.listFiles();
            for (File courseFile : courseFiles)
                try{
                    Scanner courseFileRead = new Scanner(courseFile);
                    String courseInformation_ = courseFileRead.nextLine();
                    String[] courseInfo = courseInformation_.split("[|]");

                    String courseId_ = courseFile.getName().substring(0, courseFile.getName().indexOf(".txt"));
                    int courseId = Integer.parseInt(courseId_);
                    Course newCourse = new Course(courseId, college.getName(), courseInfo[0], courseInfo[1], Integer.parseInt(courseInfo[2]),
                            courseInfo[3], Integer.parseInt(courseInfo[4]), courseInfo[5], courseInfo[6], Integer.parseInt(courseInfo[7]));

                    courses.add(newCourse);
                    courseFileRead.close();
                }catch(Exception e){

                }
        }
        return courses;
    }

    private List<Course> deptSearch(String desiredDept, List<Course> courses){
        LinkedList<Course> matchedCourses = new LinkedList<>();
        ListIterator<Course> coursesIterator = courses.listIterator();
        while (coursesIterator.hasNext()){
            Course currCourse = coursesIterator.next();
            if (currCourse.department.equals(desiredDept))
                matchedCourses.add(currCourse);
        }
        return matchedCourses;
    }

    private List<Course> aySearch(int desiredAy, List<Course> courses){
        LinkedList<Course> matchedCourses = new LinkedList<>();
        ListIterator<Course> coursesIterator = courses.listIterator();
        while (coursesIterator.hasNext()){
            Course currCourse = coursesIterator.next();

            if (currCourse.academicYear <= desiredAy){
                matchedCourses.add(currCourse);
            }
        }
        return matchedCourses;
    }

    private List<Course> keywordSearch(String searchString, List<Course> courses){
        LinkedList<Course> matchedCourses = new LinkedList<>();

        // return empty list when searchString is empty
        if (searchString.equals(""))
            return new ArrayList<>();
        else {
            String[] keywords = (searchString.split("\\s+")); //split searched Keywords

            // iterate through each course
            ListIterator<Course> coursesIterator = courses.listIterator();
            while (coursesIterator.hasNext()){
                Course currCourse = coursesIterator.next();

                // separate words in courseName by space
                String[] courseNameSplit = currCourse.courseName.split("\\s+");
                Set<String> courseNameKeywords = new HashSet<>();
                for (String keyword : courseNameSplit)
                    courseNameKeywords.add(keyword);

                // iterate through searchString keywords to evaluate match - if at least one keyword doesn't exist in courseName, no match
                Boolean match = true;
                for (String keyword : keywords){
                    if (!courseNameKeywords.contains(keyword)) {
                        match = false;
                        break;
                    }
                }
                if (!match) continue;
                else matchedCourses.add(currCourse);

            }
            return matchedCourses;
        }
    }

    private void sortCourses(List<Course> courses, String sortCriteria){
        if (sortCriteria == null || sortCriteria.equals("") || sortCriteria.equals("id"))
            courses.sort(new courseIdComparator());
        else if (sortCriteria.equals("name"))
            courses.sort(new courseNameComparator());
        else if (sortCriteria.equals("dept"))
            courses.sort(new courseDeptComparator());
        else
            courses.sort(new courseAyComparator());
    }

    class courseIdComparator implements Comparator<Course> {
        @Override
        public int compare(Course o1, Course o2) {
            return Integer.compare(o1.courseId, o2.courseId);
        }
    }

    class courseNameComparator implements Comparator<Course> {
        @Override
        public int compare(Course o1, Course o2) {
            if (o1.courseName.compareTo(o2.courseName) == 0){
                return Integer.compare(o1.courseId, o2.courseId);
            }
            return o1.courseName.compareTo(o2.courseName);
        }
    }

    class courseDeptComparator implements Comparator<Course> {
        @Override
        public int compare(Course o1, Course o2) {
            if (o1.department.compareTo(o2.department) == 0){
                return Integer.compare(o1.courseId, o2.courseId);
            }
            return o1.department.compareTo(o2.department);
        }
    }

    class courseAyComparator implements Comparator<Course> {
        @Override
        public int compare(Course o1, Course o2) {
            if (o1.academicYear == o2.academicYear){
                return Integer.compare(o1.courseId, o2.courseId);
            }
            return Integer.compare(o1.academicYear, o2.academicYear);
        }
    }

    public int bid(int courseId, int mileage, String userId) {
        // TODO Problem 2-2

        // Check USERID_NOT_FOUND = -61
        // if user exists, store User
        if (userId == null || !getUsers().keySet().contains(userId)) return ErrorCode.USERID_NOT_FOUND;
        User currUser = getUsers().get(userId);

        // Check NO_COURSE_ID = -51
        if (!courseIdExists(courseId)) return ErrorCode.NO_COURSE_ID;

        // Check NEGATIVE_MILEAGE = -43
        if (mileage < 0) return ErrorCode.NEGATIVE_MILEAGE;

        // Check if 'bid.txt' exists for user - IO_ERROR
        String userBidFilePath = usersDirPath + userId + "/" + "bid.txt";
        File bidFile = new File(userBidFilePath);
        if (!bidFile.exists()) return ErrorCode.IO_ERROR;

        // Retrieve user's preexisting bids if it does.
        Pair<Integer,List<Bidding>> retrievedBidInfo = retrieveBids(userId);
        List<Bidding> userBidList = retrievedBidInfo.value;


        // Check OVER_MAX_COURSE_MILEAGE = -42
        if (mileage > Config.MAX_MILEAGE_PER_COURSE) return ErrorCode.OVER_MAX_COURSE_MILEAGE;


        // create new Bidding and add it to memory (not in bid.txt yet)
        Bidding newBid = new Bidding(courseId, mileage);
        Map<Integer, Bidding> bidToHashMap = convertToHashMap(userBidList);
        // if user bids for a previous bid course
        if (bidToHashMap.containsKey(newBid.courseId)){
            // if user bids 0 mileage for previous bid course, cancel/remove the previous bid
            userBidList.remove(bidToHashMap.get(newBid.courseId));
            // if user bids >0 mileage for previous bid course, replace previous bid
            if (newBid.mileage > 0) userBidList.add(newBid);

        }
        // if user bids for a new bid course
        else {
            // if user bids 0 for new bid course, ignore the bid
            // if user bids >0 for new bid course, add to bidding list
            if (newBid.mileage > 0) userBidList.add(newBid);

        }
        // Check OVER_MAX_MILEAGE  = -41
        if (currUser.calcTotalMileage(userBidList) > Config.MAX_MILEAGE) return ErrorCode.OVER_MAX_MILEAGE;

        // Check OVER_MAX_COURSE_NUMBER = -40 (over limit on # of bid courses)
        if (userBidList.size() > Config.MAX_COURSE_NUMBER) return ErrorCode.OVER_MAX_COURSE_NUMBER;


        // write to bid.txt
        try {
            FileWriter fileWriter = new FileWriter(userBidFilePath);
            ListIterator<Bidding> biddingIterator = userBidList.listIterator();
            String bidFileText = "";

            while(biddingIterator.hasNext()) {
                Bidding currBidding = biddingIterator.next();
                bidFileText += currBidding.courseId + "|" + currBidding.mileage + "\n";
            }
            fileWriter.write(bidFileText);
            fileWriter.close();
        } catch(IOException e){
            // IO_ERROR = -10
            return ErrorCode.IO_ERROR;
        }

        // if all goes well, return SUCCESS = 0
        return ErrorCode.SUCCESS;
    }

    private boolean courseIdExists(int courseId){
        List<Course> courses = loadCourses();
        ListIterator<Course> coursesIterator = courses.listIterator();
        while(coursesIterator.hasNext()){
            Course currCourse = coursesIterator.next();
            if (currCourse.courseId == courseId) return true;
        }
        return false;
    }

    private Map<Integer, Bidding> convertToHashMap(List<Bidding> biddingList){
        Map<Integer, Bidding> biddingMap = new HashMap<>();
        ListIterator<Bidding> biddingIterator = biddingList.listIterator();
        while (biddingIterator.hasNext()){
            Bidding currBidding = biddingIterator.next();
            biddingMap.put(currBidding.courseId, currBidding);
        }
        return biddingMap;
    }

    private Map<String, User> getUsers(){
        Map<String, User> allUsers = new HashMap<>();

        File UsersDir = new File(usersDirPath);
        File[] usersIds = UsersDir.listFiles();
        List<String> userIds = new LinkedList<>();
        for (File userId : usersIds){
            User newUser = new User(userId.getName());
            allUsers.put(userId.getName(), newUser);
        }
        return allUsers;
    }

    public Pair<Integer,List<Bidding>> retrieveBids(String userId){
        // TODO Problem 2-2
        // Check USERID_NOT_FOUND = -61
        if (userId == null || !getUsers().keySet().contains(userId)) return new Pair<>(ErrorCode.USERID_NOT_FOUND, new ArrayList<>());

        // retrieve bid information
        try{
            File userBidFile = new File(usersDirPath + userId + "/" + "bid.txt");
            List<Bidding> userBidList = new LinkedList<>();

            Scanner bidFileRead = new Scanner(userBidFile);
            while (bidFileRead.hasNext()) {
                String bidInformation_ = bidFileRead.nextLine();
                String[] courseInfo = bidInformation_.split("[|]");

                int courseId = Integer.parseInt(courseInfo[0]);
                int mileage = Integer.parseInt(courseInfo[1]);
                Bidding currBid = new Bidding(courseId, mileage);
                userBidList.add(currBid);
            }
            bidFileRead.close();

            // SUCCESS = 0 (return bid list)
            return new Pair<>(ErrorCode.SUCCESS, userBidList);
        }catch(IOException e){
            // IO_ERROR = -10
            return new Pair<>(ErrorCode.IO_ERROR, new ArrayList<>());
        }
    }

    public boolean confirmBids(){
        Comparator<User> sortById = new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.userId.compareTo(o2.userId);
            }
        };

        Comparator<User> sortByTotalMileage = new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return Integer.compare(o1.calcTotalMileage(), o2.calcTotalMileage());
            }
        };

        Comparator<User> sortByBidding = new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return Integer.compare(o2.storeCurrBidding, o1.storeCurrBidding);
            }
        };


        // TODO Problem 2-3
        Map<Integer, Course> searchCourseById = coursesMapById(); // load all courses as a HashMap (int courseId : Course obj)
        Map<String, User>  searchUserById = getUsers(); // load all users as a HashMap (String id : User obj)

        // load Map of biddings for each user
        for (String userId : searchUserById.keySet()){
            Pair<Integer,List<Bidding>> retrievedBidInfo = retrieveBids(userId);

            // if bid.txt doesn't exist (IO_ERROR = -10), return false
            if (retrievedBidInfo.key == ErrorCode.IO_ERROR) return false;

            // if bid.txt exists for the user, retrieve the user's biddings and save in memory
            List<Bidding> userBidList = retrievedBidInfo.value;
            searchUserById.get(userId).setBiddings(userBidList);
        }

        // initialize map (courseId : list of bidders)
        Map<Integer, List<User>> biddersForEachCourse = new HashMap<>();

        // iterate through each course to compile bidders for
        for (Integer courseId : searchCourseById.keySet()){
            biddersForEachCourse.put(courseId, new LinkedList<>());

            // iterate through each user to gather bidders
            for (String userId : searchUserById.keySet()){
                if (searchUserById.get(userId).getBiddingsMap().containsKey(courseId)){
                    biddersForEachCourse.get(courseId).add(searchUserById.get(userId));
                }
            }
        }

        // initialize map (user: list for confirmed registrations)
        Map<User, List<Integer>> usersRegistrations = new HashMap<>();

        Iterator<User> userIterator = searchUserById.values().iterator();
        while (userIterator.hasNext()){
            User currUser = userIterator.next();
            usersRegistrations.put(currUser, new LinkedList<>());
        }

        // iterate through each course to confirm bidders for
        for (Integer courseId : biddersForEachCourse.keySet()){
            // store mileage bidding for current course in each user's attribute storeCurrBidding
            List<User> biddersToSort = biddersForEachCourse.get(courseId);
            for (User user : biddersToSort){
                user.storeCurrBidding = user.getBiddingsMap().get(courseId).mileage;
            }

            // sort bidders
            biddersToSort.sort(sortById); // sort by ascending userId (lower gets priority)
            biddersToSort.sort(sortByTotalMileage); // sort by ascending total mileage (lower gets priority)
            biddersToSort.sort(sortByBidding); // sort by descending bidding (higher gets priority)

            // cut list if the number of bidders exceeds the quota
            List<User> confirmedBidders = biddersToSort;
            if (biddersToSort.size() > searchCourseById.get(courseId).quota){
                confirmedBidders = biddersToSort.subList(0, searchCourseById.get(courseId).quota);
            }

            // confirm registration for confirmed bidders (store in memory first)
            ListIterator<User> confirmedBiddersIter = confirmedBidders.listIterator();
            while (confirmedBiddersIter.hasNext()){
                User confirmedBidder = confirmedBiddersIter.next();
                usersRegistrations.get(confirmedBidder).add(courseId);
            }
        }

        // save confirmed registrations to the corresponding user's directory in "registered.txt"
        for (User userToConfirm : usersRegistrations.keySet()){
            try{
                FileWriter fileWriter = new FileWriter(usersDirPath + userToConfirm.userId + "/" + regCoursesFilename);

                // iterate through registered courseId for the user, and record each one
                ListIterator<Integer> registeredCourseIdsIter = usersRegistrations.get(userToConfirm).listIterator();
                while (registeredCourseIdsIter.hasNext()){
                    int currCourseId = registeredCourseIdsIter.next();
                    fileWriter.write(currCourseId + "|");
                }
               fileWriter.close();
            }catch (IOException e){
                return false;
            }
        }

        // clear bid.txt for all users
        for (String userId : searchUserById.keySet()){
            try{
                FileWriter fileWriter = new FileWriter(usersDirPath + userId + "/bid.txt");
                fileWriter.close();
            }catch (IOException e){
                return false;
            }
        }
        return true;
    }

    // for debugging purposes
    private void printBiddersForEachCourse(Map<Integer, List<User>> biddersForEachCourse){
        for (int courseId : biddersForEachCourse.keySet()){
            System.out.print("Bidders for courseId " + courseId + ": ");
            for (User bidder : biddersForEachCourse.get(courseId)) {
                System.out.print(bidder.userId + " ");
            }
            System.out.println();
        }
    }

    private Map<Integer, Course> coursesMapById(){
        List<Course> loadedCourses = loadCourses();

        Map<Integer, Course> courseMap = new HashMap<>();
        ListIterator<Course> coursesIterator = loadedCourses.listIterator();
        while (coursesIterator.hasNext()){
            Course currCourse = coursesIterator.next();
            courseMap.put(currCourse.courseId, currCourse);
        }
        return courseMap;
    }

    public Pair<Integer,List<Course>> retrieveRegisteredCourse(String userId){
        // TODO Problem 2-3
        // Check USERID_NOT_FOUND error
        if (userId == null || !getUsers().keySet().contains(userId)) return new Pair<>(ErrorCode.USERID_NOT_FOUND,new ArrayList<>());

        // If user exists, store User
        User currUser = getUsers().get(userId);

        // retrieve registered courses for currUser
        try{
            File userRegCoursesFile = new File(usersDirPath + userId + "/" + regCoursesFilename);
            List<Course> userRegCoursesList = new LinkedList<>();
            Map<Integer, Course> searchCourseById = coursesMapById(); // load all courses as a HashMap (int courseId : Course obj)

            Scanner regCoursesFileRead = new Scanner(userRegCoursesFile);
            while (regCoursesFileRead.hasNext()) {
                String courseInformation_ = regCoursesFileRead.nextLine();
                String[] registeredCourseIds = courseInformation_.split("[|]");

                for (String courseIdStr : registeredCourseIds) {
                    int courseId = Integer.parseInt(courseIdStr);
                    Course registeredCourse = searchCourseById.get(courseId);
                    userRegCoursesList.add(registeredCourse);
                }
            }
            regCoursesFileRead.close();

            // SUCCESS = 0 - if no error occurred, return bid list
            return new Pair<>(ErrorCode.SUCCESS, userRegCoursesList);
        }catch(IOException e){
            return new Pair<>(ErrorCode.IO_ERROR, new ArrayList<>());
        }
    }
}
