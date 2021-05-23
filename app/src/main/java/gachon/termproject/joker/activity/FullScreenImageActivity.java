package gachon.termproject.joker.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import gachon.termproject.joker.R;

public class FullScreenImageActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullscreen_image);

        Intent intent = getIntent();
        String img = intent.getStringExtra("img");

        ImageView iv = findViewById(R.id.fullscreen_image);
        Glide.with(FullScreenImageActivity.this).load(img).into(iv);

        ImageView btn = findViewById(R.id.btn_back);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                finish();
            } });


    }

}