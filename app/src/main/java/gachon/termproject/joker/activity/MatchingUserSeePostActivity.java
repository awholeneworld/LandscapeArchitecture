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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import gachon.termproject.joker.Content.RequestFromExpertContent;
import gachon.termproject.joker.FirebaseDeleter;
import gachon.termproject.joker.R;
import gachon.termproject.joker.adapter.MatchingPostRequestAdapter;
import gachon.termproject.joker.fragment.MatchingUserTabCompleteFragment;
import gachon.termproject.joker.fragment.MatchingUserTabRequestFragment;

public class MatchingUserSeePostActivity extends AppCompatActivity {
    private LinearLayout container;
    private static DatabaseReference databaseReference;
    private static MatchingPostRequestAdapter adapter;
    private static ArrayList<RequestFromExpertContent> requestsList;
    private static TextView requestsNumView;
    private static TextView matchingView;
    private static String postId;
    private static int requestsNum = 0;
    private String locationStr;
    private boolean isMatched;
    private ArrayList<String> images;
    Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_user_see_post);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제

        // 인텐트 데이터 가져오기
        intent = getIntent();
        postId = intent.getStringExtra("postId");
        String profileImg = intent.getStringExtra("profileImg");
        ArrayList<String> content = intent.getStringArrayListExtra("content");
        images = intent.getStringArrayListExtra("images");
        isMatched = intent.getBooleanExtra("isMatched", false);
        locationStr = intent.getStringExtra("locationInPost");

        // 레이아웃 가져오기
        ImageView profile = findViewById(R.id.postProfile);
        TextView title = findViewById(R.id.title);
        TextView nickname = findViewById(R.id.postNickname);
        TextView time = findViewById(R.id.postTime);
        TextView location =  findViewById(R.id.see_post_location_name);
        requestsNumView = findViewById(R.id.see_post_request_total_num);
        matchingView = findViewById(R.id.see_post_matching_line);
        RecyclerView requests = findViewById(R.id.matching_see_post_request_listview);
        container = findViewById(R.id.see_post_content);

        // 제목, 닉네임, 작성시간, 지역 세팅
        title.setText(intent.getStringExtra("title"));
        nickname.setText(intent.getStringExtra("nickname"));
        time.setText(intent.getStringExtra("time"));
        location.setText(locationStr);

        // 프로필 사진 세팅 (oimage 동그랗게)
        profile.setBackground(new ShapeDrawable(new OvalShape()));
        profile.setClipToOutline(true);
        if (!profileImg.equals("None"))
            Glide.with(this).load(profileImg).into(profile);

        // 글 내용 넣기 - TextView 생성 후 layout_width, layout_height, gravity, 내용 등 설정
        TextView text_content = new TextView(MatchingUserSeePostActivity.this);
        text_content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        text_content.setText(content.get(0));
        text_content.setTextSize(dpToPx(7));
        text_content.setTextColor(Color.BLACK);
        container.addView(text_content);

        requestsList = new ArrayList<>();
        adapter = new MatchingPostRequestAdapter(this, getApplicationContext(), requestsList, postId);
        databaseReference = FirebaseDatabase.getInstance().getReference("Matching/userRequests/" + postId + "/requests");

        requests.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        requests.setHasFixedSize(true);
        requests.setAdapter(adapter);

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

        // 매칭 전이라면 요청 모두 표시 & 매칭 후라면 매칭된 사람만 보여주기
        if (!isMatched)
            beforeMatched();
        else
            afterMatched();
    }

    //위에 메뉴 관련
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            //자기가 쓴 글이므로 - 삭제
            case R.id.delete:
                FirebaseDeleter.postDelete(this, "Matching", "userRequests", postId, images);
                if (!isMatched)
                    MatchingUserTabRequestFragment.databaseReference.addValueEventListener(MatchingUserTabRequestFragment.postsListener);
                else
                    MatchingUserTabCompleteFragment.databaseReference.addValueEventListener(MatchingUserTabCompleteFragment.postsListener);

                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_post_menu, menu);

        // To display icon on overflow menu
        if (menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public static void beforeMatched() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requestsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RequestFromExpertContent content = snapshot.getValue(RequestFromExpertContent.class);
                    content.setUserId(snapshot.getKey());
                    requestsList.add(content);
                }
                requestsNum = requestsList.size();
                requestsNumView.setText(requestsNum + "건");
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public static void afterMatched() {
        matchingView.setText("매칭완료");
        requestsNumView.setVisibility(View.GONE);
        requestsList.clear();

        FirebaseDatabase.getInstance().getReference("Matching/userRequests/" + postId + "/requests").orderByChild("isMatched").equalTo(true).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if (snapshot.child("isMatched").getValue().equals(true)) {
                    RequestFromExpertContent content = snapshot.getValue(RequestFromExpertContent.class);
                    content.setUserId(snapshot.getKey());
                    requestsList.add(content);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {                                }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {                                }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {                                }
        });
    }

    public static int dpToPx(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}