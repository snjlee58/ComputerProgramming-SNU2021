
import java.util.*;

public class MovieApp {
    Map<String, Movie> movies = new HashMap<>();
    Map<String, User> users = new HashMap<>();
    Map<String, ArrayList<Integer>> ratings = new HashMap<>();

    public boolean addMovie(String title, String[] tags) {
        // TODO sub-problem 1

        return true;
    }

    public boolean addUser(String name) {
        // TODO sub-problem 1

        return true;
    }

    public Movie findMovie(String title) {
        // TODO sub-problem 1

        return null;
    }

    public User findUser(String username) {
        // TODO sub-problem 1

        return null;
    }

    public List<Movie> findMoviesWithTags(String[] tags) {
        // TODO sub-problem 2

        return null;
    }
    

    public boolean rateMovie(User user, String title, int rating) {
        // TODO sub-problem 3

        return true;
    }

    public int getUserRating(User user, String title) {
        // TODO sub-problem 3

        return 0;
    }

    public List<Movie> findUserMoviesWithTags(User user, String[] tags) {
        // TODO sub-problem 4

        return null;
    }

    public List<Movie> recommend(User user) {
        // TODO sub-problem 4

        return null;
    }


}

