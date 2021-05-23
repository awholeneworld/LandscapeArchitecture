package gachon.termproject.joker.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
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
    private FirebaseAuth fAuth;
    private DocumentReference documentReference;
    private ArrayList<String> userPostsIdList = new ArrayList<>();
    private int finishCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_page);

        fAuth = FirebaseAuth.getInstance();

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

                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

                        ChildEventListener childEventListener = new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                if (snapshot.getKey().equals("Posts")) {
                                    DatabaseReference postsDbRef = dbRef.child("Posts");
                                    postsDbRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                        @Override
                                        public void onSuccess(DataSnapshot dataSnapshot) {
                                            final long categoryNum = dataSnapshot.getChildrenCount();
                                            postsDbRef.addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                                    snapshot.getRef().orderByChild("userId").equalTo(UserInfo.userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                                        @Override
                                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                            for (DataSnapshot item : snapshot.getChildren()) {
                                                                PostContent myInfoPostContent = item.getValue(PostContent.class);
                                                                userPostsIdList.add(0, myInfoPostContent.getPostId());
                                                            }

                                                            finishCount++;
                                                            if (finishCount == categoryNum) {
                                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                                intent.putStringArrayListExtra("userPostsIdList", userPostsIdList);
                                                                startActivity(intent);
                                                                Toast.makeText(getApplicationContext(), "로그인 성공!!", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }
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
                        };


                        dbRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().exists()) {
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        Toast.makeText(getApplicationContext(), "로그인 성공!!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else
                                        dbRef.addChildEventListener(childEventListener);
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