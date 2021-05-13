package gachon.termproject.joker.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import gachon.termproject.joker.adapter.PostImage;
import gachon.termproject.joker.container.PostContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.FirebaseHelper;
import gachon.termproject.joker.UserInfo;

public class WritePostActivity extends AppCompatActivity {
    private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseHelper firebaseHelper = new FirebaseHelper(this);
    private PostContent postContent;
    private Uri image; // 이미지 저장 변수
    private ArrayList<String> contentList = new ArrayList<>();
    private ArrayList<String> imagesNumber = new ArrayList<>();
    private ArrayList<Uri> imagesList = new ArrayList<>();
    private ArrayList<Integer> contentOrder = new ArrayList<>();
    private String userId = UserInfo.userId; // 누가 업로드 했는지 알기 위함
    private String nickname = UserInfo.nickname;
    private String postId;
    private int postTime;
    private LinearLayout layout;
    private EditText title, content;
    private ImageButton imageAddButton;
    private Button register;
    private int imageorder[];
    private int imagenum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기 활성화 => 여기에 아이콘 바꿔치기
        actionBar.setHomeAsUpIndicator(R.drawable.close_grey_24x24);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목
        TextView textview = findViewById(R.id.writepost_toolbar_textview);
        textview.setText("게시글 작성");

        layout = findViewById(R.id.writepost_layout);
        title = findViewById(R.id.writepost_title);
        content = findViewById(R.id.writepost_content);
        imageAddButton = findViewById(R.id.writepost_imageAddButton);
        register = findViewById(R.id.writepost_assign);

        // 어떤 게시판에서 올리려고 하는 글인지 카테고리 정보 가져오기
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");

        // 파일 선택
        imageAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });

        // 게시글 등록!
        register.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (title.length() > 0 && content.length() > 0) {
                    post(category);
                    setResult(RESULT_OK, new Intent()); // 게시판에게 완료됐다는 신호 보내기
                    finish();
                } else if (title.length() <= 0){
                    Toast.makeText(getApplicationContext(), "제목을 최소 1자 이상 써주십시오.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "내용을 최소 1자 이상 써주십시오.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // 이미지 파일 선택 후 실행되는 액티비티 : 이미지 동적 생성
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // 이미지 파일 가져옴
            image = data.getData();

            // 레이아웃 설정
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(dpToPx(35),0, dpToPx(35),0);

            PostImage postimage = new PostImage(WritePostActivity.this, image, layoutParams);


            layout.addView(postimage);
            imagesList.add(image);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 파일선택 함수
    private void selectFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"파일을 선택해주세요."),0);
    }

    // 글 올리기 함수
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void post(String category) {
        // 글 내용과 글, 이미지 순서 각각 리스트에 담기
        int j = 0;
        for (int i = 1; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            if (view instanceof EditText) {
                String text = ((EditText) view).getText().toString();
                if (text.length() > 0) {
                    contentList.add(text);
                    contentOrder.add(0);
                }
            } else if (view instanceof ImageView) {
                contentOrder.add(1);
                contentList.add("");
                imagesNumber.add(j + "");
                j++;
            }
        }

        // 사용자가 게시한 글 수 업데이트
        documentReference = fireStore.collection("users").document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        postTime = document.getLong("posts").intValue();
                        postTime += 1;
                        documentReference.update("posts", postTime);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "다큐먼트 참조 실패", Toast.LENGTH_LONG).show();
            }
        });

        // 포스트 시간 설정
        Date currentTime = new Date();
        String updateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault()).format(currentTime);

        // 포스트할 내용 바구니
        postId = String.valueOf(System.currentTimeMillis());
        postContent = new PostContent(category, userId, UserInfo.profileImg, title.getText().toString(), nickname, updateTime, postId, contentList, imagesNumber, contentOrder);

        // DB에 글 내용 올리기
        databaseReference.child("Posts/" + category + "/" + postId).setValue(postContent).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // DB에 이미지 올리기
        for (int i = 0; i < imagesList.size(); i++) {
            try {
                // Storage에 이미지 업로드
                image = imagesList.get(i);
                StorageReference imageStorage = storageReference.child("imagesPosted/" + category + "/" + userId + "/" + postId + "/" + image.getLastPathSegment());
                UploadTask uploadTask = imageStorage.putFile(image);
                int imageNum = i;
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return imageStorage.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            // 이미지의 다운로드 Url를 postContent와 DB에 업데이트
                            Uri downloadUrl = task.getResult();
                            String url = downloadUrl.toString();
                            postContent.setImages(imageNum, url);
                            databaseReference.child("Posts/" + category + "/" + postId + "/images/" + imageNum).setValue(url);
                            Toast.makeText(WritePostActivity.this, "업로드 성공", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (NullPointerException e) {
                Toast.makeText(WritePostActivity.this, "이미지 없음", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static int dpToPx(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}