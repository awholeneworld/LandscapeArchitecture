package gachon.termproject.joker.adapter;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import gachon.termproject.joker.R;

public class PostImageSee extends RelativeLayout {

    public PostImageSee(Context context, Uri image){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.post_see_image_view, this);


        ImageView iv = findViewById(R.id.post_see_image);
        iv.setImageURI(image);


    }
}
