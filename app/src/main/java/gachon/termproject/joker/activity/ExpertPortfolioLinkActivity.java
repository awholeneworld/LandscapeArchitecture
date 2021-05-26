package gachon.termproject.joker.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;

public class ExpertPortfolioLinkActivity extends AppCompatActivity {

    private TextView original_link_text;
    private Button original_link_button;
    private EditText new_link_text;
    private Button new_link_button;
    private Button change_link_button;
    private boolean orilink = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_portfolio_link);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제


        original_link_text = findViewById(R.id.original_link_text);
        original_link_button = findViewById(R.id.original_link_button);
        new_link_text = (EditText)findViewById(R.id.new_link_text);
        new_link_button = (Button)findViewById(R.id.new_link_button);
        change_link_button = (Button)findViewById(R.id.change_link_button);


        //기존 링크 넣어주기
        if (!UserInfo.portfolioWeb.equals("None")) {
            original_link_text.setText(UserInfo.portfolioWeb);
            orilink = true;
        }
        else
            original_link_text.setText("기존에 등록된 링크가 없습니다.");


        original_link_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orilink)
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(original_link_text.getText().toString())));
            }
        });


        new_link_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newLink = new_link_text.getText().toString();
                newLink = URLUtil.guessUrl(newLink);
                boolean link_http_check = URLUtil.isValidUrl(newLink);


                if(link_http_check)
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(newLink)));
                else
                    Toast.makeText(getApplicationContext(), "올바른 링크를 입력하세요", Toast.LENGTH_SHORT).show();
            }
        });


        change_link_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newLink = new_link_text.getText().toString();
                newLink = URLUtil.guessUrl(newLink);
                boolean link_http_check = URLUtil.isValidUrl(newLink);

                if(link_http_check) {
                    String finalNewLink = newLink;
                    FirebaseFirestore.getInstance().collection("users").document(UserInfo.userId).update("portfolioWeb", newLink).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            UserInfo.portfolioWeb = finalNewLink;
                            Toast.makeText(getApplicationContext(), "포트폴리오 링크 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "올바른 링크를 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}