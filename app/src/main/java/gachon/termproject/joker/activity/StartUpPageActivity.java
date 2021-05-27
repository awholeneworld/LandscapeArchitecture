package gachon.termproject.joker.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.Content.PostContent;

public class StartUpPageActivity extends AppCompatActivity {
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private DocumentReference documentReference;
    private ArrayList<String> userPostsIdList = new ArrayList<>();
    private ArrayList<PostContent> userPostsList = new ArrayList<>();
    private ArrayList<String> userCommentsIdList = new ArrayList<>();
    private ArrayList<PostContent> postsOfCommentsList = new ArrayList<>();
    private int successCount = 0;
    private int failCount = 0;
    private int successCountFree = 0;
    private int failCountFree = 0;
    private int successCountReview = 0;
    private int failCountReview = 0;
    private int successCountTip = 0;
    private int failCountTip = 0;
    private int finishCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_page);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // 이미 로그인한 경우 로그인 상태 유지
        if (fAuth.getCurrentUser() != null)
            logIn();
        else {
            StartUpPageThread thread = new StartUpPageThread(new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 1) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                }
            });
            thread.start();
        }
    }

    // 나중에 쓸 일 많은 유저 고유 아이디, 닉네임, 프로필 사진 Url 정보 등 미리 저장 후 로그인
    public void logIn() {
        UserInfo.userId = fAuth.getCurrentUser().getUid();
        documentReference = FirebaseFirestore.getInstance().collection("users").document(UserInfo.userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // 사용자 닉네임, 프로필 사진 Url 등 가져오기
                        UserInfo.email = document.getString("ID");
                        UserInfo.nickname = document.getString("nickname");
                        UserInfo.profileImg = document.getString("profileImg");
                        UserInfo.introduction = document.getString("introduction");
                        UserInfo.isPublic = document.getBoolean("isPublic");
                        UserInfo.location = (ArrayList<String>) document.get("location");


                        if (!UserInfo.isPublic) {
                            UserInfo.portfolioImg = document.getString("portfolioImg");
                            UserInfo.portfolioWeb = document.getString("portfolioWeb");
                        }

                        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Posts");
                        postsRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() { // DB에 Posts가 있는지 확인하기 위해 가져와본다 
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) { // Posts가 없으면 바로 로그인
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                } else { // Posts가 있다면 자기가 작성한 게시글이랑 댓글 정보 가져오는 작업 수행
                                    final long categoryNum = dataSnapshot.getChildrenCount(); // 현재 Posts에 있는 category 수

                                    dataSnapshot.getRef().addChildEventListener(new ChildEventListener() { // Posts에 있는 카테고리의 데이터를 카테고리 하나씩 가져올거임
                                        @Override
                                        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                            snapshot.getRef().orderByChild("userId").equalTo(UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                @Override
                                                public void onSuccess(DataSnapshot dataSnapshot) {
                                                    if (!dataSnapshot.exists()) { // 내가 쓴 글이 존재하지 않는다면
                                                        failCount++;
                                                        if (failCount + successCount == categoryNum) { // 내가 쓴 글이 아예 존재하지 않는다면, 내가 쓴 댓글 확인하러 슝슝
                                                            failCount = 0;
                                                            successCount = 0;
                                                            postsRef.getRef().addChildEventListener(new ChildEventListener() { // Posts DB에 접근해서 카테고리 하나씩 가져오기
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
                                                                                    if (finishCount == categoryNum) {
                                                                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                                                        intent.putStringArrayListExtra("userPostsIdList", userPostsIdList);
                                                                                        intent.putStringArrayListExtra("userCommentsIdList", userCommentsIdList);
                                                                                        intent.putExtra("userPostsList", userPostsList);
                                                                                        intent.putExtra("postsOfCommentsList", postsOfCommentsList);
                                                                                        startActivity(intent);
                                                                                        finish();
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                snapshot.child("comments").getRef().orderByChild("userId").equalTo(UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                    @Override
                                                                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                                                                        if (!dataSnapshot.exists()) { // 현재 게시글에 내가 쓴 댓글이 없으면
                                                                                            if (categorySnapshot.getKey().equals("free")) failCountFree++;
                                                                                            else if (categorySnapshot.getKey().equals("review")) failCountReview++;
                                                                                            else if (categorySnapshot.getKey().equals("tip")) failCountTip++;
                                                                                        } else { // 내가 단 댓글이 있으면
                                                                                            for (DataSnapshot snapshot2 : dataSnapshot.getChildren()) { // 정보 담기
                                                                                                if (snapshot2.child("userId").getValue().equals(UserInfo.userId)) {
                                                                                                    userCommentsIdList.add(0, snapshot2.getKey());
                                                                                                    postsOfCommentsList.add(0, snapshot.getValue(PostContent.class));
                                                                                                }
                                                                                            }
                                                                                            if (categorySnapshot.getKey().equals("free")) successCountFree++;
                                                                                            else if (categorySnapshot.getKey().equals("review")) successCountReview++;
                                                                                            else if (categorySnapshot.getKey().equals("tip")) successCountTip++;
                                                                                        }

                                                                                        if ((categorySnapshot.getKey().equals("free") && failCountFree + successCountFree == categorySnapshot.getChildrenCount()) || (categorySnapshot.getKey().equals("review") && failCountReview + successCountReview == categorySnapshot.getChildrenCount()) || (categorySnapshot.getKey().equals("tip") && failCountTip + successCountTip == categorySnapshot.getChildrenCount())) {
                                                                                            finishCount++;
                                                                                            if (finishCount == categoryNum) {
                                                                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                                                                intent.putStringArrayListExtra("userPostsIdList", userPostsIdList);
                                                                                                intent.putStringArrayListExtra("userCommentsIdList", userCommentsIdList);
                                                                                                intent.putExtra("userPostsList", userPostsList);
                                                                                                intent.putExtra("postsOfCommentsList", postsOfCommentsList);
                                                                                                startActivity(intent);
                                                                                                finish();
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
                                                    } else { // 내가 쓴 글이 있다면
                                                        successCount++; // 성공 카운트 올리기
                                                        dataSnapshot.getRef().addValueEventListener(new ValueEventListener() { // 정보 저장
                                                            @Override
                                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                for (DataSnapshot shot : snapshot.getChildren()) {
                                                                    if (shot.child("userId").getValue().equals(UserInfo.userId)) {
                                                                        PostContent content = shot.getValue(PostContent.class);
                                                                        userPostsIdList.add(0, content.getPostId());
                                                                        userPostsList.add(0, content);
                                                                    }
                                                                }
                                                                if (failCount + successCount == categoryNum) { // Posts에 있는 카테고리 수만큼 내가 단 댓글 정보 가져올거임임
                                                                    failCount = 0;
                                                                    successCount = 0;
                                                                    postsRef.getRef().addChildEventListener(new ChildEventListener() { // Posts DB에 접근해서 카테고리 하나씩 가져오기
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
                                                                                            if (finishCount == categoryNum) {
                                                                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                                                                intent.putStringArrayListExtra("userPostsIdList", userPostsIdList);
                                                                                                intent.putStringArrayListExtra("userCommentsIdList", userCommentsIdList);
                                                                                                intent.putExtra("userPostsList", userPostsList);
                                                                                                intent.putExtra("postsOfCommentsList", postsOfCommentsList);
                                                                                                startActivity(intent);
                                                                                                finish();
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        snapshot.child("comments").getRef().orderByChild("userId").equalTo(UserInfo.userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                            @Override
                                                                                            public void onSuccess(DataSnapshot dataSnapshot) {
                                                                                                if (!dataSnapshot.exists()) { // 현재 게시글에 내가 쓴 댓글이 없으면
                                                                                                    if (categorySnapshot.getKey().equals("free")) failCountFree++;
                                                                                                    else if (categorySnapshot.getKey().equals("review")) failCountReview++;
                                                                                                    else if (categorySnapshot.getKey().equals("tip")) failCountTip++;
                                                                                                } else { // 내가 단 댓글이 있으면
                                                                                                    for (DataSnapshot snapshot2 : dataSnapshot.getChildren()) { // 정보 담기
                                                                                                        if (snapshot2.child("userId").getValue().equals(UserInfo.userId)) {
                                                                                                            userCommentsIdList.add(0, snapshot2.getKey());
                                                                                                            postsOfCommentsList.add(0, snapshot.getValue(PostContent.class));
                                                                                                        }
                                                                                                    }
                                                                                                    if (categorySnapshot.getKey().equals("free")) successCountFree++;
                                                                                                    else if (categorySnapshot.getKey().equals("review")) successCountReview++;
                                                                                                    else if (categorySnapshot.getKey().equals("tip")) successCountTip++;
                                                                                                }
                                                                                                
                                                                                                if ((categorySnapshot.getKey().equals("free") && failCountFree + successCountFree == categorySnapshot.getChildrenCount()) || (categorySnapshot.getKey().equals("review") && failCountReview + successCountReview == categorySnapshot.getChildrenCount()) || (categorySnapshot.getKey().equals("tip") && failCountTip + successCountTip == categorySnapshot.getChildrenCount())) {
                                                                                                    finishCount++;
                                                                                                    if (finishCount == categoryNum) {
                                                                                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                                                                        intent.putStringArrayListExtra("userPostsIdList", userPostsIdList);
                                                                                                        intent.putStringArrayListExtra("userCommentsIdList", userCommentsIdList);
                                                                                                        intent.putParcelableArrayListExtra("userPostsList", userPostsList);
                                                                                                        intent.putParcelableArrayListExtra("postsOfCommentsList", postsOfCommentsList);
                                                                                                        startActivity(intent);
                                                                                                        finish();
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                    // 나중에 포스트는 시간 순으로 정리해주기
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

                                                            @Override
                                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                            }
                                                        });
                                                    }
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
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "유저 정보 가져오기 실패", Toast.LENGTH_LONG).show();
            }
        });
    }

    public class StartUpPageThread extends Thread {
        private Handler handler;

        public StartUpPageThread(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                handler.sendEmptyMessage(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}