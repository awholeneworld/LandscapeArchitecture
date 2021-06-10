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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import gachon.termproject.joker.Content.PostContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.activity.MainActivity;
import gachon.termproject.joker.adapter.MyInfoTabCommentAdapter;


public class MyInfoTabCommentFragment extends Fragment {
    private View view;
    private RecyclerView contents;
    public static MyInfoTabCommentAdapter adapter;
    public static DatabaseReference postsRef;
    public static OnSuccessListener onSuccessListener;
    private int successCountFree = 0;
    private int failCountFree = 0;
    private int successCountReview = 0;
    private int failCountReview = 0;
    private int successCountTip = 0;
    private int failCountTip = 0;
    private int finishCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myinfo_comment, container, false);

        contents = view.findViewById(R.id.content_comment_myinfo);
        adapter = new MyInfoTabCommentAdapter(getActivity(), MainActivity.postsOfCommentsList);

        contents.setLayoutManager(new GridLayoutManager(getContext(), 3));
        contents.setHasFixedSize(true);
        contents.setAdapter(adapter);

        postsRef = FirebaseDatabase.getInstance().getReference("Posts");

        onSuccessListener = new OnSuccessListener<DataSnapshot>() { // DB에 Posts가 있는지 확인하기 위해 가져와본다
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) { // Posts가 없으면 암것도 안함

                } else { // 있다면
                    MainActivity.postsOfCommentsList.clear();
                    final long categoryNum = dataSnapshot.getChildrenCount();

                    dataSnapshot.getRef().addChildEventListener(new ChildEventListener() { // Posts DB에 접근해서 카테고리 하나씩 가져오기
                        @Override
                        public void onChildAdded(@NonNull @NotNull DataSnapshot categorySnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                            categorySnapshot.getRef().addChildEventListener(new ChildEventListener() { // 카테고리에 있는 글 하나 하나씩 가져오기
                                @Override
                                public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                    if (!snapshot.hasChild("comments")) { // 글에 댓글이 없다면
                                        if (categorySnapshot.getKey().equals("free")) failCountFree++;
                                        else if (categorySnapshot.getKey().equals("review")) failCountReview++;
                                        else if (categorySnapshot.getKey().equals("tip")) failCountTip++;

                                        if ((categorySnapshot.getKey().equals("free") && failCountFree + successCountFree == categorySnapshot.getChildrenCount()) || (categorySnapshot.getKey().equals("review") && failCountReview + successCountReview == categorySnapshot.getChildrenCount()) || (categorySnapshot.getKey().equals("tip") && failCountTip + successCountTip == categorySnapshot.getChildrenCount())) {
                                            finishCount++;
                                            if (finishCount == categoryNum) { // 내가 단 댓글이 없다면 암것도 안함

                                            }
                                        }
                                    } else {
                                        snapshot.child("comments").getRef().orderByChild("userId").equalTo(UserInfo.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                            @Override
                                            public void onSuccess(DataSnapshot dataSnapshot) {
                                                if (!dataSnapshot.exists()) { // 현재 게시글에 내가 쓴 댓글이 없으면
                                                    if (categorySnapshot.getKey().equals("free")) failCountFree++;
                                                    else if (categorySnapshot.getKey().equals("review")) failCountReview++;
                                                    else if (categorySnapshot.getKey().equals("tip")) failCountTip++;
                                                } else { // 내가 단 댓글이 있으면
                                                    for (DataSnapshot snapshot2 : dataSnapshot.getChildren()) { // 정보 담기
                                                        if (snapshot2.child("userId").getValue().equals(UserInfo.getUserId())) {
                                                            MainActivity.postsOfCommentsList.add(0, snapshot.getValue(PostContent.class));
                                                        }
                                                    }
                                                    if (categorySnapshot.getKey().equals("free")) successCountFree++;
                                                    else if (categorySnapshot.getKey().equals("review")) successCountReview++;
                                                    else if (categorySnapshot.getKey().equals("tip")) successCountTip++;
                                                }

                                                if ((categorySnapshot.getKey().equals("free") && failCountFree + successCountFree == categorySnapshot.getChildrenCount()) || (categorySnapshot.getKey().equals("review") && failCountReview + successCountReview == categorySnapshot.getChildrenCount()) || (categorySnapshot.getKey().equals("tip") && failCountTip + successCountTip == categorySnapshot.getChildrenCount())) {
                                                    finishCount++;
                                                    if (finishCount == categoryNum) { // 검색 다 끝났으면 끝

                                                    }
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            }
        };

        return view;
    }
}
