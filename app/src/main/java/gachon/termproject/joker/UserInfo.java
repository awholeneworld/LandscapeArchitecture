package gachon.termproject.joker;

import java.util.ArrayList;

public class UserInfo {
    private static String userId;
    private static String email;
    private static String nickname;
    private static String profileImg;
    private static String introduction;
    private static String portfolioImg;
    private static String portfolioWeb;
    private static String pushToken;
    private static boolean isPublic;
    private static ArrayList<String> location;

    public static String getUserId() { return userId; }
    public static String getEmail() { return email; }
    public static String getNickname() { return nickname; }
    public static String getProfileImg() { return profileImg; }
    public static String getIntroduction() { return introduction; }
    public static String getPortfolioImg() { return portfolioImg; }
    public static String getPortfolioWeb() { return portfolioWeb; }
    public static String getPushToken() { return pushToken; }
    public static boolean getIsPublic() { return isPublic; }
    public static ArrayList<String> getLocation() { return location; }
    public static void setUserId(String newUserId) { userId = newUserId; }
    public static void setEmail(String newEmail) { email = newEmail; }
    public static void setNickname(String newNickname) { nickname = newNickname; }
    public static void setPushToken(String newPushToken) { pushToken = newPushToken; }
    public static void setProfileImg(String newProfileImg) { profileImg = newProfileImg; }
    public static void setPortfolioImg(String newPortfolioImg) { portfolioImg = newPortfolioImg; }
    public static void setPortfolioWeb(String newPortfolioWeb) { portfolioWeb = newPortfolioWeb; }
    public static void setIntroduction(String newIntroduction) { introduction = newIntroduction; }
    public static void setIsPublic(boolean newIsPublic) { isPublic = newIsPublic; }
    public static void setLocation(ArrayList<String> newLocation) { location = newLocation; }
}
