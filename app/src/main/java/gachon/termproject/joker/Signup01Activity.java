package gachon.termproject.joker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Signup01Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup01_email);

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
                String UserEmail = email.getText().toString();
                //UserEmail에 이메일을 받아와서 여차저차함

                //비밀번호 페이지로 이동
                Intent intent = new Intent(getApplicationContext(), Signup02Activity.class);
                startActivity(intent);
            }
        });
    }
}