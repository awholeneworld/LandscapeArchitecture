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

public class SeePostActivity extends AppCompatActivity {
    private LinearLayout container;
    private RecyclerView commentSection;
    private DatabaseReference databaseReference;
    private ArrayList<PostCommentContent> postCommentList;
    private PostCommentAdapter postCommentAdapter;
    private PostCommentContent postComment;
    private ValueEventListener commentsListener;

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

        // imageView 채우기
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

        // 댓글 불러오기
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts/" + category + "/" + postId + "/comments");
        commentSection = findViewById(R.id.comment_listview);

        postCommentList = new ArrayList<>();
        postCommentAdapter = new PostCommentAdapter(getApplicationContext(), postCommentList);

        commentSection.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        commentSection.setHasFixedSize(true);
        commentSection.setAdapter(postCommentAdapter);

        commentsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postCommentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    postComment = snapshot.getValue(PostCommentContent.class);
                    postCommentList.add(postComment);
                }
                postCommentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addListenerForSingleValueEvent(commentsListener);

        // 댓글 작성
        ImageButton uploadComment = findViewById(R.id.see_post_comment_send_button);
        uploadComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText commentContent = findViewById(R.id.see_post_comment_text);
                String comment = commentContent.getText().toString();
                Date currentTime = new Date();
                String updateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault()).format(currentTime);
                String commentId = String.valueOf(System.currentTimeMillis());
                PostCommentContent postCommentContent = new PostCommentContent(category, UserInfo.userId, UserInfo.nickname, UserInfo.profileImg, updateTime, commentId, comment);

                // DB에 올리기
                databaseReference.child(commentId).setValue(postCommentContent)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                databaseReference.addListenerForSingleValueEvent(commentsListener);
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
        finish();
    }

    public static int dpToPx(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}