package gachon.termproject.joker;

import java.util.ArrayList;

public class UserInfo {
    public static String userId;
    public static String email;
    public static String nickname;
    public static String profileImg;
    public static String introduction;
    public static String portfolioImg;
    public static String portfolioWeb;
    public static boolean isPublic;
    public static ArrayList<String> location;

    public String getUserId() { return this.userId; }
    public String getEmail() { return this.email; }
    public String getNickname() { return this.nickname; }
    public String getProfileImg() { return this.profileImg; }
    public String getIntroduction() { return this.introduction; }
    public String getPortfolioImg() { return this.portfolioImg; }
    public String getPortfolioWeb() { return this.portfolioWeb; }
    public boolean getIsPublic() { return this.isPublic; }
    public ArrayList<String> getLocation() { return this.location; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setProfileImg(String profileImg) { this.profileImg = profileImg; }
    public void setPortfolioImg(String portfolioImg) { this.portfolioImg = portfolioImg; }
    public void setPortfolioWeb(String portfolioWeb) { this.portfolioWeb = portfolioWeb; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public void setIsPublic(boolean isPublic) { this.isPublic = isPublic; }
    public void setLocation(ArrayList<String> location) { this.location = location; }
}
