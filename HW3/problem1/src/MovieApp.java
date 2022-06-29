
import java.util.*;

public class MovieApp {
    Map<String, Movie> movies = new HashMap<>();
    Map<String, User> users = new HashMap<>();
    Map<String, ArrayList<Integer>> ratings = new HashMap<>();

    public boolean addMovie(String title, String[] tags) {
        // TODO sub-problem 1
        if (movies.containsKey(title))
            return false;
        else if (tags.length == 0)
            return false;
        else {
            Set<String> tagSet = new HashSet<String>(Arrays.asList(tags));
            tags = tagSet.toArray(String[]::new);
            Movie newMovie = new Movie(title, tags);
            movies.put(title, newMovie);
            return true;
        }
    }

    public boolean addUser(String name) {
        // TODO sub-problem 1
        if (users.containsKey(name))
            return false;
        else {
            User newUser = new User(name);
            users.put(name, newUser);
            return true;
        }
    }

    public Movie findMovie(String title) {
        // TODO sub-problem 1
        if (movies.containsKey(title)) {
            return movies.get(title);
        }
        else
            return null;
    }

    public User findUser(String username) {
        // TODO sub-problem 1
        if (users.containsKey((username))) {
            return users.get(username);
        }
        else
            return null;
    }

    public List<Movie> findMoviesWithTags(String[] tags) {
        // TODO sub-problem 2
        List<Movie> foundMovies = new LinkedList<>();

        // if tags argument is empty or null, return empty list
        if (tags == null || tags.length == 0) return foundMovies;

        // convert tags array to ArrayList (remove any duplicates)
        Set<String> tagSet = new HashSet<String>(Arrays.asList(tags));
        tags = tagSet.toArray(String[]::new);
        ArrayList<String> tagsToSearch = new ArrayList<>();
        for (String tag : tags)
            tagsToSearch.add(tag);

        // add movie to list it meets the specified conditions
        for (String title : movies.keySet()) {
            if (movies.get(title).getTags().containsAll(tagsToSearch) && !foundMovies.contains(movies.get(title))) {
                foundMovies.add(movies.get(title));
            }
        }
        Collections.sort(foundMovies, new titleSort());
        return foundMovies;
    }

    class titleSort implements Comparator<Movie>{
        public int compare(Movie movie1, Movie movie2){
            if (movie1.toString().compareTo(movie2.toString()) == 0)
                return 0;
            return movie1.toString().compareTo(movie2.toString()) > 0 ? -1 : 1;
        }
    }

    public boolean rateMovie(User user, String title, int rating) {
        // TODO sub-problem 3
        if (title == null || !movies.containsKey(title))
            return false;
        else if (user == null || !users.containsValue(user))
            return false;
        else if (!(rating >= 1 && rating <= 5))
            return false;
        else {
            // sub-problem 4
                // if movie is being rated for the first time
            if (!ratings.containsKey(title))
                ratings.put(title, new ArrayList<Integer>());

                // if user has rated this before, remove the old rating
            if (user.getRatedMovies().containsKey(title)){
                ratings.get(title).remove(user.getRatedMovies().get(title));
            }
            ratings.get(title).add(rating);

            // update or add user's rating
            user.addRating(title, rating);

            return true;
        }
    }

    public int getUserRating(User user, String title) {
        // TODO sub-problem 3
        if (user == null || !users.containsValue(user) || movies.get(title) == null || !movies.containsKey(title))
            return -1;
        else if (!user.getRatedMovies().containsKey(title))
            return 0;
        else
            return user.getRatedMovies().get(title);
    }

    public List<Movie> findUserMoviesWithTags(User user, String[] tags) {
        // TODO sub-problem 4
        LinkedList<Movie> matchedMovies = new LinkedList<>();
        if (user == null || !users.containsValue(user))
            return matchedMovies;
        else{
            List<Movie> foundMovies = findMoviesWithTags(tags);

            ListIterator<Movie> iterator = foundMovies.listIterator();
            while (iterator.hasNext()){
                Movie foundMovie = iterator.next();

                Integer currentCount = user.getSearchHistory().get(foundMovie);
                if (currentCount == null)
                    currentCount = 0;
                user.getSearchHistory().put(foundMovie, ++currentCount);
            }
            return foundMovies;
        }
    }

    public List<Movie> recommend(User user) {
        Comparator<Movie> lexicographicalComparator = new Comparator<Movie>() {
            @Override
            public int compare(Movie movie1, Movie movie2) {
                return movie1.toString().compareTo(movie2.toString());
            }
        };

        Comparator<Movie> ratingComparator = new Comparator<Movie>() {
            @Override
            public int compare(Movie movie1, Movie movie2) {
                ArrayList<Integer> movie1Ratings = ratings.get(movie1.toString());
                ArrayList<Integer> movie2Ratings = ratings.get(movie2.toString());

                if (getMedian(movie1Ratings) == getMedian(movie2Ratings)) return 0;
                return getMedian(movie1Ratings) > getMedian(movie2Ratings) ? -1 : 1;
            }
        };


        // TODO sub-problem 4
        LinkedList<Movie> recommendedMovies = new LinkedList<>();
        if (user == null || !users.containsValue(user))
            return recommendedMovies;

        LinkedList<Movie> candidates = new LinkedList<>();
        for (Movie movie : user.getSearchHistory().keySet()){
            candidates.add(movie);
        }

        Collections.sort(candidates, lexicographicalComparator);
        Collections.sort(candidates, ratingComparator);
        candidates = user.searchCountSort(candidates);

        ListIterator<Movie> iterator = candidates.listIterator();
        while (iterator.hasNext()) {
            Movie movieToRecommend = iterator.next();
            recommendedMovies.add(movieToRecommend);
            if (recommendedMovies.size() >= 3) break;
        }

        return recommendedMovies;
    }

    private double getMedian(ArrayList<Integer> ratingsList){
        if (ratingsList == null) return 0.0;
        else{
            Collections.sort(ratingsList);
            double median = -1.0;

            // if there's an odd number of ratings, return the median rating
            if (ratingsList.size() % 2 == 1) {
                int medianIndex = ratingsList.size() / 2;
                median = (double)ratingsList.get(medianIndex);
            }

            // if there's an even number of ratings, use the average value of the two middle values as the median rating
            else{
                int upperIndex = ratingsList.size() / 2;
                int lowerIndex = ratingsList.size() / 2 - 1;
                median = (ratingsList.get(lowerIndex) + ratingsList.get(upperIndex)) / (double)2;
            }
            return median;
        }


    }


}

