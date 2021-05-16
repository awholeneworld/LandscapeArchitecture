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
import gachon.termproject.joker.adapter.MyInfoPostAdapter;

public class MyInfoPostFragment extends Fragment {
    private View view;
    private RecyclerView contents;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myinfo_post, container, false);

        int numberOfColumns = 3;
        contents = view.findViewById(R.id.content_post_myinfo);

        contents.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        contents.setHasFixedSize(true);
        contents.setAdapter(new MyInfoPostAdapter(getActivity()));

        return view;
    }
}