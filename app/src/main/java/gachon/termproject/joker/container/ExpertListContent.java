package gachon.termproject.joker.container;

import java.util.ArrayList;

public class ExpertListContent {
    public String userId;
    public String nickname;
    public String profileImg;
    public String portfolioImg;
    public String portfolioWeb;
    public ArrayList<String> location;

    public ExpertListContent(String userId, String nickname, String profileImg, String portfolioImg, String portfolioWeb, ArrayList<String> location) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.portfolioImg = portfolioImg;
        this.portfolioWeb = portfolioWeb;
        this.location = location;
    }

    public String getUserId() { return userId; }
    public String getNickname() { return nickname; }
    public String getProfileImg() { return profileImg; }
    public String getPortfolioImg() { return portfolioImg; }
    public String getPortfolioWeb() { return portfolioWeb; }
    public ArrayList<String> getLocation() { return location; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setProfileImg(String profileImg) { this.profileImg = profileImg; }
    public void setPortfolioImg(String portfolioImg) { this.portfolioImg = portfolioImg; }
    public void setPortfolioWeb(String portfolioWeb) { this.portfolioWeb = portfolioWeb; }
    public void setLocation(ArrayList<String> location) { this.location = location; }
}
