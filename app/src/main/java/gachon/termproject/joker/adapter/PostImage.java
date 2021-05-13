package gachon.termproject.joker.adapter;


import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.makeramen.RoundedImageView;
import gachon.termproject.joker.R;

public class PostImage extends RelativeLayout {

    ImageView img;
    ImageButton btn;

    public PostImage(Context context, Uri image, RelativeLayout.LayoutParams layoutParams){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.post_write_image_view, this);

        // 이미지뷰를 생성하고 초기화한다.
        ImageView imageView = findViewById(R.id.writeImage);
        imageView.setLayoutParams(layoutParams);
        Glide.with(this).load(image).into(imageView);


        btn = findViewById(R.id.writeImageButton);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

    }
}
