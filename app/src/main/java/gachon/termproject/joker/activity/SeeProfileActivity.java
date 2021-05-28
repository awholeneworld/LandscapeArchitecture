package gachon.termproject.joker.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.fragment.MyInfoPortfolioFragment;
import gachon.termproject.joker.fragment.MyInfoTabCommentFragment;
import gachon.termproject.joker.fragment.MyInfoTabPostFragment;

public class SeeProfileActivity extends AppCompatActivity {

    private SwipeRefreshLayout refresher;
    private Button portfolioButton;
    public static ImageView profileImg;
    public static TextView nickname;
    public static TextView location;
    public static TextView intro;
    private LinearLayout portfolioLayout;
    static String locationStr;
    Intent intent;
    String postId;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_profile);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?

        intent = getIntent();
        postId = intent.getStringExtra("postId");

        // 레이아웃 가져오기
        refresher = findViewById(R.id.myInfoRefresher);
        portfolioLayout = findViewById(R.id.portfolioLayout);
        portfolioButton = findViewById(R.id.portfolioButton);
        profileImg = findViewById(R.id.profileImage);
        nickname = findViewById(R.id.profileNickname);
        location = findViewById(R.id.profileLocation);
        intro = findViewById(R.id.profileMessage);

        // 포트폴리오 버튼 설정
        if (UserInfo.isPublic)
            portfolioLayout.setVisibility(View.GONE);

        // 프사 설정
        profileImg.setBackground(new ShapeDrawable(new OvalShape()));
        profileImg.setClipToOutline(true);
        if (UserInfo.portfolioImg != null && !UserInfo.profileImg.equals("None"))
            Glide.with(getApplicationContext()).load(UserInfo.profileImg).override(1000).thumbnail(0.1f).into(profileImg);

        // 닉네임 설정
        nickname.setText(UserInfo.nickname);

        // 지역 설정
        locationStr = "";
        for (String item : UserInfo.location) {
            locationStr += item + " ";
        }

        location.setText(locationStr);

        // 한줄 소개 설정
        intro.setText(UserInfo.introduction);

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