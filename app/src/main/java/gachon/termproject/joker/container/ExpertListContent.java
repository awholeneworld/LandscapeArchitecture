package gachon.termproject.joker.container;

public class ExpertListContent {
    public String userId;
    public String nickname;
    public String profileImg;

    public ExpertListContent(String userId, String nickname, String profileImg) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }

    public String getUserId() { return userId; }
    public String getNickname() { return nickname; }
    public String getProfileImg() { return profileImg; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setProfileImg(String profileImg) { this.profileImg = profileImg; }

}
