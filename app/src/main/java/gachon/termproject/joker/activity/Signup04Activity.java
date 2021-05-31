package gachon.termproject.joker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;

public class Signup04Activity extends AppCompatActivity {
    public static ArrayList<String> location; // 전문가 회원가입을 위한 전역변수
    private CheckBox SU, IC, DJ, GJ, DG, US, BS, JJ, GG, GW, CB, CN, GB, GN, JB, JN, SJ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup04_location);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?

        SU = findViewById(R.id.signup04_SU);
        IC = findViewById(R.id.signup04_IC);
        DJ = findViewById(R.id.signup04_DJ);
        GJ = findViewById(R.id.signup04_GJ);
        DG = findViewById(R.id.signup04_DG);
        US = findViewById(R.id.signup04_US);
        BS = findViewById(R.id.signup04_BS);
        JJ = findViewById(R.id.signup04_JJ);
        GG = findViewById(R.id.signup04_GG);
        GW = findViewById(R.id.signup04_GW);
        CB = findViewById(R.id.signup04_CB);
        CN = findViewById(R.id.signup04_CN);
        GB = findViewById(R.id.signup04_GB);
        GN = findViewById(R.id.signup04_GN);
        JB = findViewById(R.id.signup04_JB);
        JN = findViewById(R.id.signup04_JN);
        SJ = findViewById(R.id.signup04_SJ);

        // 다음 버튼 누르면
        Button nextButton = findViewById(R.id.signup04_button01);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //어떤 박스가 체크 되었는지 확인!
                ArrayList<String> locationSelected = checklocation();

                if (!locationSelected.isEmpty()) { //하나라도 체크가 되어있다면
                    location = locationSelected; // 전역변수에 담고

                    // 회원가입 완료 페이지로 이동~ 하기 전에!!!
                    // 회원가입 맨 처음 창에서 입력한 일반인/전문가 정보에 따라서
                    // 일반 가입인지 전문가 가입인지 구별
                    if (Signup00Activity.publicMan) { //일반인이라면 회원가입 프로세스 거친 후 가입완료 페이지로
                        // 데이터베이스 가져오기
                        FirebaseAuth fAuth = FirebaseAuth.getInstance();
                        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

                        // 회원가입을 위한 전역 변수 가져오기
                        String ID = Signup01Activity.identifier;
                        String PW = Signup02Activity.password;
                        String nickname = Signup03Activity.nickname;

                        // 회원가입 프로세스
                        fAuth.createUserWithEmailAndPassword(ID, PW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 유저 고유 아이디 생성 후 정보 저장할 데이터베이스 경로 생성
                                    String userID = fAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = fStore.collection("users").document(userID);

                                    // 가입 유저 정보 맵에 모으기
                                    // 필요한 정보 더 추가 가능
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("ID", ID);
                                    user.put("nickname", nickname);
                                    user.put("location", location);
                                    user.put("isPublic", true);
                                    user.put("profileImg", "None"); // 프로필 이미지 url
                                    user.put("introduction", ""); // 자기소개 메시지

                                    documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() { // 데이터베이스에 정보 저장
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                                @Override
                                                public void onComplete(@NonNull Task<String> task) {
                                                    if (task.isSuccessful()) {
                                                        FirebaseFirestore.getInstance().collection("users").document(userID).update("pushToken", task.getResult()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                // 가입완료 페이지로 이동
                                                                Intent intent = new Intent(getApplicationContext(), Signup05Activity.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 이전 액티비티들을 모두 kill
                                                                startActivity(intent);
                                                            }
                                                        });
                                                        return;
                                                    }
                                                }
                                            });
                                        }
                                    });


                                } else {
                                    Toast.makeText(Signup04Activity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else { //전문가라면 전문가 인증 페이지로 슝
                        startActivity(new Intent(getApplicationContext(), Signup06Activity.class));
                    }
                }
                else { //체크가 하나도 안되어있다면
                    Toast.makeText(getApplicationContext(), "하나 이상의 지역을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public ArrayList<String> checklocation() {
        //선택된 지역을 저장할 리스트
        ArrayList<String> location = new ArrayList<>();

        if(SU.isChecked()) location.add("서울");
        if(IC.isChecked()) location.add("인천");
        if(DJ.isChecked()) location.add("대전");
        if(GJ.isChecked()) location.add("광주");
        if(DG.isChecked()) location.add("대구");
        if(US.isChecked()) location.add("울산");
        if(BS.isChecked()) location.add("부산");
        if(JJ.isChecked()) location.add("제주");
        if(GG.isChecked()) location.add("경기");
        if(GW.isChecked()) location.add("강원");
        if(CB.isChecked()) location.add("충북");
        if(CN.isChecked()) location.add("충남");
        if(GB.isChecked()) location.add("경북");
        if(GN.isChecked()) location.add("경남");
        if(JB.isChecked()) location.add("전북");
        if(JN.isChecked()) location.add("전남");
        if(SJ.isChecked()) location.add("세종");

        return location;
    }
}