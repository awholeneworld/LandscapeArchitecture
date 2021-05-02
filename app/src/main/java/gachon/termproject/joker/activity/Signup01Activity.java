package gachon.termproject.joker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import gachon.termproject.joker.R;

public class Signup01Activity extends AppCompatActivity {
    public static String identifier; // 회원가입을 위한 전역변수
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup01_email);

        fAuth = FirebaseAuth.getInstance();

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?

        Button nextButton = findViewById(R.id.signup01_button01);
        EditText email = findViewById(R.id.signup01_edittext01);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //editText에서 값을 받아와서 이메일을 처리한 다음에 넘어가야 함.
                String UserEmail = email.getText().toString().trim();

                //UserEmail에 이메일을 받아와서 여차저차함
                if (TextUtils.isEmpty(UserEmail))
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                // Email 중복여부 확인
                else
                    checkEmail(UserEmail);
            }
        });
    }

    // 이메일 중복여부 확인하는 함수 : 회원가입하려는 유저가 입력한 이메일에 비번 12로 로그인 실시하여 유도해냄
    private void checkEmail(String email) {
        fAuth.signInWithEmailAndPassword(email, "12")
                .addOnCompleteListener(Signup01Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.getException() instanceof FirebaseTooManyRequestsException) {
                            Toast.makeText(getApplicationContext(), "중복된 아이디입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                            switch (errorCode) {
                                case "ERROR_INVALID_EMAIL":
                                    Toast.makeText(getApplicationContext(), "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                                    break;

                                case "ERROR_WRONG_PASSWORD":
                                    Toast.makeText(getApplicationContext(), "중복된 아이디입니다.", Toast.LENGTH_SHORT).show();
                                    break;

                                case "ERROR_USER_NOT_FOUND":
                                    //비밀번호 페이지로 이동
                                    identifier = email; // ID 전역변수 설정
                                    Intent intent = new Intent(getApplicationContext(), Signup02Activity.class);
                                    startActivity(intent);
                                    break;
                            }
                        }

                    }
                });
    }
}