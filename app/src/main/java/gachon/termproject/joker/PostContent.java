package gachon.termproject.joker;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;

public class PostContent {
    public String userId;
    public String title;
    public String nickname;
    public String postId;
    public ArrayList<String> content;
    public ArrayList<String> images;
    public ArrayList<Integer> order;

    // CommunityListStyle 에서 snapshot.getValue(PostContent.class) 사용할 때 Default Constructor 꼭 있어야함
    public PostContent() {
    }

    public PostContent(String userId, String title, String nickname, String postId, ArrayList<String> content, ArrayList<String> images, ArrayList<Integer> order) {
        this.userId = userId;
        this.title = title;
        this.nickname = nickname;
        this.postId = postId;
        this.content = content;
        this.images = images;
        this.order = order;
    }

    public String getUserId() { return userId; }
    public String getTitle() {
        return title;
    }
    public String getNickname() { return nickname; }
    public String getPostId() { return postId; }
    public ArrayList<String> getContent() { return content; }
    public ArrayList<String> getImages() { return images; }
    public ArrayList<Integer> getOrder() { return order; }
    public void setUserId(String id) { this.userId = userId; }
    public void setTitle(String title) { this.title = title; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setPostId(String postId) { this.postId = postId; }
    public void setContent(ArrayList<String> content) { this.content = content; }
    public void setImages(int index, String image) { this.images.set(index, image); }
    public void setOrder(ArrayList<Integer> order) { this.order = order; }
}
