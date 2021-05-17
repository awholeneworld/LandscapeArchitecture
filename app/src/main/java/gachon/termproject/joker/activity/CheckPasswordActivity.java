package gachon.termproject.joker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import gachon.termproject.joker.R;

public class CheckPasswordActivity extends AppCompatActivity {

    private ImageButton backButton;
    private EditText check_password_text;
    private Button check_password_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_password);

        check_password_text = (EditText)findViewById(R.id.check_password_text);

        check_password_button = (Button)findViewById(R.id.check_password_button);
        check_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "비밀번호 확인", Toast.LENGTH_SHORT).show();
                // 비밀번호 확인하는 로직 넣어주세요
                // 맞으면 화면 넘어가도록!!
                startActivity(new Intent(getApplicationContext(), SettingMyInfoActivity.class));
            }
        });

    }
}