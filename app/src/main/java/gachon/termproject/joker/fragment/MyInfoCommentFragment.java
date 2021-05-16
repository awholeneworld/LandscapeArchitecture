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

import gachon.termproject.joker.R;


public class MyInfoCommentFragment extends Fragment {
    private View view;
    private RecyclerView contents;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup)inflater.inflate(R.layout.myinfo_comment, container, false);
        contents = view.findViewById(R.id.content_comment_myinfo);
        contents.setHasFixedSize(true);
        contents.setLayoutManager(new GridLayoutManager(getContext(), 1));

        return view;
    }
}
