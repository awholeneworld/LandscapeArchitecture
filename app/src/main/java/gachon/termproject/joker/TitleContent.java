package gachon.termproject.joker;

public class TitleContent {
    public String title;
    public String content;

    public TitleContent() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public TitleContent(String title, String content) {
        this.title = title;
        this.content = content;

    }

    public String getTitle() {
        return title;
    }
    public String getContent() { return content; }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) { this.content = content; }
}
