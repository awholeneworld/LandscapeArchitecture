package gachon.termproject.joker;

import java.util.ArrayList;

public class ExpertListContent {
    public String userId;
    public String nickname;
    //public ArrayList<String> images;

    public ExpertListContent(String userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
        //this.images = images;

    }

    public String getUserId() { return userId; }
    public String getNickname() { return nickname; }
    //public ArrayList<String> getImages() { return images; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setUserId(String userId) { this.userId = userId; }
    //public void setImages(int index, String image) { this.images.set(index, image); }

}
