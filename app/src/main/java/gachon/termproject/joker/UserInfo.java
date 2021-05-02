package gachon.termproject.joker;

public class UserInfo {
    public static String userId;
    public static String nickname;
    public static String profileImg;

    public String getUserId() { return this.userId; }
    public String getNickname() { return this.nickname; }
    public String getProfileImg() { return this.profileImg; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setProfileImg(String profileImg) { this.profileImg = profileImg; }
}
