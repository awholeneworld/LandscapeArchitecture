package gachon.termproject.joker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Signup05Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup05_finishpublic);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?


        Button nextButton = findViewById(R.id.signup05_button01);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //처리할 데이터 없음. 로그인 페이지로 이동합니다
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP); //이전 액티비티들을 모두 kill
                startActivity(intent);
            }
        });

    }
}