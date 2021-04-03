package gachon.termproject.joker;

import android.content.Context;
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

public class Signup02Activity extends AppCompatActivity {
    public static Context context_02;
    public String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup02_pw);
        context_02 = this;

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
                //비밀번호 맞는지 확인
                String pw = firstPw.getText().toString().trim();
                String checkpw = secondPw.getText().toString().trim();

                if (TextUtils.isEmpty(pw)) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(checkpw)) {
                    Toast.makeText(getApplicationContext(), "확인 비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                }
                else if (pw.compareTo(checkpw) != 0) { //불일치한다면
                    Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();

                }
                else if (pw.length() < 6) {
                    Toast.makeText(getApplicationContext(), "비밀번호는 최소 6자가 되어야 합니다.", Toast.LENGTH_LONG).show();
                }
                else { //비밀번호가 일치한다면
                    //pw 로 여차저차 데이터 처리
                    password = pw; // PW 전역변수 설정

                    //닉네임 페이지로 이동
                    Intent intent = new Intent(getApplicationContext(), Signup03Activity.class);
                    startActivity(intent);
                }
            }
        });
    }
}