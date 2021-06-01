package gachon.termproject.joker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import gachon.termproject.joker.R;
import gachon.termproject.joker.adapter.HomePostAdapter;
import gachon.termproject.joker.Content.PostContent;
import gachon.termproject.joker.adapter.HomePostReviewAdapter;

public class HomeFragment extends Fragment {
    private View view;
    private SwipeRefreshLayout refresher;
    private RelativeLayout expert;
    private TextView today_expert_name;
    private RecyclerView freecmulist;
    private RecyclerView tiplist;
    private RecyclerView reviewlist;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference_free;
    DatabaseReference databaseReference_tip;
    DatabaseReference databaseReference_review;

    ArrayList<PostContent> postContentList_free;
    ArrayList<PostContent> postContentList_tip;
    ArrayList<PostContent> postContentList_review;

    PostContent postContent_free;
    PostContent postContent_tip;
    PostContent postContent_review;

    HomePostAdapter postAdapter_free;
    HomePostAdapter postAdapter_tip;
    HomePostReviewAdapter postAdapter_review;

    ValueEventListener postsListener_free;
    ValueEventListener postsListener_tip;
    ValueEventListener postsListener_review;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        refresher = view.findViewById(R.id.refresh_layout);
        expert = view.findViewById(R.id.home_today_expert);
        today_expert_name = view.findViewById(R.id.home_expert_name);

        freecmulist = view.findViewById(R.id.home_free_community_list);
        tiplist = view.findViewById(R.id.home_tip_list);
        reviewlist = view.findViewById(R.id.home_expert_review_list);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference_free = firebaseDatabase.getReference("Posts/free");
        databaseReference_tip = firebaseDatabase.getReference("Posts/tip");
        databaseReference_review = firebaseDatabase.getReference("Posts/review");

        postContentList_free = new ArrayList<>();
        postContentList_tip = new ArrayList<>();
        postContentList_review = new ArrayList<>();

        postAdapter_free = new HomePostAdapter(getActivity(), postContentList_free);
        postAdapter_tip = new HomePostAdapter(getActivity(), postContentList_tip);
        postAdapter_review = new HomePostReviewAdapter(getActivity(), postContentList_review);

        freecmulist.setLayoutManager(new LinearLayoutManager(getActivity()));
        freecmulist.setHasFixedSize(true);
        freecmulist.setAdapter(postAdapter_free);

        tiplist.setLayoutManager(new LinearLayoutManager(getActivity()));
        tiplist.setHasFixedSize(true);
        tiplist.setAdapter(postAdapter_tip);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity());
        horizontalLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        reviewlist.setLayoutManager(horizontalLayoutManager);


        reviewlist.setLayoutManager(new LinearLayoutManager( getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        reviewlist.setHasFixedSize(true);
        reviewlist.setAdapter(postAdapter_review);


        postsListener_free = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postContentList_free.clear();
                int i = 0;
                long length = dataSnapshot.getChildrenCount();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (length > 6 && i >= length - 6) {
                        postContent_free = snapshot.getValue(PostContent.class);
                        postContentList_free.add(0, postContent_free);
                    } else if (length <= 6) {
                        postContent_free = snapshot.getValue(PostContent.class);
                        postContentList_free.add(0, postContent_free);
                    }
                    i++;
                }
                postAdapter_free.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        postsListener_tip = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postContentList_tip.clear();
                int i = 0;
                long length = dataSnapshot.getChildrenCount();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (length > 6 && i >= length - 6) {
                        postContent_tip = snapshot.getValue(PostContent.class);
                        postContentList_tip.add(0, postContent_tip);
                    } else if (length <= 6) {
                        postContent_tip = snapshot.getValue(PostContent.class);
                        postContentList_tip.add(0, postContent_tip);
                    }
                    i++;
                }
                postAdapter_tip.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        postsListener_review = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postContentList_review.clear();
                int i = 0;
                long length = dataSnapshot.getChildrenCount();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (length > 6 && i >= length - 6) {
                        postContent_review = snapshot.getValue(PostContent.class);
                        postContentList_review.add(0, postContent_review);
                    } else if (length <= 6) {
                        postContent_review = snapshot.getValue(PostContent.class);
                        postContentList_review.add(0, postContent_review);
                    }
                    i++;
                }
                postAdapter_review.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference_free.addListenerForSingleValueEvent(postsListener_free);
        databaseReference_tip.addListenerForSingleValueEvent(postsListener_tip);
        databaseReference_review.addListenerForSingleValueEvent(postsListener_review);

        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                databaseReference_free.addListenerForSingleValueEvent(postsListener_free);
                databaseReference_tip.addListenerForSingleValueEvent(postsListener_tip);
                databaseReference_review.addListenerForSingleValueEvent(postsListener_review);
                refresher.setRefreshing(false);
            }
        });

        return view;
    }
}
