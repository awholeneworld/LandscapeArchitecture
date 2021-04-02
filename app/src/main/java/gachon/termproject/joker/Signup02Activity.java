package gachon.termproject.joker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Signup02Activity extends AppCompatActivity {

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
                //비밀번호 맞는지 확인
                String pw = firstPw.getText().toString();
                String checkpw = secondPw.getText().toString();

                if(pw.compareTo(checkpw) == 0){ //비밀번호가 일치한다면
                    //pw 로 여차저차 데이터 처리


                    //닉네임 페이지로 이동
                    Intent intent = new Intent(getApplicationContext(), Signup03Activity.class);
                    startActivity(intent);
                }
                else{//불일치한다면
                    Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}