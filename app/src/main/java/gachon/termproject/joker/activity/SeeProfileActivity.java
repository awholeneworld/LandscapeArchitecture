package gachon.termproject.joker.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;

import gachon.termproject.joker.Content.PostContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.adapter.MyInfoTabPostAdapter;

public class SeeProfileActivity extends AppCompatActivity {
    public static ImageView profileImgView;
    public static TextView nicknameView;
    public static TextView locationView;
    public static TextView introView;
    private String locationStr;
    private RecyclerView contents;
    private ArrayList<PostContent> postsList;
    private MyInfoTabPostAdapter adapter;
    private DatabaseReference postsRef;
    private OnSuccessListener onSuccessListener;
    private int successCount = 0;
    private int failCount = 0;

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

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String nickname = intent.getStringExtra("nickname");
        String profileImg = intent.getStringExtra("profileImg");
        String intro = intent.getStringExtra("intro");
        ArrayList<String> location = intent.getStringArrayListExtra("location");

        // 레이아웃 가져오기
        profileImgView = findViewById(R.id.profileImage);
        nicknameView = findViewById(R.id.profileNickname);
        locationView = findViewById(R.id.profileLocation);
        introView = findViewById(R.id.profileMessage);
        contents = findViewById(R.id.post_check_profile);

        // 프사 설정
        profileImgView.setBackground(new ShapeDrawable(new OvalShape()));
        profileImgView.setClipToOutline(true);
        if (UserInfo.portfolioImg != null && !UserInfo.profileImg.equals("None"))
            Glide.with(getApplicationContext()).load(profileImg).override(1000).thumbnail(0.1f).into(profileImgView);

        // 닉네임 설정
        nicknameView.setText(nickname);

        // 지역 설정
        locationStr = "";
        for (String item : location) {
            locationStr += item + " ";
        }

        locationView.setText(locationStr);

        // 한줄 소개 설정
        introView.setText(intro);

        postsList = new ArrayList<>();
        adapter = new MyInfoTabPostAdapter(getApplicationContext(), postsList);

        contents.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        contents.setHasFixedSize(true);
        contents.setAdapter(adapter);

        postsRef = FirebaseDatabase.getInstance().getReference("Posts");

        onSuccessListener = new OnSuccessListener<DataSnapshot>() { // DB에 Posts가 있는지 확인하기 위해 가져와본다
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) { // Posts가 없으면 암것도 안함

                } else { // Posts가 있다면 작성한 게시글 정보 가져오는 작업 수행
                    postsList.clear();
                    final long categoryNum = dataSnapshot.getChildrenCount(); // 현재 Posts에 있는 category 수

                    dataSnapshot.getRef().addChildEventListener(new ChildEventListener() { // Posts에 있는 카테고리의 데이터를 카테고리 하나씩 가져올거임
                        @Override
                        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                            snapshot.getRef().orderByChild("userId").equalTo(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) { // 쓴 글이 존재하지 않는다면
                                        failCount++;
                                    } else { // 내가 쓴 글이 있다면
                                        dataSnapshot.getRef().addValueEventListener(new ValueEventListener() { // 정보 저장
                                            @RequiresApi(api = Build.VERSION_CODES.N)
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                for (DataSnapshot shot : snapshot.getChildren()) {
                                                    PostContent content = shot.getValue(PostContent.class);
                                                    postsList.add(0, content);
                                                }
                                                successCount++; // 성공 카운트 올리기
                                                if (failCount + successCount == categoryNum) { // 모든 카테고리 확인 다 끝나면
                                                    failCount = 0;
                                                    successCount = 0;
                                                    postsList.sort(new Comparator<PostContent>() {
                                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                                        @Override
                                                        public int compare(PostContent o1, PostContent o2) {
                                                            long o1Id = Long.parseUnsignedLong(o1.getPostId());
                                                            long o2Id = Long.parseUnsignedLong(o2.getPostId());

                                                            if (o1Id < o2Id) return 1;
                                                            else return -1;
                                                        }
                                                    });
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                            }
                                        });
                                    }
                                }
                            });
                        }

                        @Override
                        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                        }

                        @Override
                        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                        }

                        @Override
                        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        }
                    });
                }
            }
        };

        postsRef.get().addOnSuccessListener(onSuccessListener);
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