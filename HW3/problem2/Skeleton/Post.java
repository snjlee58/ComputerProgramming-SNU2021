import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.*;

class Post {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private int id;
    private final static int ID_NOT_INITIATED = -1;
    private LocalDateTime dateTime;
    private String title, content;
    private int totalKeywordOccurrences;
    private int contentLength;

    Post(String title, String content) {
        this(ID_NOT_INITIATED, LocalDateTime.now(), title, content);
    }

    Post(int id, LocalDateTime dateTime, String title, String content) {
        this.id = id;
        this.dateTime = dateTime;
        this.title = title;
        this.content = content.trim();
        this.totalKeywordOccurrences = 0;
        this.contentLength = 0;
    }

    String getSummary() {
        return String.format("id: %d, created at: %s, title: %s", id, getDate(), title);
    }

    @Override
    public String toString() {
        return String.format(
            "-----------------------------------\n" +
            "id: %d\n" +
            "created at: %s\n" +
            "title: %s\n" +
            "content: %s"
            , id, getDate(), title, content);
}

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    String getDate() {
        return dateTime.format(formatter);
    }

    void setDateTime(LocalDateTime dateTime){
        this.dateTime = dateTime;
    }

    void setContentLength(int contentLength){ this.contentLength = contentLength; }

    String getTitle() {
        return title;
    }

    String getContent() {
        return content;
    }

    int getContentLength(){ return contentLength; }

    void countKeywordOccurrences(String keyword){
        String[] text = {title, content};
        int keywordOccurrenceInText = 0;

        for (String t : text){
            String[] contentWords_ = t.split("\\s+");
            List<String> contentWords = new LinkedList<>();
            for (String word : contentWords_) contentWords.add(word);

            keywordOccurrenceInText = Collections.frequency(contentWords, keyword);
            totalKeywordOccurrences += keywordOccurrenceInText;
        }


    }

    int getTotalKeywordOccurrences() { return totalKeywordOccurrences; }

    static DateTimeFormatter getFormatter(){ return formatter; }

    static LocalDateTime parseDateTimeString(String dateString, DateTimeFormatter dateTimeFormatter) {
        return LocalDateTime.parse(dateString,dateTimeFormatter);
    }
}
