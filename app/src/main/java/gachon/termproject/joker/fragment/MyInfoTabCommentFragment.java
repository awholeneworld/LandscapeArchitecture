package gachon.termproject.joker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import gachon.termproject.joker.Content.PostContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.activity.MainActivity;


public class MyInfoTabCommentFragment extends Fragment {
    private View view;
    private RecyclerView contents;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myinfo_comment, container, false);
        contents = view.findViewById(R.id.content_comment_myinfo);
        contents.setHasFixedSize(true);
        contents.setLayoutManager(new GridLayoutManager(getContext(), 1));

//        postsRef.getRef().addChildEventListener(new ChildEventListener() { // Posts DB에 접근해서 카테고리 하나씩 가져오기
//            @Override
//            public void onChildAdded(@NonNull @NotNull DataSnapshot categorySnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
//                categorySnapshot.getRef().addChildEventListener(new ChildEventListener() { // 카테고리에 있는 글 하나 하나씩 가져오기
//                    @Override
//                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
//                        if (!snapshot.hasChild("comments")) { // 글에 댓글이 없다면
//                            failCount++;
//                            if (failCount + successCount == categorySnapshot.getChildrenCount()) {
//                                finishCount++;
//                                if (finishCount == categoryNum) {
//                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                    intent.putStringArrayListExtra("userCommentsIdList", userCommentsIdList);
//                                    intent.putParcelableArrayListExtra("postsOfCommentsList", (ArrayList<? extends Parcelable>) postsOfCommentsList);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            }
//                        } else {
//                            snapshot.child("comments").getRef().orderByChild("userId").equalTo(UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                                @Override
//                                public void onSuccess(DataSnapshot dataSnapshot) {
//                                    if (!dataSnapshot.exists()) { // 현재 게시글에 내가 쓴 댓글이 없으면
//                                        failCount++;
//                                    } else { // 내가 단 댓글이 있으면
//                                        for (DataSnapshot snapshot3 : dataSnapshot.getChildren()) { // 정보 담기
//                                            userCommentsIdList.add(0, snapshot3.getKey());
//                                            postsOfCommentsList.add(0, snapshot.getValue(PostContent.class));
//                                        }
//                                        successCount++;
//                                    }
//
//                                    if (failCount + successCount == categorySnapshot.getChildrenCount()) {
//                                        finishCount++;
//                                        if (finishCount == categoryNum) {
//                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                            intent.putStringArrayListExtra("userCommentsIdList", userCommentsIdList);
//                                            intent.putParcelableArrayListExtra("postsOfCommentsList", (ArrayList<? extends Parcelable>) postsOfCommentsList);
//                                            startActivity(intent);
//                                            finish();
//                                        }
//                                    }
//                                }
//                            });
//                        }
//                        // 나중에 포스트는 시간 순으로 정리해주기
//                    }
//
//                    @Override
//                    public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });

        return view;
    }
}
