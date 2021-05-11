package gachon.termproject.joker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gachon.termproject.joker.R;
import gachon.termproject.joker.container.CommentMyInfoContent;

public class CommentMyInfo extends Fragment {
    private View view;
    private RecyclerView contents;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CommentMyInfoContent> dataSet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup)inflater.inflate(R.layout.comment_myinfo, container, false);
        contents = view.findViewById(R.id.content_comment_myinfo);
        contents.setHasFixedSize(true);
        contents.setLayoutManager(new GridLayoutManager(getContext(), 1));
        dataSet = new ArrayList<>();
        mAdapter = new MyInfoCommentAdapter(dataSet);
        contents.setAdapter(mAdapter);

        dataSet.add(new CommentMyInfoContent("댓글1", "제목1"));
        dataSet.add(new CommentMyInfoContent("댓글2", "제목2"));
        dataSet.add(new CommentMyInfoContent("댓글3", "제목3"));
        dataSet.add(new CommentMyInfoContent("댓글4", "제목4"));
        dataSet.add(new CommentMyInfoContent("댓글5", "제목5"));
        dataSet.add(new CommentMyInfoContent("댓글6", "제목6"));
        dataSet.add(new CommentMyInfoContent("댓글7", "제목7"));
        dataSet.add(new CommentMyInfoContent("댓글8", "제목8"));
        dataSet.add(new CommentMyInfoContent("댓글9", "제목9"));
        dataSet.add(new CommentMyInfoContent("댓글10", "제목10"));

        return view;
    }
}
