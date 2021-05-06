package gachon.termproject.joker.container;

public class ExpertListContent {
    public String nickname;
    public String profileImg;

    public ExpertListContent(String nickname, String profileImg) {
        this.nickname = nickname;
        this.profileImg = profileImg;
    }

    public String getNickname() { return nickname; }
    public String getProfileImg() { return profileImg; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setProfileImg(String profileImg) { this.profileImg = profileImg; }

}
