package gachon.termproject.joker.activity;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.adapter.ExpertPortfolioReviewAdapter;

public class ExpertPortfolioActivity extends AppCompatActivity {
    private RecyclerView contents;
    private TextView numberView;
    private String locationStr;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_expert_portfolio);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?

        ImageView profileImage= findViewById(R.id.myInfoProfileImage);
        ImageView portfolioImg = findViewById(R.id.portfolioImg);
        TextView nickname = findViewById(R.id.myInfoNickname);
        TextView locationView = findViewById(R.id.myInfoLocation);
        Button chatBtn = findViewById(R.id.chatButton);
        Button portfolioWeb = findViewById(R.id.portfolioWeb);

        Intent intent = getIntent();
        String expertUserId = intent.getStringExtra("userId");
        String expertNickname = intent.getStringExtra("nickname");
        String expertProfileImg = intent.getStringExtra("profileImg");
        String expertPortfolioImg = intent.getStringExtra("portfolioImg");
        String expertPortfolioWeb = intent.getStringExtra("portfolioWeb");
        String expertPushToken = intent.getStringExtra("pushToken");
        ArrayList<String> expertLocation = intent.getStringArrayListExtra("location");

        // 프로필 이미지 설정
        profileImage.setBackground(new ShapeDrawable(new OvalShape()));
        profileImage.setClipToOutline(true);
        if (!expertProfileImg.equals("None"))
            Glide.with(getApplicationContext()).load(UserInfo.profileImg).override(1000).thumbnail(0.1f).into(profileImage);

        // 닉네임 설정
        nickname.setText(expertNickname);

        // 지역 설정
        locationStr = "";
        for (String item : expertLocation) {
            locationStr += item + " ";
        }
        locationView.setText(locationStr);

        // 대표 포트폴리오 이미지 설정
        if (!expertPortfolioImg.equals("None"))
            Glide.with(getApplicationContext()).load(expertPortfolioImg).override(1000).thumbnail(0.1f).into(portfolioImg);

        // 리뷰 설정
        numberView = findViewById(R.id.number);
        contents = findViewById(R.id.content_portfolio_myinfo);
        contents.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        contents.setHasFixedSize(true);
        contents.setAdapter(new ExpertPortfolioReviewAdapter(getApplicationContext(), expertUserId, numberView));

        // 채팅버튼 눌렀을 때
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("userId", expertUserId);
                intent.putExtra("nickname", expertNickname);
                intent.putExtra("profileImg", expertProfileImg);
                intent.putExtra("pushToken", expertPushToken);
                startActivity(intent);
            }
        });

        // 포트폴리오 버튼 눌렀을 때
        portfolioWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expertPortfolioWeb.equals("None")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(expertPortfolioWeb)));
                }
                else
                    Toast.makeText(getApplicationContext(), "등록된 웹 포트폴리오가 없습니다", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}