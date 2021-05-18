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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import gachon.termproject.joker.FirebaseHelper;
import gachon.termproject.joker.PostImage;
import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.container.PostContent;

public class ReWritePostActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private FirebaseHelper firebaseHelper = new FirebaseHelper(this);
    private PostContent postContent;
    private Uri image; // 이미지 저장 변수
    private ArrayList<String> contentList = new ArrayList<>();
    private ArrayList<Uri> imagesList = new ArrayList<>();
    private String userId = UserInfo.userId; // 누가 업로드 했는지 알기 위함
    private String nickname = UserInfo.nickname;
    private String postId;
    private String expertId;
    private LinearLayout layout;
    private EditText title, contenttext;
    private ImageButton imageAddButton;
    private Button register;
    private ArrayList<String> imagesUrl = new ArrayList<>();
    private int uploadFinishCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewrite_post);

        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        String postId = intent.getStringExtra("postId");
        String profileImg = intent.getStringExtra("profileImg");
        ArrayList<String> content = intent.getStringArrayListExtra("content");
        ArrayList<String> images = intent.getStringArrayListExtra("images");


        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기 활성화 => 여기에 아이콘 바꿔치기
        actionBar.setHomeAsUpIndicator(R.drawable.close_grey_24x24);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목
        TextView textview = findViewById(R.id.writepost_toolbar_textview);


        layout = findViewById(R.id.writepost_layout);
        title = findViewById(R.id.writepost_title);
        contenttext = findViewById(R.id.writepost_content);
        imageAddButton = findViewById(R.id.writepost_imageAddButton);
        register = findViewById(R.id.writepost_assign);


        //firebase에 저장되어 있던 글의 정보 가져와서 올바른 곳에 넣어주기!!!!!~!
        //제목, 내용, 사진 넣어주면 됨. => 근데 이제 사진은 어케 할건가?
        //제목이나 내용은ㅇ 그냥 key update 시켜주면 되는데
        //사진은 storage에 저장된걸 건드려야 한다!
        //아ㅓ이디어...
        //일단 images에는 image의 uri가 배열로 들어가는 모양입니다.
        //예시 : [https://firebasestorage.googleapis.com/v0/b/joker-a9981.appspot.com/o/imagesPosted%2Ffree%2FxrtlvdCUeEhOjry1oOS3KK6kEzj1%2F1621177624639%2Fimage%3A31?alt=media&token=d12550b9-8952-44d0-ba19-ee83e37f7485]
        //원래 images가 어떻게 저장되었었는지 두는 배열을 하나 두고,
        //수정에 쓰일 이미지 배열을 둔다.
        //비교를 잘하면서.... 어찌어찌한다???

        //제목이랑 내용(글) 채워넣기
        title.setText(intent.getStringExtra("title"));
        contenttext.setText(content.get(0));


        //이제 이미지를....

        // imageView 채우기
        for (int i = 0; i < images.size(); i++) {

            if(images.get(0).compareTo("") == 0) break;

            // 레이아웃 설정
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(dpToPx(35), 0, dpToPx(35), 0);

            PostImage postimage = new PostImage(ReWritePostActivity.this, Uri.parse(images.get(i)), layoutParams);
            postimage.getBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout.removeView(postimage);
                    imagesList.remove(image);
                    //여기서 list 처리를 잘 하면 여차저차 될거같은데?
                }
            });

            layout.addView(postimage);
        }





















        // 파일 선택
        imageAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(imagesList.size() < 10){
                    selectFile();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Image는 10장까지 업로드 가능합니다", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 게시글 등록!
        register.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (title.length() > 0 && contenttext.length() > 0) {
                    register.setEnabled(false);
                    post(category);
                } else if (title.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "제목을 최소 1자 이상 써주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "내용을 최소 1자 이상 써주세요.", Toast.LENGTH_SHORT).show();
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
            layoutParams.setMargins(dpToPx(35), 0, dpToPx(35), 0);

            PostImage postimage = new PostImage(ReWritePostActivity.this, image, layoutParams);
            postimage.getBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout.removeView(postimage);
                    imagesList.remove(image);
                    //여기서 list 처리를 잘 하면 여차저차 될거같은데?
                }
            });

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
    private void selectFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "파일을 선택해주세요."), 0);
    }

    // 글 올리기 함수
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void post(String category) {
        // 포스트 고유 아이디
        postId = String.valueOf(System.currentTimeMillis());

        //글넣기
        contentList.add(contenttext.getText().toString());

        if (imagesList.size() == 0) {// 사진이 없다면? 바로 글쓰기
            // 포스트 시간 설정
            Date currentTime = new Date();
            String updateTime = new SimpleDateFormat("yyyy-MM-dd k:mm", Locale.getDefault()).format(currentTime);

            // 포스트할 내용
            postContent = new PostContent(category, userId, UserInfo.profileImg, title.getText().toString(), nickname, updateTime, postId, contentList, imagesUrl, expertId, !UserInfo.isPublic, UserInfo.location);

            // Firebase Realtime DB에 글 내용 올리기
            databaseReference.child("Posts/" + category + "/" + postId).setValue(postContent).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            });
        } else { //사진이 있다면 그림넣기
            for (int i = 0; i < imagesList.size(); i++) {
                try {
                    image = imagesList.get(i);

                    // Firebase Storage에 이미지 업로드
                    StorageReference imageReference = storageReference.child("imagesPosted/" + category + "/" + userId + "/" + postId + "/" + image.getLastPathSegment());

                    UploadTask uploadTask = imageReference.putFile(image);
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            return imageReference.getDownloadUrl(); // URL은 반드시 업로드 후 다운받아야 사용 가능
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() { // URL 다운 성공 시
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) { // URL을 포스트 내용 Class(postContent)와 DB에 업데이트
                                Uri downloadUrl = task.getResult();
                                String url = downloadUrl.toString();

                                imagesUrl.add(url);
                                contentList.add("");
                                uploadFinishCount++;

                                if (uploadFinishCount == imagesList.size()) { // 이미지 업로드가 완료되면 글을 최종적으로 업로드
                                    // 포스트 시간 설정
                                    Date currentTime = new Date();
                                    String updateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault()).format(currentTime);

                                    // 포스트할 내용
                                    postContent = new PostContent(category, userId, UserInfo.profileImg, title.getText().toString(), nickname, updateTime, postId, contentList, imagesUrl, expertId, !UserInfo.isPublic,UserInfo.location);

                                    // Firebase Realtime DB에 글 내용 올리기
                                    databaseReference.child("Posts/" + category + "/" + postId).setValue(postContent).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                            setResult(RESULT_OK, new Intent());
                                            finish();
                                        }
                                    });
                                }

                                Toast.makeText(ReWritePostActivity.this, "등록중", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (NullPointerException e) {
                    Toast.makeText(ReWritePostActivity.this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}