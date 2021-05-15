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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.adapter.PostCommentAdapter;
import gachon.termproject.joker.container.PostCommentContent;

import static gachon.termproject.joker.Util.isStorageUrl;

public class Matching_user_SeePostActivity extends AppCompatActivity {
    private LinearLayout container;


    //해야 할 일!
//    1. 선택한 지역 보여주기 (한글로) => 서울 | 경기도 | 전라남도
//    2. 매칭요청이 몇건인지 보여주기
//    3. 추가된 매칭이 있다면 list로 보여주기 => item_matching_request.xml 사용
//    4. list item들의 button 연결하기.........
//    5. 4에서 button 들이 연결된다면 "매칭 수락" 버튼을 눌렀을 때 팝업이 뜨게 해야 하는데,,,,,
//    6. 매칭 수락해서 팝업에서 예 눌렀으면 item_matching_end.xml 을 나타내서 그거 한사람만 남기고 나머지는 다 지워주면 됨
// 아예 게시글 상태같은걸 지정 해야 할듯?


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_post);

        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        String postId = intent.getStringExtra("postId");
        String profileImg = intent.getStringExtra("profileImg");
        ArrayList<String> content = intent.getStringArrayListExtra("content");
        ArrayList<String> images = intent.getStringArrayListExtra("images");
        ArrayList<Integer> order = intent.getIntegerArrayListExtra("order");

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제

        // 제목, 닉네임, 작성시간 세팅
        TextView title = findViewById(R.id.title);
        TextView nickname = findViewById(R.id.postNickname);
        TextView time = findViewById(R.id.postTime);
        title.setText(intent.getStringExtra("title"));
        nickname.setText(intent.getStringExtra("nickname"));
        time.setText(intent.getStringExtra("time"));

        // 프로필 사진 세팅 (oimage 동그랗게)
        ImageView profile = findViewById(R.id.postProfile);
        profile.setBackground(new ShapeDrawable(new OvalShape()));
        profile.setClipToOutline(true);
        if (!profileImg.equals("None"))
            Glide.with(this).load(profileImg).into(profile);

        // 포스트 내용 넣을 공간 지정
        container = findViewById(R.id.seepost_content);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //TextView 생성
        TextView text_content = new TextView(Matching_user_SeePostActivity.this);
        //layout_width, layout_height, gravity, 내용 등 설정
        text_content.setLayoutParams(lp);
        text_content.setText(content.get(0));
        text_content.setTextSize(dpToPx(7));
        text_content.setTextColor(Color.BLACK);
        container.addView(text_content);

        //img 넣기
        LinearLayout imageContainer = findViewById(R.id.seepost_imagecontainer);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpToPx(150), dpToPx(150));
        layoutParams.setMargins(dpToPx(10),0, dpToPx(10), 0);

        // imageView 채우기
        for (int i = 0; i < images.size(); i++) {
            if(images.get(0).compareTo("") == 0) break;

            ImageView imageView = new ImageView(Matching_user_SeePostActivity.this);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            Glide.with(Matching_user_SeePostActivity.this).load(images.get(i)).into(imageView);
            imageContainer.addView(imageView);
        }


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
        finish();
    }

    public static int dpToPx(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}