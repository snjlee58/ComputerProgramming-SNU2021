import java.util.*;
import java.time.LocalDateTime;

public class FrontEnd {
    private UserInterface ui;
    private BackEnd backend;
    private User user;

    public FrontEnd(UserInterface ui, BackEnd backend) {
        this.ui = ui;
        this.backend = backend;
    }
    
    public boolean auth(String authInfo) {
        // TODO sub-problem 1
        String inputId = authInfo.substring(0, authInfo.indexOf("\n"));
        String inputPw = authInfo.substring(authInfo.indexOf("\n") + 1);
        this.user = new User(inputId, inputPw);

        if (!backend.userExists(inputId)) return false;

        if (inputPw.equalsIgnoreCase(backend.getStoredPassword(inputId))) return true;
        else return false;
    }

    public void post(Pair<String, String> titleContentPair) {
        // TODO sub-problem 2
        String title = titleContentPair.key;
        String content = titleContentPair.value;

        backend.storePost(user.id, title, content);
    }
    
    public void recommend(int N){
        // TODO sub-problem 3
        List<Post> sortedFriendsPosts = backend.getSortedFriendsPosts(user.id);
        List<Post> recommendedPosts;

        // collect up to the first N posts
        if (sortedFriendsPosts.size() > N){
            recommendedPosts = sortedFriendsPosts.subList(0, N);
        }
        else recommendedPosts = sortedFriendsPosts;

        // print recommended posts
        for (Post post : recommendedPosts){
            ui.println(post.toString());
        }
    }

    public void search(String command) {
        // TODO sub-problem 4
        String[] commandSlices = command.trim().split("\\s+");
        Set<String> keywords = new HashSet<>();
        for (int i = 1; i < commandSlices.length; i++)
            keywords.add(commandSlices[i]);

        List<Post> searchSorted = backend.searchPosts(keywords);
        List<Post> foundPosts = new LinkedList<>();

        int numPosts = 0;
        ListIterator<Post> iterator = searchSorted.listIterator();

        while (iterator.hasNext()){
            Post currentPost = iterator.next();
            if (currentPost.getTotalKeywordOccurrences() <= 0) break;
            else if (numPosts >= 10) break;

            foundPosts.add(currentPost);
            numPosts++;
        }

        // print searched posts
        for (Post post : foundPosts){
            ui.println(post.getSummary());
        }

    }
    
    User getUser(){
        return user;
    }
}
