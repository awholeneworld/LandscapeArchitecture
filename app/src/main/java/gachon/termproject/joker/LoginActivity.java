package gachon.termproject.joker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button button_login = (Button)findViewById(R.id.login_button);
        TextView forgetPW = (TextView)findViewById(R.id.login_text04_forgetPW);
        TextView toSignup = findViewById(R.id.login_text06_signup);
        EditText id = findViewById(R.id.login_editText_ID);
        EditText pw = findViewById(R.id.login_editText_PW);

        fAuth = FirebaseAuth.getInstance();

        //login버튼을 눌럿을때
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //editText에서 아이디 비번 받아오기
                String ID = id.getText().toString().trim();
                String PW = pw.getText().toString().trim();

                //뭐 아이디랑 비밀번호가지고 여차져차 햇습니다

                boolean isEnable = true; //아뒤비번이맞으면  true, 아니면 flase

                if(isEnable){ //아듸비번맞으면 홈으로 넘어갑니다


                    //근데 아직 홈을 구현 안해서... 구현되면 주석풀면됨
                    //일단 로그인 되었다고 치고 토스트만 띄웁니다
                    Toast.makeText(getApplicationContext(), "로그인 성공!! (화면넘어가는건 추후 홈이 구현된뒤에..)", Toast.LENGTH_LONG).show();

//                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                    startActivity(intent);
                }
                else{ //아뒤비번이 안맞으면 토스트띄웁니다
                    Toast.makeText(getApplicationContext(), "아이디나 비밀번호가 다릅니다", Toast.LENGTH_LONG).show();
                }
            }
        });


        //비밀번호 까묵엇을때
        forgetPW.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // forget password 글자 눌렀을 때 넘어가는 화면넣기
                Toast.makeText(getApplicationContext(), "비밀번호를 까먹엇다고? 아직 비밀번호찾기페이지가 구현이 안되어서 찾을수없다..", Toast.LENGTH_LONG).show();

            }
        });


        //대망의 회원가입!!!
        toSignup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Signup00Activity.class);
                startActivity(intent);//액티비티 이동
            }
        });

    }
}