package gachon.termproject.joker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import gachon.termproject.joker.Content.PostContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;

public class Signup05Activity extends AppCompatActivity {
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
        setContentView(R.layout.signup05_finishpublic);

        //toolbar??? activity bar??? ??????!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //?????? ?????? ??????
        actionBar.setDisplayHomeAsUpEnabled(true); //?????? ?????????????

        Button nextButton = findViewById(R.id.signup05_button01);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ????????? ??? ??? ?????? ?????? ????????? ?????????
                logIn();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // ????????? ??? ??? ?????? ?????? ?????? ?????????, ?????????, ????????? ?????? Url ?????? ??? ?????? ?????? ??? ?????????
    public void logIn() {
        UserInfo.setUserId(fAuth.getCurrentUser().getUid());
        documentReference = FirebaseFirestore.getInstance().collection("users").document(UserInfo.getUserId());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // ????????? ?????????, ????????? ?????? Url ??? ????????????
                        UserInfo.setEmail(document.getString("ID"));
                        UserInfo.setNickname(document.getString("nickname"));
                        UserInfo.setProfileImg(document.getString("profileImg"));
                        UserInfo.setIntroduction(document.getString("introduction"));
                        UserInfo.setIsPublic(document.getBoolean("isPublic"));
                        UserInfo.setLocation((ArrayList<String>) document.get("location"));
                        UserInfo.setPushToken(document.getString("pushToken"));

                        if (!UserInfo.getIsPublic()) {
                            UserInfo.setPortfolioImg(document.getString("portfolioImg"));
                            UserInfo.setPortfolioWeb(document.getString("portfolioWeb"));
                        }

                        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Posts");
                        postsRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() { // DB??? Posts??? ????????? ???????????? ?????? ???????????????
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) { // Posts??? ????????? ?????? ?????????
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                } else { // Posts??? ????????? ????????? ????????? ??????????????? ?????? ?????? ???????????? ?????? ??????
                                    final long categoryNum = dataSnapshot.getChildrenCount(); // ?????? Posts??? ?????? category ???

                                    dataSnapshot.getRef().addChildEventListener(new ChildEventListener() { // Posts??? ?????? ??????????????? ???????????? ???????????? ????????? ???????????????
                                        @Override
                                        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                            snapshot.getRef().orderByChild("userId").equalTo(UserInfo.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                @Override
                                                public void onSuccess(DataSnapshot dataSnapshot) {
                                                    if (!dataSnapshot.exists()) { // ?????? ??? ?????? ???????????? ????????????
                                                        failCount++;
                                                        if (failCount + successCount == categoryNum) { // ?????? ??? ?????? ?????? ???????????? ????????????, ?????? ??? ?????? ???????????? ??????
                                                            failCount = 0;
                                                            successCount = 0;
                                                            postsRef.getRef().addChildEventListener(new ChildEventListener() { // Posts DB??? ???????????? ???????????? ????????? ????????????
                                                                @Override
                                                                public void onChildAdded(@NonNull @NotNull DataSnapshot categorySnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                    categorySnapshot.getRef().addChildEventListener(new ChildEventListener() { // ??????????????? ?????? ??? ?????? ????????? ????????????
                                                                        @Override
                                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                            if (!snapshot.hasChild("comments")) { // ?????? ????????? ?????????
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
                                                                                snapshot.child("comments").getRef().orderByChild("userId").equalTo(UserInfo.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                    @Override
                                                                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                                                                        if (!dataSnapshot.exists()) { // ?????? ???????????? ?????? ??? ????????? ?????????
                                                                                            if (categorySnapshot.getKey().equals("free")) failCountFree++;
                                                                                            else if (categorySnapshot.getKey().equals("review")) failCountReview++;
                                                                                            else if (categorySnapshot.getKey().equals("tip")) failCountTip++;
                                                                                        } else { // ?????? ??? ????????? ?????????
                                                                                            for (DataSnapshot snapshot2 : dataSnapshot.getChildren()) { // ?????? ??????
                                                                                                if (snapshot2.child("userId").getValue().equals(UserInfo.getUserId())) {
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
                                                    } else { // ?????? ??? ?????? ?????????
                                                        successCount++; // ?????? ????????? ?????????
                                                        dataSnapshot.getRef().addValueEventListener(new ValueEventListener() { // ?????? ??????
                                                            @Override
                                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                for (DataSnapshot shot : snapshot.getChildren()) {
                                                                    if (shot.child("userId").getValue().equals(UserInfo.getUserId())) {
                                                                        PostContent content = shot.getValue(PostContent.class);
                                                                        userPostsIdList.add(0, content.getPostId());
                                                                        userPostsList.add(0, content);
                                                                    }
                                                                }
                                                                if (failCount + successCount == categoryNum) { // Posts??? ?????? ???????????? ????????? ?????? ??? ?????? ?????? ??????????????????
                                                                    failCount = 0;
                                                                    successCount = 0;
                                                                    postsRef.getRef().addChildEventListener(new ChildEventListener() { // Posts DB??? ???????????? ???????????? ????????? ????????????
                                                                        @Override
                                                                        public void onChildAdded(@NonNull @NotNull DataSnapshot categorySnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                            categorySnapshot.getRef().addChildEventListener(new ChildEventListener() { // ??????????????? ?????? ??? ?????? ????????? ????????????
                                                                                @Override
                                                                                public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                                                    if (!snapshot.hasChild("comments")) { // ?????? ????????? ?????????
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
                                                                                        snapshot.child("comments").getRef().orderByChild("userId").equalTo(UserInfo.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                                                            @Override
                                                                                            public void onSuccess(DataSnapshot dataSnapshot) {
                                                                                                if (!dataSnapshot.exists()) { // ?????? ???????????? ?????? ??? ????????? ?????????
                                                                                                    if (categorySnapshot.getKey().equals("free")) failCountFree++;
                                                                                                    else if (categorySnapshot.getKey().equals("review")) failCountReview++;
                                                                                                    else if (categorySnapshot.getKey().equals("tip")) failCountTip++;
                                                                                                } else { // ?????? ??? ????????? ?????????
                                                                                                    for (DataSnapshot snapshot2 : dataSnapshot.getChildren()) { // ?????? ??????
                                                                                                        if (snapshot2.child("userId").getValue().equals(UserInfo.getUserId())) {
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
                                                                                    // ????????? ???????????? ?????? ????????? ???????????????
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
                Toast.makeText(getApplicationContext(), "?????? ?????? ???????????? ??????", Toast.LENGTH_LONG).show();
            }
        });
    }
}