package gachon.termproject.joker.fragment;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import gachon.termproject.joker.OnPostListener;
import gachon.termproject.joker.R;
import gachon.termproject.joker.adapter.HomePostAdapter;
import gachon.termproject.joker.Content.PostContent;

import static android.app.Activity.RESULT_OK;

public class MainHomeFragment extends Fragment {
    private View view;
    private RelativeLayout expert;
    private TextView today_expert_name;

    private RecyclerView freecmulist;
    private RecyclerView tiplist;
    private RecyclerView reviewlist;

    private FirebaseUser user;

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
    HomePostAdapter postAdapter_review;

    ValueEventListener postsListener_free;
    ValueEventListener postsListener_tip;
    ValueEventListener postsListener_review;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        expert = view.findViewById(R.id.home_today_expert);
        today_expert_name = view.findViewById(R.id.home_expert_name);

        freecmulist = view.findViewById(R.id.home_free_community_list);
        tiplist = view.findViewById(R.id.home_tip_list);
        reviewlist = view.findViewById(R.id.home_expert_review_list);

        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference_free = firebaseDatabase.getReference("Posts/free");
        databaseReference_tip = firebaseDatabase.getReference("Posts/tip");
        databaseReference_review = firebaseDatabase.getReference("Posts/review");

        postContentList_free = new ArrayList<>();
        postContentList_tip = new ArrayList<>();
        postContentList_review = new ArrayList<>();

        postAdapter_free = new HomePostAdapter(getActivity(), postContentList_free);
        postAdapter_tip = new HomePostAdapter(getActivity(), postContentList_tip);
        postAdapter_review = new HomePostAdapter(getActivity(), postContentList_review);

        // postAdapter.setOnPostListener(onPostListener);

        freecmulist.setLayoutManager(new LinearLayoutManager(getActivity()));
        freecmulist.setHasFixedSize(true);
        freecmulist.setAdapter(postAdapter_free);

        tiplist.setLayoutManager(new LinearLayoutManager(getActivity()));
        tiplist.setHasFixedSize(true);
        tiplist.setAdapter(postAdapter_tip);

        reviewlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        reviewlist.setHasFixedSize(true);
        reviewlist.setAdapter(postAdapter_review);


        postsListener_free = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postContentList_free.clear();
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    i++;
                    postContent_free = snapshot.getValue(PostContent.class);
                    postContentList_free.add(0, postContent_free);
                    if(i > 5) break;
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
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    i++;
                    postContent_tip = snapshot.getValue(PostContent.class);
                    postContentList_tip.add(0, postContent_tip);
                    if(i > 5) break;
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
                int i=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    i++;
                    postContent_review = snapshot.getValue(PostContent.class);
                    postContentList_review.add(0, postContent_review);
                    if(i > 5) break;
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

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK)
                databaseReference_free.addListenerForSingleValueEvent(postsListener_free);
                databaseReference_tip.addListenerForSingleValueEvent(postsListener_tip);
                databaseReference_review.addListenerForSingleValueEvent(postsListener_review);
        }
    }

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onPost() {

        }

        @Override
        public void onDelete(PostContent postContent) {
            postContentList_free.remove(postContent);
            postAdapter_free.notifyDataSetChanged();

            postContentList_tip.remove(postContent);
            postAdapter_tip.notifyDataSetChanged();

            postContentList_review.remove(postContent);
            postAdapter_review.notifyDataSetChanged();
        }

        @Override
        public void onModify() {
        }
    };

}
