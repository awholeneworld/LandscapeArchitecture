package gachon.termproject.joker;

import java.util.ArrayList;

public class ExpertListContent {
    public String nickname;
    public ArrayList<String> images;

    public ExpertListContent(String nickname, ArrayList<String> images) {

        this.nickname = nickname;
        this.images = images;

    }


    public String getNickname() { return nickname; }
    public ArrayList<String> getImages() { return images; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setImages(int index, String image) { this.images.set(index, image); }

}
