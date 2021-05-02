package gachon.termproject.joker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import gachon.termproject.joker.R;

public class Signup02Activity extends AppCompatActivity {
    public static String password; // 회원가입을 위한 전역변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup02_pw);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?


        Button nextButton = findViewById(R.id.signup02_button01);
        EditText firstPw = findViewById(R.id.signup02_edittext01);
        EditText secondPw = findViewById(R.id.signup02_edittext02);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pw = firstPw.getText().toString().trim();
                String checkPw = secondPw.getText().toString().trim();

                // 비밀번호 확인
                if (TextUtils.isEmpty(pw)) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(checkPw)) {
                    Toast.makeText(getApplicationContext(), "확인 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (pw.compareTo(checkPw) != 0) { //불일치한다면
                    Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();

                }
                else if (pw.length() < 6) {
                    Toast.makeText(getApplicationContext(), "비밀번호는 최소 6자가 되어야 합니다.", Toast.LENGTH_SHORT).show();
                }
                else { //비밀번호가 일치한다면
                    password = pw; // PW 전역변수 설정
                    startActivity(new Intent(getApplicationContext(), Signup03Activity.class)); //닉네임 페이지로 이동
                }
            }
        });
    }
}