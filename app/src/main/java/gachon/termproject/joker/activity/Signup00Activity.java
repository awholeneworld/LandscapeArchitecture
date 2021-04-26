package gachon.termproject.joker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import gachon.termproject.joker.R;

public class Signup00Activity extends AppCompatActivity {
    // 회원가입을 위한 전역변수 설정
    public static Context context_00;
    public boolean publicMan = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup00_selecttype);
        context_00 = this; // 이게 할당되어야 전역변수 쓸 수 있음

        Button publicbutton = findViewById(R.id.signup00_button01);
        Button expertbutton = findViewById(R.id.signup00_button02);
        TextView toLogin = findViewById(R.id.signup00_textView_goLogIn);

        //여기서 문제가 나중에 뒤에서 전문가는 전문가 인증 페이지가 나온다!!! 여기서 유저가 선택한 정보를 어케 전달해야 할지 모르겟음 백엔드 화이팅~~
        //아마 뭐 전역변수에 저장하거나 데이터 뭐 하면 잘 되지 않을까요??


        //일반인으로 가입할래여
        publicbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Signup01Activity.class);
                startActivity(intent);
            }
        });


        //전문가로 가입할래여
        expertbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicMan = false; // 전문가이면 전역변수 false
                Intent intent = new Intent(getApplicationContext(), Signup01Activity.class);
                startActivity(intent);
            }
        });

        //다시 로그인으로 회귀
        toLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                // 현재 액티비티 스택 : 로그인 액티비티 -> 회원가입00 액티비티
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP); // 로그인 액티비티가 자기 다음에 실행된 액티비티 다 없애고 제일 위로 옴
                startActivity(intent);
            }
        });
    }
}