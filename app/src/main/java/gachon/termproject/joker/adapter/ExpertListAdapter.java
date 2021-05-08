package gachon.termproject.joker.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import gachon.termproject.joker.R;
import gachon.termproject.joker.activity.ChatActivity;
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
        Button chatBtn;
        String expertUserId;
        String expertNickname;
        String expertProfileImg;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        ViewHolder(View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.expert_nickname);
            profileImg = itemView.findViewById(R.id.expert_image);
            chatBtn = itemView.findViewById(R.id.chat);

            profileImg.setBackground(new ShapeDrawable(new OvalShape()));
            profileImg.setClipToOutline(true);

            chatBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("userId", expertUserId);
                    intent.putExtra("nickname", expertNickname);
                    intent.putExtra("profileImg", expertProfileImg);
                    context.startActivity(intent);
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content_expert_list_matching, parent,false);

        return new ViewHolder(view);
    }

    @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExpertListContent content = expertList.get(position);

        String contentUserId = content.getUserId();
        String contentNickname = content.getNickname();
        String contentProfileImg = content.getProfileImg();

        holder.expertUserId = contentUserId;
        holder.expertNickname = contentNickname;
        holder.expertProfileImg = contentProfileImg;

        holder.nickname.setText(contentNickname);
        if (!contentProfileImg.equals("None"))
            Glide.with(context).load(contentProfileImg).override(1000).thumbnail(0.1f).into(holder.profileImg);
    }

    @Override
    public int getItemCount() {
        return expertList.size();
    }
}
