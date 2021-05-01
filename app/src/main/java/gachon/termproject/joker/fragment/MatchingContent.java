package gachon.termproject.joker.fragment;

import java.util.ArrayList;

public class MatchingContent {
    public String nickname;
    public ArrayList<String> images;

    public MatchingContent(String nickname, ArrayList<String> images) {

        this.nickname = nickname;
        this.images = images;

    }


    public String getNickname() { return nickname; }
    public ArrayList<String> getImages() { return images; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setImages(int index, String image) { this.images.set(index, image); }

}
