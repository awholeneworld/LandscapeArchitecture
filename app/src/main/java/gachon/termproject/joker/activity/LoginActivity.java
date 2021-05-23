package gachon.termproject.joker.activity;

import android.content.Intent;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;

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
        button_login.setOnClickListener(new View.OnClickListener() { // login 버튼 눌렀을때
            @Override
            public void onClick(View v) {
                button_login.setEnabled(false);

                //editText에서 아이디 비번 받아오기
                String ID = id.getText().toString().trim();
                String PW = pw.getText().toString().trim();

                // 아이디 비번 맞는지 확인하는 절차 코드
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
                    Toast.makeText(getApplicationContext(), "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    button_login.setEnabled(true);
                } else if (PW.length() == 0) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    button_login.setEnabled(true);
                } else {
                    Toast.makeText(getApplicationContext(), "이메일 또는 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    button_login.setEnabled(true);
                }
            }
        });


        //비밀번호 까묵엇을때
        forgetPW.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                forgetPW.setEnabled(false);
                Intent intent = new Intent(getApplicationContext(), FindPasswordActivity.class);
                startActivity(intent);
            }
        });


        //대망의 회원가입!!!
        toSignup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toSignup.setEnabled(false);
                startActivity(new Intent(getApplicationContext(), Signup00Activity.class));//액티비티 이동
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        forgetPW.setEnabled(true);
        toSignup.setEnabled(true);
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
                                    DataSnapshot snapshot = task.getResult();
                                    if (!snapshot.exists() || !snapshot.child("Posts").exists()) {
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
}