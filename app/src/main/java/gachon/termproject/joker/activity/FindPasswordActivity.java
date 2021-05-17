package gachon.termproject.joker.activity;

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

public class FindPasswordActivity extends AppCompatActivity {
    //public static String identifier; // 회원가입을 위한 전역변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        Button findButton = findViewById(R.id.find_password_button01);
        EditText email = findViewById(R.id.find_password_edittext01);

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserEmail = email.getText().toString().trim();

                if (TextUtils.isEmpty(UserEmail))
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                else
                    checkEmail(UserEmail);
            }
        });
    }

    // 비밀번호를 찾기 위한 이메일 확인!!
    private void checkEmail(String email) {

    }
}
