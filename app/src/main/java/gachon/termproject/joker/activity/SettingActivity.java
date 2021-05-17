package gachon.termproject.joker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import gachon.termproject.joker.R;

public class SettingActivity extends AppCompatActivity {

    private TextView myinfoText;
    private TextView bellText;
    private TextView logoutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        myinfoText = findViewById(R.id.myinfoText);
        myinfoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CheckPasswordActivity.class));
            }
        });

        bellText = findViewById(R.id.bellText);
        bellText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingBellActivity.class));
            }
        });

        logoutText = findViewById(R.id.logoutText);
        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그아웃 되도록
            }
        });
    }
}