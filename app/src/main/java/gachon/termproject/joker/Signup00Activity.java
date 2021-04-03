package gachon.termproject.joker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Signup00Activity extends AppCompatActivity {
    public static Context context_00;
    public boolean publicMan = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup00_selecttype);
        context_00 = this;

        Button publicbutton = findViewById(R.id.signup00_button01);
        Button expertbutton = findViewById(R.id.signup00_button02);
        TextView toLogin = findViewById(R.id.signup00_textView_goLogIn);

        //다시 로그인으로 회귀
        toLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

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
                publicMan = false;
                Intent intent = new Intent(getApplicationContext(), Signup01Activity.class);
                startActivity(intent);
            }
        });
    }
}