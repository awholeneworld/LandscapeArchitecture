package gachon.termproject.joker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import gachon.termproject.joker.adapter.PostAdapter;
import gachon.termproject.joker.Content.PostContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.adapter.PostAlbumAdapter;

public class CommunityTipFragment extends Fragment {
    private View view;
    private SwipeRefreshLayout refresher;
    private static RecyclerView contents;
    private ArrayList<PostContent> postContentList;
    private PostContent postContent;
    private static PostAdapter postAdapter;
    private static PostAlbumAdapter postAlbumAdapter;
    private static boolean clicked = false;
    public static DatabaseReference databaseReference;
    public static ValueEventListener postsListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_community, container, false);

        // 레이아웃 가져오기
        contents = view.findViewById(R.id.communityContent);
        refresher = view.findViewById(R.id.refresh_layout);

        // 게시판 글 목록 내용 넣어줄 어레이 리스트와 어댑터 지정
        postContentList = new ArrayList<>();
        postAdapter = new PostAdapter(getActivity(), postContentList);
        postAlbumAdapter = new PostAlbumAdapter(getActivity(), postContentList);

        // 레이아웃 설정
        contents.setLayoutManager(new LinearLayoutManager(getActivity()));
        contents.setHasFixedSize(true);
        contents.setAdapter(postAdapter);

        // 리스너 설정
        postsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postContentList.clear();
                contents.setAdapter(postAdapter);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    postContent = snapshot.getValue(PostContent.class);
                    postContent.setUserId(snapshot.child("userId").getValue().toString());
                    postContentList.add(0, postContent);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        // DB에 리스너 설정
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts/tip");
        databaseReference.addListenerForSingleValueEvent(postsListener);

        // 새로고침 리스너 설정
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                databaseReference.addListenerForSingleValueEvent(postsListener);
                refresher.setRefreshing(false);
            }
        });

        return view;
    }

    // 앨범형으로 보기 눌렀을 때 실행되는 함수
    public static void changeMode() {
        if (!clicked) {
            contents.setLayoutManager(new GridLayoutManager(contents.getContext(), 3));
            contents.setAdapter(postAlbumAdapter);
            clicked = true;
        } else {
            contents.setLayoutManager(new LinearLayoutManager(contents.getContext()));
            contents.setAdapter(postAdapter);
            clicked = false;
        }
    }
}
