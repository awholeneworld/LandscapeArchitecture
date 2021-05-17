package gachon.termproject.joker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.activity.ExpertPortfolioLinkActivity;
import gachon.termproject.joker.adapter.MyInfoPortfolioAdapter;

import static gachon.termproject.joker.fragment.MyInfoFragment.locationStr;

public class MyInfoExpertPortfolioFragment extends AppCompatActivity {
    private RecyclerView contents;
    private ImageButton backButton;
    private Button change_main_image;
    private Button connect_link;
    private TextView numberView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo_expert_portfolio);

        backButton = (ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MyInfo로 가도록!
                // intent 넣어주기
                // startActivity(new Intent(getApplicationContext(), MyInfoFragment.class));
            }
        });

        ImageView profileImage= findViewById(R.id.myInfoProfileImage);
        TextView nickname = findViewById(R.id.myInfoNickname);
        TextView location = findViewById(R.id.myInfoLocation);

        if (!UserInfo.profileImg.equals("None"))
            Glide.with(getApplicationContext()).load(UserInfo.profileImg).override(1000).thumbnail(0.1f).into(profileImage);
        nickname.setText(UserInfo.nickname);
        location.setText(locationStr);

        change_main_image = findViewById(R.id.change_main_image);
        change_main_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이미지 수정할 수 있도록 로직 넣어주세요
            }
        });

        connect_link = findViewById(R.id.connect_link);
        connect_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ExpertPortfolioLinkActivity.class));
                // 웹 포트폴리오 링크 수정하는 창으로 이동
            }
        });

        numberView = findViewById(R.id.number);
        contents = findViewById(R.id.content_portfolio_myinfo);
        contents.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        contents.setHasFixedSize(true);
        contents.setAdapter(new MyInfoPortfolioAdapter(getApplicationContext(), numberView));
    }
}