package gachon.termproject.joker;

import gachon.termproject.joker.container.PostContent;

public interface OnPostListener {
    void onPost();
    void onDelete(PostContent postContent);
    void onModify();
}
