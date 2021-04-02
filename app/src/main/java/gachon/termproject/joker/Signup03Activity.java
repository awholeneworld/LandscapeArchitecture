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

public class Signup03Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup03_nickname);


        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?

        Button nextButton = findViewById(R.id.signup03_button01);
        EditText nickName = findViewById(R.id.signup03_edittext01);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //닉네임을 입력받음
                String nickname = nickName.getText().toString();

                //데이터베이스에서 중복되는 닉네임 있는지 확인!!!

                boolean used = false; //true면 이미 사용되고 있는것 (데이터에 따라 바꿔주세요)



                if(!used){ //사용가능한 닉네임이라면
                    //닉네임 데이터 여차저차 처리하고



                    //주 활동 지역 페이지로 이동
                    Intent intent = new Intent(getApplicationContext(), Signup04Activity.class);
                    startActivity(intent);
                }
                else{//불일치한다면
                    Toast.makeText(getApplicationContext(), "중복된 닉네임 입니다", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}