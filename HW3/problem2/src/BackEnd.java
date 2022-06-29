import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BackEnd extends ServerResourceAccessible {
    // Use getServerStorageDir() as a default directory
    // Create helper functions to support FrontEnd class
    // TODO sub-program 1 ~ 4 :

    // sub-problem 1

    public boolean userExists(String userId){
        File dataDirectory = new File(getServerStorageDir());
        File[] userDirectories = dataDirectory.listFiles(); // array of user directories (folders)

        LinkedList<String> userNames = new LinkedList<>();
        for (File userDir : userDirectories) {
            userNames.add(userDir.getName());
        }

        if (userNames.contains(userId)) return true;
        else return false;

    }

    public String getStoredPassword(String userId){
        File testFile = new File(getServerStorageDir() + userId + "/password.txt");
        try{
            Scanner input = new Scanner(testFile);
            return input.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // sub-problem 2
    public void storePost(String userId, String title, String content){
        File dataDirectory = new File(getServerStorageDir());
        File[] userDirectories = dataDirectory.listFiles(); // array of user directories (folders)
        int largestPostId = -1;

        LinkedList<String> userNames = new LinkedList<>();
        for (File userDir : userDirectories) {
            userNames.add(userDir.getName());
        }

        // create list of all posts from all users
        for (String username : userNames) {
            File postDirectory = new File(getServerStorageDir() + username + "/post");
            File[] postFiles = postDirectory.listFiles();
            for (File postFile : postFiles) {
                String filename = postFile.getName();
                filename = filename.substring(0, filename.indexOf('.'));
                int currentPostId = Integer.parseInt(filename);
                if (currentPostId > largestPostId)
                    largestPostId = currentPostId;
            }
        }
        Post newPost = new Post(title, content);
        newPost.setId(largestPostId+1);

        try {
            FileWriter fileWriter = new FileWriter(getServerStorageDir() + userId + "/post/" + newPost.getId() + ".txt");
            fileWriter.write(newPost.getDate()+"\n"+newPost.getTitle() + "\n\n" + newPost.getContent());
            fileWriter.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    // sub-problem 3
    public LinkedList<Post> getSortedFriendsPosts(String userId){
        // create list of friends
        LinkedList<String> friendList = new LinkedList<>();
        File friendFile = new File(getServerStorageDir()+userId+"/friend.txt");
        try{
            Scanner input = new Scanner(friendFile);
            while (input.hasNext()){
                friendList.add(input.nextLine());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        // create list of posts from all friends
        LinkedList<Post> postList = new LinkedList<>();
        for (String friendId : friendList){
            File postDirectory = new File(getServerStorageDir()+friendId+"/post");
            File[] postFiles = postDirectory.listFiles();
            for (File postFile : postFiles) {
                try {
                    Scanner postContent = new Scanner(postFile);
                    String dateTime = postContent.nextLine();
                    String title = postContent.nextLine();
                    postContent.nextLine();
                    String content = "";

                    while (postContent.hasNext()) {
                        content = content + postContent.nextLine() + "\n";
                    }

                    // create new Post object
                    Post newPost = new Post(title, content);
                    String filename = postFile.getName();
                    newPost.setId(Integer.parseInt(filename.substring(0, filename.indexOf('.'))));
                    newPost.setDateTime(Post.parseDateTimeString(dateTime, Post.getFormatter()));
                    postList.add(newPost);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        // sort posts by dateTime
        Collections.sort(postList, new dateTimeSort());
        return postList;
    }

    // sub-problem 4
    public LinkedList<Post> searchPosts(Set<String> keywords) {
        // create list of posts from all friends
        LinkedList<Post> allPosts = new LinkedList<>();

        File dataDirectory = new File(getServerStorageDir());
        File[] userDirectories = dataDirectory.listFiles(); // array of user directories (folders)

        LinkedList<String> userNames = new LinkedList<>();
        for (File userDir : userDirectories) {
            userNames.add(userDir.getName());
        }

        // create list of all posts from all users
        for (String userId : userNames) {
            File postDirectory = new File(getServerStorageDir() + userId + "/post");
            File[] postFiles = postDirectory.listFiles();
            for (File postFile : postFiles) {
                try {
                    Scanner postContent = new Scanner(postFile);
                    String dateTime = postContent.nextLine();
                    String title = postContent.nextLine();
                    postContent.nextLine();
                    String content = "";

                    while (postContent.hasNext()) {
                        String nextLine = postContent.nextLine();
                        content = content + nextLine + "\n";
                    }

                    // create new Post object
                    Post newPost = new Post(title, content);
                    newPost.setContentLength(content.split("\\s+").length);
                    String filename = postFile.getName();
                    newPost.setId(Integer.parseInt(filename.substring(0, filename.indexOf('.'))));
                    newPost.setDateTime(Post.parseDateTimeString(dateTime, Post.getFormatter()));
                    allPosts.add(newPost);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Copy keywords from set to string array
        String[] keywordArray = new String[keywords.size()];
        int index = 0;
        for (String keyword : keywords){
            keywordArray[index] = keyword;
            index++;
        }

        // count keyword occurrences for each post
        ListIterator<Post> iterator = allPosts.listIterator();
        while (iterator.hasNext()) {
            Post currentPost = iterator.next();
            for (String keyword : keywordArray) {
                currentPost.countKeywordOccurrences(keyword);
            }
        }

        Collections.sort(allPosts, new keywordSearchSort());
        return allPosts;
    }
}

class dateTimeSort implements Comparator<Post>{
    @Override
    public int compare(Post post1, Post post2){
        return post2.getDate().compareTo(post1.getDate());
    }
}

class keywordOccurrencesSort implements Comparator<Post>{
    @Override
    public int compare(Post post1, Post post2){
        if (post1.getTotalKeywordOccurrences() == post2.getTotalKeywordOccurrences()) return 0;
        return post1.getTotalKeywordOccurrences() > post2.getTotalKeywordOccurrences() ? -1 : 1;
    }
}

class contentLengthSort implements Comparator<Post>{
    @Override
    public int compare(Post post1, Post post2){
        if (post1.getContentLength() == post2.getContentLength()) return 0;
        return post1.getContentLength() > post2.getContentLength() ? -1 : 1;
    }
}

class keywordSearchSort implements Comparator<Post>{
    @Override
    public int compare(Post post1, Post post2){
        if (post1.getTotalKeywordOccurrences() == post2.getTotalKeywordOccurrences()){
            if (post1.getContentLength() == post2.getContentLength()) return 0;
            return post1.getContentLength() > post2.getContentLength() ? -1 : 1;
        }
        else return post1.getTotalKeywordOccurrences() > post2.getTotalKeywordOccurrences() ? -1 : 1;
    }
}
