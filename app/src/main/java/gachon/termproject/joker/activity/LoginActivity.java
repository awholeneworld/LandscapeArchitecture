package gachon.termproject.joker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.Content.PostContent;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private DocumentReference documentReference;
    private TextView toSignup;
    private TextView forgetPW;
    private Button button_login;
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
        setContentView(R.layout.activity_login);

        EditText id = findViewById(R.id.login_editText_ID);
        EditText pw = findViewById(R.id.login_editText_PW);
        toSignup = findViewById(R.id.login_text06_signup);
        forgetPW = findViewById(R.id.login_text04_forgetPW);
        button_login = findViewById(R.id.login_button);
        button_login.setOnClickListener(new View.OnClickListener() { // login ?????? ????????????
            @Override
            public void onClick(View v) {
                button_login.setEnabled(false);

                //editText?????? ????????? ?????? ????????????
                String ID = id.getText().toString().trim();
                String PW = pw.getText().toString().trim();

                // ????????? ?????? ????????? ???????????? ?????? ??????
                if (ID.length() > 0 && PW.length() > 0) {
                    fAuth.signInWithEmailAndPassword(ID, PW)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        logIn();
                                    } else if (task.getException() != null) {
                                        Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        button_login.setEnabled(true);
                                    }
                                }
                            });
                } else if (ID.length() == 0) {
                    Toast.makeText(getApplicationContext(), "???????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                    button_login.setEnabled(true);
                } else if (PW.length() == 0) {
                    Toast.makeText(getApplicationContext(), "??????????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                    button_login.setEnabled(true);
                } else {
                    Toast.makeText(getApplicationContext(), "????????? ?????? ??????????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                    button_login.setEnabled(true);
                }
            }
        });


        //???????????? ???????????????
        forgetPW.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                forgetPW.setEnabled(false);
                Intent intent = new Intent(getApplicationContext(), FindPasswordActivity.class);
                startActivity(intent);
            }
        });


        //????????? ????????????!!!
        toSignup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toSignup.setEnabled(false);
                startActivity(new Intent(getApplicationContext(), Signup00Activity.class));//???????????? ??????
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        forgetPW.setEnabled(true);
        toSignup.setEnabled(true);
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
                                                                                                    int length = postsOfCommentsList.size();
                                                                                                    if (length == 0)
                                                                                                        postsOfCommentsList.add(0, snapshot.getValue(PostContent.class));
                                                                                                    else {
                                                                                                        for (int i = 0; i < length; i++) {
                                                                                                            if (postsOfCommentsList.get(i).getPostId().equals(snapshot.getKey()))
                                                                                                                break;
                                                                                                            else if (i == length - 1)
                                                                                                                postsOfCommentsList.add(0, snapshot.getValue(PostContent.class));
                                                                                                        }
                                                                                                    }
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
                                                                                                            int length = postsOfCommentsList.size();
                                                                                                            if (length == 0)
                                                                                                                postsOfCommentsList.add(0, snapshot.getValue(PostContent.class));
                                                                                                            else {
                                                                                                                for (int i = 0; i < length; i++) {
                                                                                                                    if (postsOfCommentsList.get(i).getPostId().equals(snapshot.getKey()))
                                                                                                                        break;
                                                                                                                    else if (i == length - 1)
                                                                                                                        postsOfCommentsList.add(0, snapshot.getValue(PostContent.class));
                                                                                                                }
                                                                                                            }
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