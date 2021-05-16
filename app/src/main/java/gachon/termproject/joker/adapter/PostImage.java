package gachon.termproject.joker.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import gachon.termproject.joker.R;

public class PostImage extends RelativeLayout {
    ImageView imgView;
    ImageButton btn;

    public ImageButton getBtn() {
        return btn;
    }

    public PostImage(Context context, Uri image, RelativeLayout.LayoutParams layoutParams){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.post_write_image_view, this);

        // 이미지뷰에 이미지 넣기.
        imgView = findViewById(R.id.writeImage);
        imgView.setLayoutParams(layoutParams);
        Glide.with(this).load(image).into(imgView);

        btn = findViewById(R.id.writeImageButton);
    }
}
