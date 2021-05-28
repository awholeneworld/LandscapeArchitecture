package gachon.termproject.joker.fragment;

import android.content.Intent;
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
import gachon.termproject.joker.Content.RequestFromExpertContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.adapter.MatchingPostAdapter;

import static android.app.Activity.RESULT_OK;

public class MatchingExpertTabCompleteFragment extends Fragment { //매칭완료
    private View view;
    private SwipeRefreshLayout refresher;
    private RecyclerView contents;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<MatchingPostContent> postContentList;
    MatchingPostContent postContent;
    MatchingPostAdapter matchingpostAdapter;
    ValueEventListener postEventListener;
    Boolean topScrolled;
    Boolean doUpdate;

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
        matchingpostAdapter = new MatchingPostAdapter(getActivity(), postContentList, "complete");
        // postAdapter.setOnPostListener(onPostListener);

        contents.setLayoutManager(new LinearLayoutManager(getActivity()));
        contents.setHasFixedSize(true);
        contents.setAdapter(matchingpostAdapter);

        postEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postContentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    postContent = snapshot.getValue(MatchingPostContent.class);

                    //글 상태가 매칭 완료인뎅
                    if (postContent.getIsMatched()){
                        //매칭 대기 목록에 앗 내가 잇네?
                        if(snapshot.child("requests/" + UserInfo.userId).exists()){
                            //그럼 내가 match = true인지 확인하기
                            RequestFromExpertContent a = snapshot.child("requests/" + UserInfo.userId).getValue(RequestFromExpertContent.class);
                            if(a.getIsMatched()){
                                postContentList.add(0, postContent);
                            }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                databaseReference.addListenerForSingleValueEvent(postEventListener);
            }
        }
    }

}
