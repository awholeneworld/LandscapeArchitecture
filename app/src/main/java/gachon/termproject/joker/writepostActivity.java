package gachon.termproject.joker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

public class writepostActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;
    private Toolbar mToolbar;
    private Uri file; // 파일 담는 그릇
    LinearLayout ImageContainer;

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

        ImageButton imageAddButton = findViewById(R.id.writepost_imageAddButton);
        Button register = findViewById(R.id.writepost_assign);

        ImageContainer = (LinearLayout)findViewById(R.id.writepost_imageArea);

        // 파일 선택
        imageAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });

        // 게시글 등록!
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"게시글등록.",Toast.LENGTH_SHORT).show();
            }
        });

    }


    //파일선택
    private void selectFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"파일을 선택하세요."),0);
    }


    //이미지 동적 생성
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK && data!= null && data.getData()!= null) {
            file = data.getData(); // 파일 가져옴

            // 이미지뷰를 생성하고 초기화한다.
            ImageView showImage = new ImageView(getBaseContext());
            showImage.setImageURI(file);
            ImageContainer.addView(showImage);
        }
    }

}