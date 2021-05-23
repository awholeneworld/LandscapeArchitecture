package gachon.termproject.joker.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.activity.ExpertPortfolioLinkActivity;
import gachon.termproject.joker.adapter.MyInfoPortfolioReviewAdapter;

import static gachon.termproject.joker.fragment.MyInfoFragment.locationStr;

public class MyInfoPortfolioFragment extends AppCompatActivity {
    private StorageReference storageReference;
    private Uri file;
    private RecyclerView contents;
    private Button change_main_image;
    private Button connect_link;
    private TextView numberView;
    private ImageView mainImg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo_portfolio);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?

        ImageView profileImage= findViewById(R.id.myInfoProfileImage);
        TextView nickname = findViewById(R.id.myInfoNickname);
        TextView location = findViewById(R.id.myInfoLocation);
        mainImg = findViewById(R.id.main_image);

        if (!UserInfo.profileImg.equals("None"))
            Glide.with(getApplicationContext()).load(UserInfo.profileImg).override(1000).thumbnail(0.1f).into(profileImage);
        nickname.setText(UserInfo.nickname);
        location.setText(locationStr);

        change_main_image = findViewById(R.id.change_main_image);
        change_main_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "사진을 선택하세요."), 0);
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
        contents.setAdapter(new MyInfoPortfolioReviewAdapter(getApplicationContext(), numberView));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && data != null && data.getData() != null) { // 이미지 파일 선택하였다면
            file = data.getData();
            uploadPortfolioImage();
        }
    }

    // 이미지 넣는거
    private void uploadPortfolioImage(){
        storageReference = FirebaseStorage.getInstance().getReference().child("portfolioImages/" + UserInfo.userId);
        storageReference.putFile(file).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return storageReference.getDownloadUrl(); // URL은 반드시 업로드 후 다운받아야 사용 가능
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() { // URL 다운 성공 시
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) { // URL을 포스트 내용 Class(postContent)와 DB에 업데이트
                    Uri downloadUrl = task.getResult();
                    String url = downloadUrl.toString();

                    FirebaseFirestore.getInstance().collection("users").document(UserInfo.userId).update("portfolioImg", url).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            UserInfo.portfolioImg = url;
                            Glide.with(getApplicationContext()).load(url).into(mainImg);
                            Toast.makeText(getApplicationContext(), "포트폴리오 이미지가 설정/변경되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}