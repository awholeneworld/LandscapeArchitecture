package gachon.termproject.joker.fragment;

import android.os.Bundle;
import android.view.View;
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
import gachon.termproject.joker.adapter.MyInfoPortfolioAdapter;

import static gachon.termproject.joker.fragment.MyInfoFragment.locationStr;

public class MyInfoPortfolioFragment extends AppCompatActivity {
    private RecyclerView contents;
    private ImageButton backButton;
    private TextView numberView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo_portfolio);

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

        numberView = findViewById(R.id.number);
        contents = findViewById(R.id.content_portfolio_myinfo);
        contents.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        contents.setHasFixedSize(true);
        contents.setAdapter(new MyInfoPortfolioAdapter(getApplicationContext(), numberView));
    }
}