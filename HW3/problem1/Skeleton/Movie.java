
import java.util.*;

public class Movie {
    private String title;
    private ArrayList<String> tags;

    public Movie(String title) {
        this.title = title;
    }

    public Movie(String title, String[] tags) {
        this.title = title;
        this.tags = createTagArrayList(tags);
    }

    private ArrayList<String> createTagArrayList(String[] tags) {
        ArrayList<String> tagArray = new ArrayList<>();
        for (String tag : tags)
            tagArray.add(tag);
        return tagArray;
    }

    public ArrayList<String> getTags(){
        return tags;
    }

    @Override
    public String toString() {
        return title;
    }
}
