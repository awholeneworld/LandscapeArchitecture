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
import android.widget.ImageButton;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;

public class SettingMyInfoActivity extends AppCompatActivity {
    private CheckBox SU, IC, DJ, GJ, DG, US, BS, JJ, GG, GW, CB, CN, GB, GN, JB, JN, SJ;
    private StorageReference storageReference;
    private List<String> locationSelected;
    private List<String> location;
    private ImageView profileImg;
    private EditText nickname;
    private Uri file;
    int flag_nickname_check = 0;
    int flag_location = 0;
    int flag_message = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_myinfo);

        profileImg = findViewById(R.id.profileImage);
        TextView profileImgSet = findViewById(R.id.setting_image);
        EditText email = findViewById(R.id.setting_email);
        nickname = findViewById(R.id.setting_nickname);
        EditText introMsg = findViewById(R.id.setting_message);
        Button checkNickname = findViewById(R.id.duplicateCheck);
        Button save = findViewById(R.id.setting_save_button);
        Button resetPwd = findViewById(R.id.setting_password_button);
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

        // 프사 설정
        if (!UserInfo.profileImg.equals("None"))
            Glide.with(getApplicationContext()).load(UserInfo.profileImg).into(profileImg);

        // 이메일 설정
        email.setText(UserInfo.email);
        email.setTextColor(Color.parseColor("#000000"));

        // 닉네임 설정
        nickname.setText(UserInfo.nickname);

        // 중복확인 버튼 잠그기
        checkNickname.setEnabled(false);

        // 한줄 메시지 설정
        introMsg.setText(UserInfo.introduction);

        // 지역 설정
        setLocation();

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
                flag_nickname_check = 0;
                nickname.setEnabled(true);
                checkNickname.setEnabled(true);
            }
        });

        // 닉네임 중복체크 버튼 눌렀을 때
        checkNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //닉네임을 입력받음
                String temp = nickname.getText().toString();

                if (temp.length() == 0) {
                    Toast.makeText(getApplicationContext(), "변경할 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    nickname.setText(UserInfo.nickname);
                    nickname.setEnabled(false);
                    checkNickname.setEnabled(false);
                }
                else if (temp.equals(UserInfo.nickname)) {
                    Toast.makeText(getApplicationContext(), "본인의 닉네임입니다.", Toast.LENGTH_SHORT).show();
                    nickname.setEnabled(false);
                    checkNickname.setEnabled(false);
                }
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
                                        Toast.makeText(getApplicationContext(), "사용 가능한 닉네임 입니다", Toast.LENGTH_SHORT).show();
                                        flag_nickname_check++;
                                        nickname.setEnabled(false);
                                        checkNickname.setEnabled(false);
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

        // 한줄 메시지 수정하려 할 때
        introMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                introMsg.setEnabled(true);
            }
        });

        // 저장 눌렀을 때
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                int finishCount = 0;

                // 닉네임
                if (checkNickname.isEnabled())
                    Toast.makeText(getApplicationContext(), "닉네임 중복 확인을 해주세요", Toast.LENGTH_SHORT).show();
                else if (flag_nickname_check == 0 || flag_nickname_check == 1)
                    finishCount++;

                // 지역
                locationSelected = checkLocation();
                if (locationSelected.isEmpty())
                    Toast.makeText(getApplicationContext(), "지역을 하나 이상 선택해주세요", Toast.LENGTH_SHORT).show();
                else if (!locationSelected.isEmpty()) { // 체크 되어있으면
                    if (!location.equals(locationSelected)) // 다르면
                        flag_location++;

                    finishCount++;
                }

                // 한줄 메시지
                if (introMsg.getText().toString().equals(UserInfo.introduction))
                    finishCount++;
                else if (!introMsg.getText().toString().equals(UserInfo.introduction)) {
                    flag_message++;
                    finishCount++;
                }

                // 저장 경우 8가지
                if (finishCount == 3 && flag_nickname_check == 0 && flag_location == 0 && flag_message == 0) {
                    Toast.makeText(getApplicationContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (finishCount == 3 && flag_nickname_check == 0 && flag_location == 0 && flag_message == 1) {
                    FirebaseFirestore.getInstance().collection("users").document(UserInfo.userId).update("introduction", introMsg.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } else if (finishCount == 3 && flag_nickname_check == 0 && flag_location == 1 && flag_message == 0) {
                    FirebaseFirestore.getInstance().collection("users").document(UserInfo.userId).update("location", locationSelected).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } else if (finishCount == 3 && flag_nickname_check == 0 && flag_location == 1 && flag_message == 1) {

                } else if (finishCount == 3 && flag_nickname_check == 1 && flag_location == 0 && flag_message == 0) {
                    FirebaseFirestore.getInstance().collection("users").document(UserInfo.userId).update("nickname", nickname.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } else if (finishCount == 3 && flag_nickname_check == 1 && flag_location == 0 && flag_message == 1) {

                } else if (finishCount == 3 && flag_nickname_check == 1 && flag_location == 1 && flag_message == 0) {

                } else if (finishCount == 3 && flag_nickname_check == 1 && flag_location == 1 && flag_message == 1) {

                }
            }
        });

        // 비밀번호 변경 눌렀을 때
        resetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CheckPasswordActivity.class));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && data != null && data.getData() != null) { // 이미지 파일 선택하였다면
            file = data.getData();
            uploadProfileImage();
        }
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

    public void setLocation() {
        location = UserInfo.location;

        if (location.contains("서울")) SU.setChecked(true);
        if (location.contains("인천")) IC.setChecked(true);
        if (location.contains("대전")) DJ.setChecked(true);
        if (location.contains("광주")) GJ.setChecked(true);
        if (location.contains("대구")) DG.setChecked(true);
        if (location.contains("울산")) US.setChecked(true);
        if (location.contains("부산")) BS.setChecked(true);
        if (location.contains("제주")) JJ.setChecked(true);
        if (location.contains("경기")) GG.setChecked(true);
        if (location.contains("강원")) GW.setChecked(true);
        if (location.contains("충북")) CB.setChecked(true);
        if (location.contains("충남")) CN.setChecked(true);
        if (location.contains("경북")) GB.setChecked(true);
        if (location.contains("경남")) GN.setChecked(true);
        if (location.contains("전북")) JB.setChecked(true);
        if (location.contains("전남")) JN.setChecked(true);
        if (location.contains("세종")) SJ.setChecked(true);
    }

    public List<String> checkLocation() {
        //선택된 지역을 저장할 리스트
        List<String> location = new ArrayList<>();

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
