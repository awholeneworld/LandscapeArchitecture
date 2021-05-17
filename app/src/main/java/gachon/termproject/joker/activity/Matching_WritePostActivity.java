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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.List;
import java.util.Locale;

import gachon.termproject.joker.FirebaseHelper;
import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.PostImage;
import gachon.termproject.joker.container.PostContent;

public class Matching_WritePostActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseHelper firebaseHelper = new FirebaseHelper(this);
    private PostContent postContent;
    private Uri image; // 이미지 저장 변수
    private ArrayList<String> contentList = new ArrayList<>();
    private ArrayList<String> imagesNumber = new ArrayList<>();
    private ArrayList<Uri> imagesList = new ArrayList<>();
    private String userId = UserInfo.userId; // 누가 업로드 했는지 알기 위함
    private String nickname = UserInfo.nickname;
    private String postId;
    private int postTime;
    private String expertId;
    private LinearLayout layout;
    private EditText title, content;
    private TextView location_select;
    private ImageButton imageAddButton;
    private ArrayList<String> imagesUrl = new ArrayList<>();
    private int uploadFinishCount = 0;


    LinearLayout location_table;
    private Button register;

    private boolean is_location = false;
    List<String> locationSelected;

    public static List<String> location; // 회원가입을 위한 전역변수(전문가 회원가입을 위해 static으로 설정)
    private CheckBox SU, IC, DJ, GJ, DG, US, BS, JJ, GG, GW, CB, CN, GB, GN, JB, JN;


    //해야 할 일!
//    1. 게시글 저장에 선택 지역 반영하기!
//    2. 이건 유저별로 저장되어야 하는? 글임 => 그니까 이 글은 해당 일반인 유저랑 전문가들밖에 볼수없음
    // => DB저장 생각하면서 짜기


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_user_view_write_post);

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


        //===============> matching에서만 추가된 부분!!!=========================
        // 지역을 선택하는 부분입니다!!!!
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

        //지역 선택 글씨
        location_select = findViewById(R.id.writepost_region);

        //지역선택 버튼을 누르게 되면
        location_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_location = true;

                //지역선택하는 화면이 있는 relative layout -> 선택지를 화면 내로 끌고와서 보여줌
                LinearLayout LL = findViewById(R.id.post_select_location);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) LL.getLayoutParams();
                lp.addRule(RelativeLayout.ABOVE, R.id.layout_post_bottom);
                lp.addRule(RelativeLayout.BELOW, 0);
                LL.setLayoutParams(lp);

                //잠시 안보이게 가려두기 & 버튼 이름 바꿔주기
                ImageButton imbnt = findViewById(R.id.writepost_imageAddButton);
                imbnt.setVisibility(View.INVISIBLE);
                location_select.setVisibility(View.INVISIBLE);
                register.setText("완료");

            }
        });




        // 게시글 등록!
        register.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(is_location){ //location이 켜져있을 때는 (지역 선택)
                    //지금의 이 버튼은 완료 버튼!
                    //표에서 뭐뭐 선택했는지 얻어내서
                    //대충 어딘가 저장ㅇ해두고
                    //지역선택 text를 만약 선택한 갯수로 나타내자,,,, "n개 지역"
                   locationSelected = checklocation();

                   if(locationSelected.isEmpty()){
                       location_select.setText("지역선택");
                   }
                   else{
                       location_select.setText(locationSelected.size() + "개 지역");
                   }


                    //그리고 숨겼던 사진등록이랑 지역선택 글씨를 보이게 하고
                    //지역선택 뷰를 다시 밑으로 내립니다.
                    LinearLayout LL = findViewById(R.id.post_select_location);
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) LL.getLayoutParams();
                    lp.addRule(RelativeLayout.BELOW, R.id.layout_post_bottom);
                    lp.addRule(RelativeLayout.ABOVE, 0);
                    LL.setLayoutParams(lp);

                    ImageButton imbnt = findViewById(R.id.writepost_imageAddButton);
                    imbnt.setVisibility(View.VISIBLE);
                    location_select.setVisibility(View.VISIBLE);
                    register.setText("등록");
                    is_location = false;

                }
                else{
                    //지금의 이 버튼은 등록 버튼!
                    if (title.length() > 0 && content.length() > 0) {
                        post(category);
                        setResult(RESULT_OK, new Intent()); // 게시판에게 완료됐다는 신호 보내기
                        finish();
                    } else if (title.length() <= 0){
                        Toast.makeText(getApplicationContext(), "제목을 최소 1자 이상 써주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "내용을 최소 1자 이상 써주세요.", Toast.LENGTH_SHORT).show();
                    }

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

            PostImage postimage = new PostImage(Matching_WritePostActivity.this, image, layoutParams);


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
        // 포스트 고유 아이디
        postId = String.valueOf(System.currentTimeMillis());

        //글넣기
        String text = ((EditText) findViewById(R.id.writepost_content)).getText().toString();
        contentList.add(text);

        if(imagesList.size() == 0){// 사진이 없다면? 바로 글쓰기
            // 포스트 시간 설정
            Date currentTime = new Date();
            String updateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault()).format(currentTime);

            // 포스트할 내용
            postContent = new PostContent(category, userId, UserInfo.profileImg, title.getText().toString(), nickname, updateTime, postId, contentList, imagesUrl, expertId);

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

        //사진이 있다면 그림넣기
        for(int i=0;i<imagesList.size();i++){
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
                                postContent = new PostContent(category, userId, UserInfo.profileImg, title.getText().toString(), nickname, updateTime, postId, contentList, imagesUrl, expertId);

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

                            Toast.makeText(Matching_WritePostActivity.this, "등록중 입니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (NullPointerException e) {
                Toast.makeText(Matching_WritePostActivity.this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static int dpToPx(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
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

        return location;
    }
}