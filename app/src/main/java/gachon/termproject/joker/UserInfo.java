package gachon.termproject.joker;

import java.util.List;

public class UserInfo {
    public static String userId;
    public static String nickname;
    public static String profileImg;
    public static Boolean isPublic;
    public static List<String> location;

    public String getUserId() { return this.userId; }
    public String getNickname() { return this.nickname; }
    public String getProfileImg() { return this.profileImg; }
    public Boolean getIsPublic() { return this.isPublic; }
    public List<String> getLocation() { return this.location; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setProfileImg(String profileImg) { this.profileImg = profileImg; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
    public void setLocation(List<String> location) { this.location = location; }
}
