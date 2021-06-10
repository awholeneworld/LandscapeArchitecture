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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import gachon.termproject.joker.Content.MatchingPostContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.adapter.MatchingPostAdapter;

public class MatchingExpertTabProgressFragment extends Fragment { //매칭중
    private View view;
    private SwipeRefreshLayout refresher;
    private RecyclerView contents;
    public static DatabaseReference databaseReference;
    public static ValueEventListener postEventListener;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    ArrayList<MatchingPostContent> postContentList;
    MatchingPostContent postContent;
    MatchingPostAdapter matchingpostAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_matching, container, false);

        contents = view.findViewById(R.id.matchingContent);
        refresher = view.findViewById(R.id.refresh_layout);

        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Matching/userRequests");

        postContentList = new ArrayList<>();
        matchingpostAdapter = new MatchingPostAdapter(getActivity(), postContentList, "awaiting");

        contents.setLayoutManager(new LinearLayoutManager(getActivity()));
        contents.setHasFixedSize(true);
        contents.setAdapter(matchingpostAdapter);

        postEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postContentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    postContent = snapshot.getValue(MatchingPostContent.class);
                    postContent.setUserId(snapshot.child("userId").getValue().toString());
                    if (!postContent.getIsMatched()){
                        //아직 게시글은 매칭이 안되었는데

                        //매칭 대기 목록에 앗 내가 잇네? => 그럼 매칭중 탭에
                        if(snapshot.child("requests/" + UserInfo.getUserId()).exists()){
                            postContentList.add(0, postContent);
                        }
                    }
                }
                matchingpostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addListenerForSingleValueEvent(postEventListener);

        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                databaseReference.addListenerForSingleValueEvent(postEventListener);
                refresher.setRefreshing(false);
            }
        });

        return view;
    }
}
