package gachon.termproject.joker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
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

public class SeePostActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;
    private LinearLayout container;
    DatabaseReference mDatabase;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_post);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제

        //oimage 동그랗게
        ImageView profile = findViewById(R.id.post_profile);
        profile.setBackground(new ShapeDrawable(new OvalShape()));
        profile.setClipToOutline(true);


        //이제여기서 서버에서 정보 받아와서 Textview랑 imageview를 채우면 됨
        Intent intent = getIntent();
        TextView title = findViewById(R.id.signup01_text01);
        title.setText(intent.getStringExtra("title"));

        /*
        mDatabase = FirebaseDatabase.getInstance().getReference("Posts/example");
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PostContent postContent = snapshot.getValue(PostContent.class);
                        postContentList.add(0, postContent);
                    }
                }
            }
        });
        */


        //imageview같은 경우 사진 있는 경우만 나오게 예외처리? 해도될듯 ㅁㄹ알아서하세요~~ㅎ
        //일단 ㄹㅇ 들어갈 자리만 봐둔거라 레이아웃은 시험 끝나고 손볼 예정이에요~~~
        //그리고 코딩은 따로 안해둿는데 유저 이름 - 글올린 시간 - 사진도 다 넣어주셔야 해용 (아이디는 xml파일에 지정은 다 해둠ㅁ)
        //아마 유저이름 그거할라면 login에서부터 이어져야겟죠? 하지만아직구현안해서 알아서하시면될듯... 모르겟으면 바로 연락주세요
        //댓글아직 구현암함!!!

        //넣을 공간 지정
        container = findViewById(R.id.seepost_content);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ArrayList<String> content = intent.getStringArrayListExtra("content");
        ArrayList<String> images = intent.getStringArrayListExtra("images");
        ArrayList<Integer> order = intent.getIntegerArrayListExtra("order");
        int j = 0;
        for (int i = 0; i < order.size(); i++) {
            if (order.get(i) == 0) {
                //TextView 생성
                TextView text_content = new TextView(this);
                //layout_width, layout_height, gravity, 내용 등 설정
                text_content.setLayoutParams(lp);
                text_content.setText(content.get(i));
                text_content.setTextSize(ConvertDPtoPX(this, 7));
                text_content.setTextColor(Color.BLACK);

                //부모 뷰에 추가
                container.addView(text_content);
            } else if (order.get(i) == 1){
                //이미지뷰 생성
                ImageView imageView = new ImageView(SeePostActivity.this);
                imageView.setLayoutParams(lp);
                Uri image = Uri.parse(images.get(j));
                Glide.with(this).load(image).into(imageView);
                container.addView(imageView);
                j++;
            }
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
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }

    public static int ConvertDPtoPX(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}