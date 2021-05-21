package gachon.termproject.joker.Content;

import java.util.ArrayList;
import java.util.HashMap;

// 게시할 글에 들어가는 정보들을 모아주는 클래스
public class PostContent {
    public String category;
    public String userId;
    public String profileImg;
    public String title;
    public String nickname;
    public String postTime;
    public String postId;
    public ArrayList<String> content;
    public ArrayList<String> images;
    public ArrayList<String> location;
    public HashMap<String, RequestFromExpertContent> requests = new HashMap<>();
    public String expertId; // 리뷰 게시판 작성에 필요한 변수
    public Boolean isMatched; // 매칭 게시판 작성에 필요한 변수

    // CommunityListStyle 에서 snapshot.getValue(PostContent.class) 사용할 때 Default Constructor 꼭 있어야함
    public PostContent() {
    }

    public PostContent(String category, String userId, String profileImg, String title, String nickname, String postTime, String postId, ArrayList<String> content, ArrayList<String> images, String expertId, ArrayList<String> location, Boolean isMatched) {
        this.category = category;
        this.userId = userId;
        this.profileImg = profileImg;
        this.title = title;
        this.nickname = nickname;
        this.postTime = postTime;
        this.postId = postId;
        this.content = content;
        this.images = images;
        this.expertId = expertId;
        this.location = location;
        this.isMatched = isMatched;
    }

    public PostContent(String category, String userId, String profileImg, String title, String nickname, String postTime, String postId, ArrayList<String> content, ArrayList<String> images, String expertId, ArrayList<String> location, Boolean isMatched, HashMap<String, RequestFromExpertContent> requests) {
        this.category = category;
        this.userId = userId;
        this.profileImg = profileImg;
        this.title = title;
        this.nickname = nickname;
        this.postTime = postTime;
        this.postId = postId;
        this.content = content;
        this.images = images;
        this.expertId = expertId;
        this.location = location;
        this.isMatched = isMatched;
        this.requests = requests;
    }

    public String getCategory() { return category; }
    public String getUserId() { return userId; }
    public String getProfileImg() { return profileImg; }
    public String getTitle() {
        return title;
    }
    public String getNickname() { return nickname; }
    public String getPostTime() { return postTime; }
    public String getPostId() { return postId; }
    public ArrayList<String> getContent() { return content; }
    public ArrayList<String> getImages() { return images; }
    public ArrayList<String> getLocation(){ return location; }
    public HashMap<String, RequestFromExpertContent> getRequests(){ return requests; }
    public String getExpertId() { return expertId; }
    public Boolean getIsMatched(){ return isMatched; }
    public void setCategory() { this.category = category; }
    public void setUserId(String id) { this.userId = userId; }
    public void setProfileImg(String url) { this.profileImg = url; }
    public void setTitle(String title) { this.title = title; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setPostTime(String postTime) { this.postTime = postTime; }
    public void setPostId(String postId) { this.postId = postId; }
    public void setContent(ArrayList<String> content) { this.content = content; }
    public void setImages(int index, String image) { this.images.set(index, image); }
    public void setLocation(ArrayList<String> location) { this.location = location; }
    public void setRequests(HashMap<String, RequestFromExpertContent> requestsList) { this.requests = requests; };
    public void setExpertId(String expertId) { this.expertId = expertId; }
    public void setIsMatched(Boolean isMatched) { this.isMatched = isMatched; }
}
