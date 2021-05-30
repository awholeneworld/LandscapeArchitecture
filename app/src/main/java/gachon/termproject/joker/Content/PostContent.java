package gachon.termproject.joker.Content;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

// 게시할 글에 들어가는 정보들을 모아주는 클래스
public class PostContent implements Parcelable {
    public String category;
    public String userId;
    public String profileImg;
    public String title;
    public String nickname;
    public String postTime;
    public String postId;
    public String pushToken;
    public String intro;
    public String expertId; // 리뷰 게시판 작성에 필요한 변수
    public ArrayList<String> content;
    public ArrayList<String> images;
    public ArrayList<String> location;

    // CommunityListStyle 에서 snapshot.getValue(PostContent.class) 사용할 때 Default Constructor 꼭 있어야함
    public PostContent() {
    }

    public PostContent(String category, String userId, String profileImg, String title, String nickname, String postTime, String postId, String pushToken, ArrayList<String> content, ArrayList<String> images, String expertId, String intro, ArrayList<String> location) {
        this.category = category;
        this.userId = userId;
        this.profileImg = profileImg;
        this.title = title;
        this.nickname = nickname;
        this.postTime = postTime;
        this.postId = postId;
        this.pushToken = pushToken;
        this.content = content;
        this.images = images;
        this.expertId = expertId;
        this.intro = intro;
        this.location = location;
    }

    protected PostContent(Parcel in) {
        category = in.readString();
        userId = in.readString();
        profileImg = in.readString();
        title = in.readString();
        nickname = in.readString();
        postTime = in.readString();
        postId = in.readString();
        pushToken = in.readString();
        content = in.createStringArrayList();
        images = in.createStringArrayList();
        expertId = in.readString();
        intro = in.readString();
        location = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(userId);
        dest.writeString(profileImg);
        dest.writeString(title);
        dest.writeString(nickname);
        dest.writeString(postTime);
        dest.writeString(postId);
        dest.writeString(pushToken);
        dest.writeStringList(content);
        dest.writeStringList(images);
        dest.writeString(expertId);
        dest.writeString(intro);
        dest.writeStringList(location);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostContent> CREATOR = new Creator<PostContent>() {
        @Override
        public PostContent createFromParcel(Parcel in) {
            return new PostContent(in);
        }

        @Override
        public PostContent[] newArray(int size) {
            return new PostContent[size];
        }
    };

    public String getCategory() { return category; }
    public String getUserId() { return userId; }
    public String getProfileImg() { return profileImg; }
    public String getTitle() {
        return title;
    }
    public String getNickname() { return nickname; }
    public String getPostTime() { return postTime; }
    public String getPostId() { return postId; }
    public String getPushToken() { return pushToken; }
    public String getExpertId() { return expertId; }
    public String getIntro() { return intro; }
    public ArrayList<String> getContent() { return content; }
    public ArrayList<String> getImages() { return images; }
    public ArrayList<String> getLocation(){ return location; }
    public void setCategory() { this.category = category; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setProfileImg(String url) { this.profileImg = url; }
    public void setTitle(String title) { this.title = title; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setPostTime(String postTime) { this.postTime = postTime; }
    public void setPostId(String postId) { this.postId = postId; }
    public void setContent(ArrayList<String> content) { this.content = content; }
    public void setImages(int index, String image) { this.images.set(index, image); }
    public void setExpertId(String expertId) { this.expertId = expertId; }
}
