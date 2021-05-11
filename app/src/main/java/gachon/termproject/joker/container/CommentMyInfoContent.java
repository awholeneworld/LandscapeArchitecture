package gachon.termproject.joker.container;

public class CommentMyInfoContent {
    public String comment;
    public String postTitle;

    public CommentMyInfoContent() {

    }

    public CommentMyInfoContent(String comment, String postTitle) {
        this.comment = comment;
        this.postTitle = postTitle;
    }

    public String getComment() {
        return comment;
    }
    public String getPostTitle() {
        return postTitle;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }
}
