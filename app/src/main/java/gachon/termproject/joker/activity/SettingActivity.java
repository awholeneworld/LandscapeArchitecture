package gachon.termproject.joker.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;

public class SettingActivity extends AppCompatActivity {
    private StorageReference storageReference;
    private List<String> location;
    private ImageView profileImg;
    private EditText nickname;
    private Uri file;
    private CheckBox SU, IC, DJ, GJ, DG, US, BS, JJ, GG, GW, CB, CN, GB, GN, JB, JN, SJ;
    int flag_nickname_check = 0; // 닉네임이 중복되지 않을 때 (기본 상태)
    int flag_location = 0; // 기본상태
    int flag_message = 0; // 한줄메시지를 수정하지 않았을 때


    //////////////// 기존에 선택되어 있던 location 불러오는 부분 로직 넣어야해요!! //////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        profileImg = findViewById(R.id.profileImage);
        TextView profileImgSet = findViewById(R.id.setting_image);
        EditText email = findViewById(R.id.setting_email);
        nickname = findViewById(R.id.setting_nickname);
        EditText introMsg = findViewById(R.id.setting_message);
        Button checkNickname = findViewById(R.id.duplicateCheck);
        Button save = findViewById(R.id.setting_save_button);
        Button resetPwd = findViewById(R.id.setting_password_button);
        Button logOut = findViewById(R.id.setting_logout_button);

        // 프사 설정
        if (!UserInfo.profileImg.equals("None"))
            Glide.with(getApplicationContext()).load(UserInfo.profileImg).into(profileImg);

        // 이메일 설정
        email.setText(UserInfo.email);
        email.setTextColor(Color.parseColor("#000000"));

        // 닉네임 설정
        nickname.setText(UserInfo.nickname);

        // 프사 눌렀을 때 이미지 파일 선택 창으로 이동
        profileImgSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "사진을 선택하세요."), 0);
            }
        });

        // 닉네임 창 눌렀을 때
        nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname.setEnabled(true);
            }
        });

        // 닉네임 중복체크 버튼 눌렀을 때
        checkNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //닉네임을 입력받음
                String temp = nickname.getText().toString();

                if (temp.length() == 0)
                    Toast.makeText(getApplicationContext(), "변경할 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                else if (temp.equals(UserInfo.nickname))
                    Toast.makeText(getApplicationContext(), "본인의 닉네임입니다.", Toast.LENGTH_SHORT).show();
                else { //데이터베이스에서 중복되는 닉네임 있는지 확인!!!
                    FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                List<DocumentSnapshot> list = querySnapshot.getDocuments();

                                for (int i = 0; i < list.size(); i++) {
                                    DocumentSnapshot snapshot = list.get(i);
                                    String nicknameCheck = snapshot.getString("nickname");
                                    if (temp.compareTo(nicknameCheck) == 0) {
                                        Toast.makeText(getApplicationContext(), "중복된 닉네임 입니다", Toast.LENGTH_SHORT).show();
                                        break;
                                    } else if (i == list.size() - 1) {
                                        // 닉네임 설정 후 다음 페이지로 이동
                                        nickname.setText(temp);
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

        // 비밀번호 변경 눌렀을 때
        resetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
            }
        });

        // 로그아웃 버튼 눌렀을 때 -> 아직 앱 종료가 안됨
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                // 백스택에 있는 모든 액티비티 지우는 코드 필요함
                startActivity(intent);
                finish();
            }
        });


        /*
        //일단 지역 데이터 처리는 리스트로 만들어서 추가했는데
        //나중에 처리하면서 더 좋은 아이디어 있으면 바꿔도 됨
        //아래의 순서로 구성됨
        //서울 SU
        //인천 IC
        //대전 DJ
        //광주 GJ
        //대구 DG
        //울산 US
        //부산 BS
        //제주 JJ
        //경기 GG
        //강원 GW
        //충북 CB
        //충남 CN
        //경북 GB
        //경남 GN
        //전북 JB
        //전남 JN
        //세종 SJ

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

        setting_save_button = (Button)findViewById(R.id.setting_save_button);
        setting_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이렇게하는거 맞나 모르겠네...
                if (flag_nickname_check == 1) {
                    Toast.makeText(getApplicationContext(), "닉네임 중복 체크를 해주세요", Toast.LENGTH_SHORT).show();
                } else if (flag_nickname_check == 2) {
                    Toast.makeText(getApplicationContext(), "닉네임이 중복됩니다", Toast.LENGTH_SHORT).show();
                } else if (flag_nickname_check == 3) {
                    Toast.makeText(getApplicationContext(), "닉네임 중복 체크 완료! 변경", Toast.LENGTH_SHORT).show();
                    String newNickname = setting_nickname_text.getText().toString();
                    // 닉네임 변경 시켜주세요
                }

                // 이부분은 로직을 모르겠네요..........
                // 지역 변경 부분만 하면 될 것 같아요....
                List<String> locationSelected = checklocation();
                if (!locationSelected.isEmpty()) { //하나라도 체크가 되었다면
                    location = locationSelected;
                    flag_location = 0;
                } else {
                    flag_location = 1;
                    Toast.makeText(getApplicationContext(), "지역을 하나 이상 선택해주세요", Toast.LENGTH_SHORT).show();
                }

                if (flag_message == 1) {
                    Toast.makeText(getApplicationContext(), "한줄메시지 변화있을 때", Toast.LENGTH_SHORT).show();
                    String newMessage = setting_message_text.getText().toString();
                    // 메시지 변경 시켜주세요
                }

                if ((flag_nickname_check == 0 || flag_nickname_check == 3) && flag_location == 0) {
                    Toast.makeText(getApplicationContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                    flag_nickname_check = 0;
                    flag_location = 0;
                    flag_message = 0;
                }
            }
        });
        */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && data != null && data.getData() != null) { // 이미지 파일 선택하였다면
            file = data.getData();
            uploadProfileImage();
        }
    }


    public List<String> checklocation() {
        //선택된 지역 약어를 저장할 리스트 location
        List<String> location = new ArrayList<>();

        if(SU.isChecked()) location.add("SU");
        if(IC.isChecked()) location.add("IC");
        if(DJ.isChecked()) location.add("DJ");
        if(GJ.isChecked()) location.add("GJ");
        if(DG.isChecked()) location.add("DG");
        if(US.isChecked()) location.add("US");
        if(BS.isChecked()) location.add("BS");
        if(JJ.isChecked()) location.add("JJ");
        if(GG.isChecked()) location.add("GG");
        if(GW.isChecked()) location.add("GW");
        if(CB.isChecked()) location.add("CB");
        if(CN.isChecked()) location.add("CN");
        if(GB.isChecked()) location.add("GB");
        if(GN.isChecked()) location.add("GN");
        if(JB.isChecked()) location.add("JB");
        if(JN.isChecked()) location.add("JN");
        if(SJ.isChecked()) location.add("SJ");

        return location;
    }

    // 이미지 넣는거
    private void uploadProfileImage(){
        storageReference = FirebaseStorage.getInstance().getReference().child("profileImages/" + UserInfo.userId + "/" + file.getLastPathSegment());
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
                    UserInfo.profileImg = url;

                    profileImg.setBackground(new ShapeDrawable(new OvalShape()));
                    profileImg.setClipToOutline(true);
                    Glide.with(getApplicationContext()).load(url).into(profileImg);

                    Toast.makeText(getApplicationContext(), "프로필 이미지가 설정/변경되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}