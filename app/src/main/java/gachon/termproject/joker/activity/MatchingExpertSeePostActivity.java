package gachon.termproject.joker.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import gachon.termproject.joker.Content.RequestFromExpertContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;

public class MatchingExpertSeePostActivity extends AppCompatActivity {
    private LinearLayout container;
    private Button applyBtn;
    private Button cancelBtn;
    private HashMap<String, RequestFromExpertContent> requestsList;
    private RequestFromExpertContent request;

    //해야 할 일!
//    1. 선택한 지역 보여주기 (한글로) => 서울 | 경기도 | 전라남도
//    2. 게시글 상태에 따라 버튼의 모양을 "매칭 신청", "매칭 대기", "매칭 완료"를 보여주어야 함. => 들어가있는 게시글 카테고리도 달라야 함.
    // 이때, 매칭 완료인 경우는 그 전문가와 유저가 매칭 완료된 경우만 가능!
    //다른 전문가에 의해 매칭이 완료되었다면 그냥 글 자체가 안보임.
    //헷깔리면 바로 카톡 ㄱ


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_expert_see_post);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제

        // 인텐트 데이터 가져오기
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        String postId = intent.getStringExtra("postId");
        String profileImg = intent.getStringExtra("profileImg");
        ArrayList<String> content = intent.getStringArrayListExtra("content");
        ArrayList<String> images = intent.getStringArrayListExtra("images");
        requestsList = intent.getParcelableExtra("requests");

        // 레이아웃 가져오기
        ImageView profile = findViewById(R.id.postProfile);
        TextView title = findViewById(R.id.title);
        TextView nickname = findViewById(R.id.postNickname);
        TextView time = findViewById(R.id.postTime);
        container = findViewById(R.id.see_post_content);
        applyBtn = findViewById(R.id.matching_expert_apply_button);
        cancelBtn = findViewById(R.id.matching_expert_cancel_button);

        // 이미 매칭 요청 했는지 확인
        if (category.equals("awaiting") || category.equals("complete"))
            applyBtn.setVisibility(View.INVISIBLE);
        else
            cancelBtn.setVisibility(View.INVISIBLE);


        // 제목, 닉네임, 작성시간 세팅
        title.setText(intent.getStringExtra("title"));
        nickname.setText(intent.getStringExtra("nickname"));
        time.setText(intent.getStringExtra("time"));

        // 프로필 사진 세팅 (oimage 동그랗게)
        profile.setBackground(new ShapeDrawable(new OvalShape()));
        profile.setClipToOutline(true);
        if (!profileImg.equals("None"))
            Glide.with(this).load(profileImg).into(profile);

        //TextView 생성 후 layout_width, layout_height, gravity, 내용 등 설정
        TextView text_content = new TextView(MatchingExpertSeePostActivity.this);
        text_content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        text_content.setText(content.get(0));
        text_content.setTextSize(dpToPx(7));
        text_content.setTextColor(Color.BLACK);

        // 글 넣기
        container.addView(text_content);

        // 이미지 있으면 넣기
        if (images != null) {
            LinearLayout imageContainer = findViewById(R.id.see_post_imagecontainer);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpToPx(150), dpToPx(150));
            layoutParams.setMargins(dpToPx(10),0, dpToPx(10), 0);

            // imageView 채우기
            for (int i = 0; i < images.size(); i++) {
                if(images.get(0).compareTo("") == 0) break;

                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setLayoutParams(layoutParams);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                Glide.with(getApplicationContext()).load(images.get(i)).into(imageView);
                imageContainer.addView(imageView);
            }
        }

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyBtn.setEnabled(false);
                if (requestsList == null) requestsList = new HashMap<>();
                if (request == null) request = new RequestFromExpertContent();
                request.setExpertProfileImg(UserInfo.profileImg);
                request.setExpertNickname(UserInfo.nickname);
                request.setExpertPortfolioImg(UserInfo.portfolioImg);
                request.setExpertPortfolioWeb(UserInfo.portfolioWeb);
                request.setExpertLocation(UserInfo.location);
                requestsList.put(UserInfo.userId, request);

                FirebaseDatabase.getInstance().getReference("Matching/userRequests/" + postId + "/requests").setValue(requestsList).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        applyBtn.setVisibility(View.INVISIBLE);
                        cancelBtn.setVisibility(View.VISIBLE);
                        cancelBtn.setEnabled(true);
                    }
                });
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBtn.setEnabled(false);
                FirebaseDatabase.getInstance().getReference("Matching/userRequests/" + postId + "/requests/" + UserInfo.userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        cancelBtn.setVisibility(View.INVISIBLE);
                        applyBtn.setVisibility(View.VISIBLE);
                        applyBtn.setEnabled(true);
                    }
                });
            }
        });
    }

    //위에 메뉴 관련
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.others_post_menu,menu);

        // To display icon on overflow menu
        if (menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    public static int dpToPx(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}