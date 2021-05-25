package gachon.termproject.joker.Content;

public class PostCommentContent {
    public String category;
    public String userId;
    public String nickname;
    public String profileImg;
    public String commentTime;
    public String commentId;
    public String content;

    // SeePostActivity 에서 snapshot.getValue(PostCommentContent.class) 사용할 때 Default Constructor 꼭 있어야함
    public PostCommentContent() {
    }

    public PostCommentContent(String category, String userId, String nickname, String profileImg, String commentTime, String commentId, String content) {
        this.category = category;
        this.userId = userId;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.commentTime = commentTime;
        this.commentId = commentId;
        this.content = content;
    }

    public String getCategory() { return category; }
    public String getUserId() { return userId; }
    public String getNickname() { return nickname; }
    public String getProfileImg() { return profileImg; }
    public String getCommentTime() { return commentTime; }
    public String getCommentId() { return commentId; }
    public String getContent() { return content; }
    public void setCategory() { this.category = category; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setProfileImg(String profileImg) { this.profileImg = profileImg; }
    public void setCommentTime(String commentTime) { this.commentTime = commentTime; }
    public void setCommentId(String commentId) { this.commentId = commentId; }
    public void setContent(String content) { this.content = content; }
}
