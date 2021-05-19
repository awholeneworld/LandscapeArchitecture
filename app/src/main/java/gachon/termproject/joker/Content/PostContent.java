package gachon.termproject.joker.Content;

import java.util.ArrayList;

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
    public String expertId; // 리뷰 게시판 작성에 필요한 변수
    public Boolean expertBool; // 매칭 게시판 작성에 필요한 변수
    public Boolean isMatched; // 매칭 게시판 작성에 필요한 변수

    // CommunityListStyle 에서 snapshot.getValue(PostContent.class) 사용할 때 Default Constructor 꼭 있어야함
    public PostContent() {
    }

    //일반 포스트용 생성자
    public PostContent(String category, String userId, String profileImg, String title, String nickname, String postTime, String postId, ArrayList<String> content, ArrayList<String> images, String expertId, Boolean expertBool, ArrayList<String> location, Boolean isMatched) {
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
        this.expertBool = expertBool;
        this.location = location;
        this.isMatched = isMatched;
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
    public String getExpertId() { return expertId; }
    public Boolean getExpertBool(){ return expertBool; }
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
    public void setLocation(ArrayList<String> location){this.location = location;}
    public void setExpertId(String expertId) { this.expertId = expertId; }
    public void setExpertBool(Boolean expertBool) { this.expertBool = expertBool; }
    public void setIsMatched(Boolean isMatched) { this.isMatched = isMatched; }
}
