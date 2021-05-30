package gachon.termproject.joker.Content;

import java.util.ArrayList;

public class RequestFromExpertContent {
    String expertUserId;
    String expertNickname;
    String expertProfileImg;
    String expertPortfolioImg;
    String expertPortfolioWeb;
    String expertPushToken;
    ArrayList<String> expertLocation;
    boolean isMatched;
    
    public RequestFromExpertContent() {}
    
    public RequestFromExpertContent(String expertNickname, String expertProfileImg, String expertPortfolioImg, String expertPortfolioWeb, String expertPushToken, ArrayList<String> expertLocation, boolean isMatched) {
        this.expertNickname = expertNickname;
        this.expertProfileImg = expertProfileImg;
        this.expertPortfolioImg = expertPortfolioImg;
        this.expertPortfolioWeb = expertPortfolioWeb;
        this.expertPushToken = expertPushToken;
        this.expertLocation = expertLocation;
        this.isMatched = isMatched;
    }

    public String getExpertUserId() { return expertUserId; }

    public String getExpertNickname() {
        return expertNickname;
    }

    public String getExpertProfileImg() {
        return expertProfileImg;
    }

    public String getExpertPortfolioImg() {
        return expertPortfolioImg;
    }

    public String getExpertPortfolioWeb() {
        return expertPortfolioWeb;
    }

    public String getExpertPushToken() { return expertPushToken; }

    public ArrayList<String> getExpertLocation() {
        return expertLocation;
    }
    
    public boolean getIsMatched() {
        return isMatched;
    }

    public void setExpertUserId(String expertUserId) { this.expertUserId = expertUserId; }

    public void setExpertNickname(String expertNickname) {
        this.expertNickname = expertNickname;
    }

    public void setExpertProfileImg(String expertProfileImg) {
        this.expertProfileImg = expertProfileImg;
    }

    public void setExpertPortfolioImg(String expertPortfolioImg) {
        this.expertPortfolioImg = expertPortfolioImg;
    }

    public void setExpertPortfolioWeb(String expertPortfolioWeb) {
        this.expertPortfolioWeb = expertPortfolioWeb;
    }

    public void setExpertLocation(ArrayList<String> expertLocation) {
        this.expertLocation = expertLocation;
    }
    
    public void setIsMatched(boolean isMatched) {
        this.isMatched = isMatched;
    }
}
