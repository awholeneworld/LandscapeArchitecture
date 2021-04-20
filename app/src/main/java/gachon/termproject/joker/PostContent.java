package gachon.termproject.joker;

import android.net.Uri;

import java.util.ArrayList;

public class PostContent {
    public String id;
    public String title;
    public String postId;
    public ArrayList<String> content;
    public ArrayList<String> images;
    public ArrayList<Integer> order;

    public PostContent() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public PostContent(String id, String title, String postId, ArrayList<String> content, ArrayList<String> images, ArrayList<Integer> order) {
        this.id = id;
        this.title = title;
        this.postId = postId;
        this.content = content;
        this.images = images;
        this.order = order;
    }

    public String getId() { return id; }
    public String getTitle() {
        return title;
    }
    public String getPostId() { return postId; }
    public ArrayList<String> getContent() { return content; }
    public ArrayList<String> getImages() { return images; }
    public ArrayList<Integer> getOrder() { return order; }
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setPostId(String postId) { this.postId = postId; }
    public void setContent(ArrayList<String> content) { this.content = content; }
    public void setImages(ArrayList<String> images) { this.images = images; }
    public void setOrder(ArrayList<Integer> order) { this.order = order; }
}
