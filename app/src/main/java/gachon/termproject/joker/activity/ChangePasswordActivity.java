package gachon.termproject.joker.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import gachon.termproject.joker.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText original_password_text;
    private EditText new_password_text;
    private Button change_password_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?

        original_password_text = (EditText)findViewById(R.id.original_password_text);
        new_password_text = (EditText)findViewById(R.id.new_password_text);

        change_password_button = (Button)findViewById(R.id.change_password_button);
        change_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String originalPW = original_password_text.getText().toString();
                String newPW = new_password_text.getText().toString();

                // if (기존의 비밀번호를 잘못입력했을 때) {
                // Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show(); }
                // else if (기존 비밀번호는 잘 입력했는데 비밀번호가 6자리가 아닐 때) {
                // Toast.makeText(getApplicationContext(), "비밀번호는 최소 6자가 되어야 합니다", Toast.LENGTH_SHORT).show(); }
                // else if (조건 모두 만족했을 때) {
                // 비밀번호 변경 로직 넣고
                Toast.makeText(getApplicationContext(), "비밀번호가 변경되었습니다", Toast.LENGTH_SHORT).show();
                finish();
                finish();
                //startActivity(new Intent(getApplicationContext(), SettingMyInfoActivity.class));
                // }
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