package gachon.termproject.joker.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import gachon.termproject.joker.R;

import static gachon.termproject.joker.Util.isStorageUrl;

public class SeePostActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;
    private LinearLayout container;
    private DatabaseReference databaseReference;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_post);

        Intent intent = getIntent();
        String postId = intent.getStringExtra("postId");
        String category = "example";
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

        TextView title = findViewById(R.id.signup01_text01);
        title.setText(intent.getStringExtra("title"));

        //oimage 동그랗게
        ImageView profile = findViewById(R.id.post_profile);
        profile.setBackground(new ShapeDrawable(new OvalShape()));
        profile.setClipToOutline(true);

        // 포스트 내용 넣을 공간 지정
        container = findViewById(R.id.seepost_content);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // imageView 채우기
        databaseReference = FirebaseDatabase.getInstance().getReference();

        int imageNum = 0;
        for (int i = 0; i < order.size(); i++) {
            if (order.get(i) == 0) {
                //TextView 생성
                TextView text_content = new TextView(SeePostActivity.this);
                //layout_width, layout_height, gravity, 내용 등 설정
                text_content.setLayoutParams(lp);
                text_content.setText(content.get(i));
                text_content.setTextSize(dpToPx(7));
                text_content.setTextColor(Color.BLACK);

                //부모 뷰에 추가
                container.addView(text_content);
            } else if (order.get(i) == 1 && isStorageUrl(images.get(imageNum))){
                //이미지뷰 생성
                ImageView imageView = new ImageView(SeePostActivity.this);
                imageView.setLayoutParams(lp);
                Glide.with(SeePostActivity.this).load(images.get(imageNum)).into(imageView);
                container.addView(imageView);
                imageNum++;
            }
        }

        //댓글아직 구현암함!!!
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
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }

    public static int dpToPx(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}