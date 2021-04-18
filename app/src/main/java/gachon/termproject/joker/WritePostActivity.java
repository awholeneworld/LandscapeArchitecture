package gachon.termproject.joker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.OnProgressListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class WritePostActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;
    private Toolbar mToolbar;
    private FirebaseUser user;
    private FirebaseFirestore fStore;
    private DocumentReference documentReference;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;
    private PostContent postContent;
    private Uri image; // 이미지 저장 변수
    private ArrayList<String> postContentList = new ArrayList<String>();
    private ArrayList<String> imageList = new ArrayList<String>();
    private ArrayList<Integer> contentOrder = new ArrayList<Integer>();
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

    // 문제 : 화면 회전 시 이미지 다 날라감. 다크모드 사용시 실행 안됨.
    //이미지 동적 생성
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            image = data.getData(); // 파일 가져옴

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(dpToPx(35),0, dpToPx(35),0);

            // 이미지뷰를 생성하고 초기화한다.
            ImageView imageView = new ImageView(WritePostActivity.this);
            imageView.setLayoutParams(layoutParams);
            Glide.with(this).load(image).into(imageView);

            EditText editText = new EditText(WritePostActivity.this);
            editText.setLayoutParams(layoutParams);
            editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
            editText.setBackground(null);
            editText.setTextSize(16);

            String imageStr = image.toString();
            imageList.add(imageStr);
            layout.addView(imageView);
            layout.addView(editText);
        }
    }

    //파일선택
    private void selectFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"파일을 선택해주세요."),0);
    }

    // 글(+이미지) 업로드
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void post(String category) {
        fStore = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid(); // 누가 업로드 했는지 알기 위함
        ProgressBar progressBar = new ProgressBar(WritePostActivity.this, null, android.R.attr.progressBarStyleLarge); // 진행 상황 표시 팝업
        progressBar.setVisibility(View.VISIBLE);

        // 게시한 글 수 업데이트
        documentReference = fStore.collection("users").document(userId);
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

        // 게시글 내용 리스트에 담기
        for (int i = 1; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            if (view instanceof EditText) {
                String text = ((EditText) view).getText().toString();
                if (text.length() > 0) {
                    postContentList.add(text);
                    contentOrder.add(0);
                }
            } else if (view instanceof ImageView) {
                contentOrder.add(1);
                postContentList.add("");
            }
        }

        // 제목, 내용 String으로 변환 후 리스트에 담기
        String titleToSend = title.getText().toString();
        // String contents = String.join("\n", postContentList);
        postContent = new PostContent(userId, titleToSend, postContentList, imageList, contentOrder);

        // DB에 올리기
        mDatabase.child("Posts").child(category).push().setValue(postContent).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // Storage에 이미지 올리기
        String postPath = String.valueOf(System.currentTimeMillis());
        for (int i = 0; i < imageList.size(); i++) {
            image = Uri.parse(imageList.get(i));
            storageReference.child("imagesPosted/" + category + "/" + userId + "/" + postPath + "/" + postPath + ".jpg").putFile(image).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) { // 업로드 몇퍼센트 진행되고 있는지 알려줌
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressBar.setProgress((int)progress, true);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() { // 파일 업로드
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { // 업로드 완료되면
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

    }

    public static int dpToPx(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}