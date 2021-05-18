package gachon.termproject.joker.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gachon.termproject.joker.R;

public class Signup06Activity extends AppCompatActivity {
    private StorageReference storageReference; // 파일 저장소
    private EditText fileInfo; // 선택한 파일 정보 표시하기 위한 뷰
    private Uri file; // 파일 담는 그릇

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup06_authexpert);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?

        storageReference = FirebaseStorage.getInstance().getReference(); // 파일 저장소 가져오기

        fileInfo = findViewById(R.id.signup06_edittext01);
        Button select = findViewById(R.id.signup06_button01);
        Button upload = findViewById(R.id.signup06_button02);

        // 파일 선택
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });

        // 파일 업로드
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFileAndRegister();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK && data!= null && data.getData()!= null) {
            file = data.getData(); // 파일 가져옴
            fileInfo.setText(String.valueOf(file)); // EditText 뷰에 파일 정보 띄움
        }
    }

    // 파일 선택 함수
    private void selectFile(){
        Intent intent = new Intent();
        intent.setType("application/*"); // 모든 종류의 파일 선택 가능
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"파일을 선택하세요."), 0);
    }

    // 파일 업로드 후 회원가입 함수
    private void uploadFileAndRegister() {
        if (file == null) { // 파일 선택 안했을 시
            Toast.makeText(getApplicationContext(), "파일을 선택하세요.", Toast.LENGTH_SHORT).show();
        } else { // 파일 선택했을 시
            ProgressBar progressBar = new ProgressBar(Signup06Activity.this, null, android.R.attr.progressBarStyleLarge); // 진행 상황 표시 팝업
            progressBar.setVisibility(View.VISIBLE);

            String ID = Signup01Activity.identifier; // 누가 업로드 했는지 알기 위함

            StorageReference reference = storageReference.child("forExpertVerification/" + ID); // 파일 저장소 경로 생성
            reference.putFile(file).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressBar.setProgress((int)progress, true); // 업로드 몇퍼센트 진행되고 있는지 알려줌
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { // 파일 업로드 완료되면
                    Toast.makeText(Signup06Activity.this,"업로드가 완료되었습니다.",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE); // 표시 팝업 없애기
                    register(ID); // 회원가입 진행
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Signup06Activity.this,"업로드에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Signup04Activity 파일에 있던 회원가입 기능 가져온거
    private void register(String ID){
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        // 회원가입을 위한 전역변수 가져오기
        String PW = Signup02Activity.password;
        String nickname = Signup03Activity.nickname;
        ArrayList<String> locations = Signup04Activity.location;

        fAuth.createUserWithEmailAndPassword(ID, PW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);

                    Map<String, Object> user = new HashMap<>();
                    user.put("ID", ID);
                    user.put("nickname", nickname);
                    user.put("location", locations);
                    user.put("isPublic", false);
                    user.put("profileImg", "None"); // 프로필 이미지 url
                    user.put("introduction", ""); // 자기소개 메시지
                    user.put("portfolioImg", "None");
                    user.put("portfolioWeb", "None");

                    documentReference.set(user);

                    Intent intent = new Intent(getApplicationContext(), Signup05Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 이전 액티비티들을 모두 kill
                    startActivity(intent);
                } else {
                    Toast.makeText(Signup06Activity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}