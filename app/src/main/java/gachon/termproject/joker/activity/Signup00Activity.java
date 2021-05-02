package gachon.termproject.joker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import gachon.termproject.joker.R;

public class Signup00Activity extends AppCompatActivity {
    public static boolean publicMan = true; // 회원가입을 위한 전역변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup00_selecttype);

        Button publicbutton = findViewById(R.id.signup00_button01);
        Button expertbutton = findViewById(R.id.signup00_button02);
        TextView toLogin = findViewById(R.id.signup00_textView_goLogIn);

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
                // 이 때의 액티비티 스택 순서 : 로그인 액티비티 -> 회원가입00 액티비티
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP); // 로그인 액티비티가 자기 다음에 실행된 액티비티 다 없애고 제일 위로 옴
                startActivity(intent);
            }
        });
    }
}