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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import gachon.termproject.joker.R;
import gachon.termproject.joker.adapter.ExpertListAdapter;
import gachon.termproject.joker.container.ExpertListContent;

public class ExpertList extends Fragment {
    private View view;
    private RecyclerView content;
    private SwipeRefreshLayout refresher;
    private FirebaseFirestore fStore;
    private CollectionReference collectionReference;
    private ExpertListAdapter expertListAdapter;
    private ArrayList<ExpertListContent> expertList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_expert_list_matching, container, false);

        content = view.findViewById(R.id.content);
        refresher = view.findViewById(R.id.refresh_layout);

        expertList = new ArrayList<>();
        expertListAdapter = new ExpertListAdapter(getActivity(), expertList);

        content.setLayoutManager(new LinearLayoutManager(getActivity()));
        content.setHasFixedSize(true);
        content.setAdapter(expertListAdapter);

        fStore = FirebaseFirestore.getInstance();
        collectionReference = fStore.collection("users");
        collectionReference.limit(50).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> list = querySnapshot.getDocuments();
                    for (int i = 0; i < list.size(); i++) {
                        DocumentSnapshot snapshot = list.get(i);
                        Boolean isPublic = snapshot.getBoolean("isPublic");
                        if (!isPublic) {
                            String nickname = snapshot.getString("nickname");
                            String profileImg = snapshot.getString("profileUrl");
                            expertList.add(new ExpertListContent(nickname, profileImg));
                        }
                    }
                    expertListAdapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}
