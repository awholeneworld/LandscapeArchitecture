package gachon.termproject.joker;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class WritePostActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;
    private Toolbar mToolbar;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private PostContent postContent;
    private Uri image; // 이미지 저장 변수
    private ArrayList<String> contentList = new ArrayList<String>();
    private ArrayList<String> imagesNumber = new ArrayList<String>();
    private ArrayList<Uri> imagesList = new ArrayList<Uri>();
    private ArrayList<Integer> contentOrder = new ArrayList<Integer>();
    private String userId = user.getUid(); // 누가 업로드 했는지 알기 위함
    private String postId;
    private int postTime;
    LinearLayout layout;
    EditText title, content;
    ImageButton imageAddButton;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_post);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목

        layout = findViewById(R.id.writepost_layout);
        title = findViewById(R.id.writepost_title);
        content = findViewById(R.id.writepost_content);
        imageAddButton = findViewById(R.id.writepost_imageAddButton);
        register = findViewById(R.id.writepost_assign);

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
                    post("example");
                    setResult(RESULT_OK, new Intent());
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
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(dpToPx(35),0, dpToPx(35),0);

            // 이미지뷰를 생성하고 초기화한다.
            ImageView imageView = new ImageView(WritePostActivity.this);
            imageView.setLayoutParams(layoutParams);
            Glide.with(this).load(image).into(imageView);

            // 텍스트뷰가 이어서 생성된다.
            EditText editText = new EditText(WritePostActivity.this);
            editText.setLayoutParams(layoutParams);
            editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
            editText.setBackground(null);
            editText.setTextSize(16);

            layout.addView(imageView);
            layout.addView(editText);

            imagesList.add(image);
        }
    }

    // 파일선택 함수
    private void selectFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"파일을 선택해주세요."),0);
    }

    // 포스트 업로드 함수
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void post(String category) {
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
                Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
            }
        });

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

        // 포스트 내용 바구니
        postId = String.valueOf(System.currentTimeMillis());
        postContent = new PostContent(userId, title.getText().toString(), postId, contentList, imagesNumber, contentOrder);

        // DB에 글 올리기
        databaseReference.child("Posts/" + category + "/" + postId).setValue(postContent).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // Storage에 이미지 업로드 후 Url 저장하기
        for (int i = 0; i < imagesList.size(); i++) {
            try {
                StorageReference imageStorage = storageReference.child("imagesPosted/" + category + "/" + userId + "/" + postId + "/" + image.getLastPathSegment());
                UploadTask uploadTask = imageStorage.putFile(imagesList.get(i));
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
                            //파이어베이스에 데이터베이스 업로드
                            @SuppressWarnings("VisibleForTests")
                            Uri downloadUrl = task.getResult();
                            String url = downloadUrl.toString();
                            /*
                            ImageDTO imageDTO = new ImageDTO();
                            imageDTO.setImageUrl(downloadUrl.toString());
                            imageDTO.setTitle(etTitle.getText().toString());
                            imageDTO.setDescription(etDesc.getText().toString());
                            imageDTO.setUid(mAuth.getCurrentUser().getUid());
                            imageDTO.setUserId(mAuth.getCurrentUser().getEmail());
                            */
                            //image 라는 테이블에 json 형태로 담긴다.
                            //database.getReference().child("Profile").setValue(imageDTO);
                            //  .push()  :  데이터가 쌓인다.
                            databaseReference.child("Posts/" + category + "/" + postId + "/images/" + imageNum).setValue(url);
                            Toast.makeText(WritePostActivity.this, "업로드 성공", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle failures
                            // ...
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