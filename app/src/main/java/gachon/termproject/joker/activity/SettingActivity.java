package gachon.termproject.joker.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gachon.termproject.joker.R;

public class SettingActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private ImageButton bell_screen;
    private ImageView setting_image;
    private TextView setting_changeImage;
    private EditText setting_email_text;
    private EditText setting_nickname_text;
    private Button setting_nickname_button;
    public static List<String> location; // 회원가입을 위한 전역변수(전문가 회원가입을 위해 static으로 설정)
    private CheckBox SU, IC, DJ, GJ, DG, US, BS, JJ, GG, GW, CB, CN, GB, GN, JB, JN, SJ;
    private EditText setting_message_text;
    private Button setting_save_button;
    private Button setting_password_button;
    private Button setting_logout_button;
    int flag_nickname_check = 0; // 닉네임이 중복되지 않을 때 (기본 상태)
    int flag_location = 0; // 기본상태
    int flag_message = 0; // 한줄메시지를 수정하지 않았을 때

    ActionBar actionBar;
    TextView action_bar_title;

    //////////////// 기존에 선택되어 있던 location 불러오는 부분 로직 넣어야해요!! //////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        bell_screen = (ImageButton)findViewById(R.id.bell_screen);
        bell_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "알림 설정 창으로!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), SettingBellActivity.class));
            }
        });

        setting_image = (ImageView)findViewById(R.id.setting_image);

        setting_changeImage = (TextView)findViewById(R.id.setting_changeImage);
        setting_changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 누르면 사진 변환할 수 있도록 해주기!
            }
        });

        setting_email_text = (EditText)findViewById(R.id.setting_email_text);
        // 자신의 기존 이메일 불러서 여기 넣어주세용
        setting_email_text.setText("기존의 이메일");
        setting_email_text.setTextColor(Color.parseColor("#000000"));

        setting_nickname_text = (EditText)findViewById(R.id.setting_nickname_text);
        // 자신의 기존 닉네임 불러서 여기 넣어주세용
        setting_nickname_text.setText("기존의 닉네임");
        setting_nickname_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 왜 여러번 눌러야 1로 바뀔까요 쩜쩜쩜...
                flag_nickname_check = 1;
            }
        });

        setting_nickname_button = (Button)findViewById(R.id.setting_nickname_button);
        setting_nickname_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "중복체크해주세요", Toast.LENGTH_SHORT).show();
                // 중복 체크하는 로직 넣어주세용

                // if (중복이라면) {
                // Toast.makeText(getApplicationContext(), "중복된 닉네임입니다", Toast.LENGTH_SHORT).show();
                // flag_nickname_check = 2; }
                // else if (중복이 아니라면) {
                flag_nickname_check = 3;
                // Toast.makeText(getApplicationContext(), "사용가능한 닉네임입니다", Toast.LENGTH_SHORT).show(); }

                // 마지막 설정 저장 부분에서 중복이라고 했는데 저장하는 경우 저장 안되도록 설정해놨습니당
            }
        });

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

        setting_message_text = (EditText)findViewById(R.id.setting_message_text);
        // 자신의 기존 닉네임 불러서 여기 넣어주세용
        setting_message_text.setText("기존의 한줄메시지");
        setting_message_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_message = 1;
            }
        });

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

        setting_password_button = (Button)findViewById(R.id.setting_password_button);
        setting_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "비밀번호 변경 창으로!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
                // 비밀번호 변경하는 창으로 넘어가도록!
            }
        });

        setting_logout_button = (Button)findViewById(R.id.setting_logout_button);
        setting_logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "로그아웃", Toast.LENGTH_SHORT).show();
                // 로그아웃 부분 넣어주세요
            }
        });

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

    /*
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(getApplicationContext(), SettingBellActivity.class));
        return super.onOptionsItemSelected(item);
    }
     */

}