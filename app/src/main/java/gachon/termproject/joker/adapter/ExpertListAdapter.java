package gachon.termproject.joker.adapter;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import gachon.termproject.joker.R;
import gachon.termproject.joker.container.ExpertListContent;

public class ExpertListAdapter extends RecyclerView.Adapter<ExpertListAdapter.ViewHolder>{
    private Context context;
    private ArrayList<ExpertListContent> expertList;

    public ExpertListAdapter(Context context, ArrayList<ExpertListContent> expertList) {
            this.context = context;
            this.expertList = expertList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView nickname;
        ImageView profileImg;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        ViewHolder(View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.expert_nickname);
            profileImg = itemView.findViewById(R.id.expert_image);
            profileImg.setBackground(new ShapeDrawable(new OvalShape()));
            profileImg.setClipToOutline(true);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        // 나중에 누르면 전문가 포트폴리오 페이지로 이동
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_content_expert_list_matching, viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ExpertListContent content = expertList.get(position);

        String contentNickname = content.getNickname();
        String contentProfileImg = content.getProfileImg();

        viewHolder.nickname.setText(contentNickname);
        if (!contentProfileImg.equals("None"))
            Glide.with(context).load(contentProfileImg).override(1000).thumbnail(0.1f).into(viewHolder.profileImg);
    }

    @Override
    public int getItemCount() {
        return expertList.size();
    }
}
