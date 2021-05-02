package gachon.termproject.joker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import gachon.termproject.joker.ExpertListContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.adapter.ExpertListAdapter;

public class ExpertList extends Fragment {
    private View view;
    private RecyclerView expertlists;
    private SwipeRefreshLayout refresher;
    private FirebaseUser user;
    private FirebaseFirestore fStore;
    private ExpertListAdapter expertlistAdapter;

    ArrayList<ExpertListContent> expertList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.expertlist_matching, container, false);
        expertlists = view.findViewById(R.id.expertlist_matching);
        refresher = view.findViewById(R.id.refresh_layout);

        user = FirebaseAuth.getInstance().getCurrentUser();

        expertList = MatchingFrame.expertList;

        expertlistAdapter = new ExpertListAdapter(getActivity(), expertList);

        expertlists.setLayoutManager(new LinearLayoutManager(getActivity()));
        expertlists.setHasFixedSize(true);
        expertlists.setAdapter(expertlistAdapter);


        return view;
    }
}
