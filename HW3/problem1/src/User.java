

import java.util.*;

public class User {
    private String username;
    private HashMap<String, Integer> ratedMovies;
    private HashMap<Movie, Integer> searchHistory;

    public User(String username) {
        this.username = username;
        this.ratedMovies = new HashMap<>();
        this.searchHistory = new HashMap<>();
    }

    public void addRating(String title, int rating){
        ratedMovies.put(title, rating);
    }

    public HashMap<String, Integer> getRatedMovies(){
        return ratedMovies;
    }

    public HashMap<Movie, Integer> getSearchHistory() { return searchHistory; }

    public LinkedList<Movie> searchCountSort(LinkedList<Movie> candidates) {
        Comparator<Movie> searchCountComparator = new Comparator<Movie>() {
            @Override
            public int compare(Movie movie1, Movie movie2) {
                if (searchHistory.get(movie1) == searchHistory.get(movie2)) return 0;
                return searchHistory.get(movie1) > searchHistory.get(movie2) ? -1 : 1;
            }
        };

        Collections.sort(candidates, searchCountComparator);
        return candidates;
    }

    @Override
    public String toString() {
        return username;
    }
}


