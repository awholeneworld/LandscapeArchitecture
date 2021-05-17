package gachon.termproject.joker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import gachon.termproject.joker.R;

public class SettingBellActivity extends AppCompatActivity {

    private ImageButton backButton;
    private Switch bell_switch1;
    private Switch bell_switch2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_bell);

        backButton = (ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingMyInfoActivity.class));
            }
        });

        bell_switch1 = (Switch)findViewById(R.id.bell_switch1);
        bell_switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "댓글 및 답글 알림이 켜졌습니다", Toast.LENGTH_SHORT);
                    // 알림 키는 기능 넣어주세요
                } else if (isChecked) {
                    Toast.makeText(getApplicationContext(), "댓글 및 답글 알림이 꺼졌습니다", Toast.LENGTH_SHORT);
                    // 알림 끄는 기능 넣어주세요
                }
            }
        });

        bell_switch2 = (Switch)findViewById(R.id.bell_switch2);
        bell_switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "매칭 알림이 켜졌습니다", Toast.LENGTH_SHORT);
                    // 알림 키는 기능 넣어주세요
                } else if (isChecked) {
                    Toast.makeText(getApplicationContext(), "매칭 알림이 꺼졌습니다", Toast.LENGTH_SHORT);
                    // 알림 끄는 기능 넣어주세요
                }
            }
        });
    }
}