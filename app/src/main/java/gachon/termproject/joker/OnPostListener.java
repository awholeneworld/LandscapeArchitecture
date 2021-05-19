package gachon.termproject.joker;

import gachon.termproject.joker.Content.PostContent;

public interface OnPostListener {
    void onPost();
    void onDelete(PostContent postContent);
    void onModify();
}
