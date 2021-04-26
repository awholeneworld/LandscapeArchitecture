package gachon.termproject.joker;

public interface OnPostListener {
    void onPost();
    void onDelete(PostContent postContent);
    void onModify();
}
