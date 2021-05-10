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
import gachon.termproject.joker.adapter.MyInfoPostAdapter;
import gachon.termproject.joker.adapter.PostAlbumAdapter;
import gachon.termproject.joker.container.PostAlbumContent;
import gachon.termproject.joker.container.PostMyInfoContent;

public class CommunityAlbumStyle extends Fragment {

    private View view;
    private RecyclerView contents;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<PostAlbumContent> dataSet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup)inflater.inflate(R.layout.post_myinfo, container, false);
        contents = view.findViewById(R.id.content_post_myinfo);
        int numberOfColumns = 3;
        contents.setHasFixedSize(true);
        contents.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        dataSet = new ArrayList<>();
        mAdapter = new PostAlbumAdapter(dataSet);
        contents.setAdapter(mAdapter);

        dataSet.add(new PostAlbumContent(R.drawable.ic_launcher_background));
        dataSet.add(new PostAlbumContent(R.mipmap.ic_launcher));
        dataSet.add(new PostAlbumContent(R.mipmap.ic_launcher));
        dataSet.add(new PostAlbumContent(R.mipmap.ic_launcher));
        dataSet.add(new PostAlbumContent(R.mipmap.ic_launcher));

        return view;
    }
}