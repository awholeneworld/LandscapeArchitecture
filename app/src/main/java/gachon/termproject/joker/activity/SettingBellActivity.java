package gachon.termproject.joker.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import gachon.termproject.joker.R;

public class SettingBellActivity extends AppCompatActivity {

    private SwitchCompat bell_switch1;
    private SwitchCompat bell_switch2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_bell);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?

        bell_switch1 = findViewById(R.id.bell_switch1);
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

        bell_switch2 = findViewById(R.id.bell_switch2);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}