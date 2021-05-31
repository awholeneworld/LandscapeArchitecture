package gachon.termproject.joker.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gachon.termproject.joker.R;
import gachon.termproject.joker.activity.SeePostActivity;
import gachon.termproject.joker.Content.PostContent;

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.ViewHolder>
{
    private Context context;
    ArrayList<PostContent> postContentList;

    public HomePostAdapter(Context context, ArrayList<PostContent> postContentList)
    {
        this.context = context;
        this.postContentList = postContentList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView title;
        TextView nickname;
        TextView date;
        TextView content;
        ImageView image;
        String categoryOfPost;
        String userIdInPost;
        String profileImgInPost;
        String titleInPost;
        String nicknameInPost;
        String timeInPost;
        String postIdInPost;
        String expertIdOfPost;
        String pushToken;
        ArrayList<String> contentInPost;
        ArrayList<String> imagesInPost;
        ArrayList<String> locationOfUser;


        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            nickname = itemView.findViewById(R.id.writer);
            date = itemView.findViewById(R.id.date);
            image = itemView.findViewById(R.id.image);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SeePostActivity.class);
                    intent.putExtra("category", categoryOfPost);
                    intent.putExtra("userId", userIdInPost);
                    intent.putExtra("profileImg", profileImgInPost);
                    intent.putExtra("title", titleInPost);
                    intent.putExtra("nickname", nicknameInPost);
                    intent.putExtra("time", timeInPost);
                    intent.putExtra("postId", postIdInPost);
                    intent.putExtra("expertId", expertIdOfPost);
                    intent.putExtra("pushToken", pushToken);
                    intent.putStringArrayListExtra("content", contentInPost);
                    intent.putStringArrayListExtra("images", imagesInPost);
                    intent.putStringArrayListExtra("location", locationOfUser);

                    context.startActivity(intent);
                }
            });
        }
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_list, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostContent content = postContentList.get(position);
        String contentTitle = content.getTitle();
        String contentNickname = content.getNickname();
        String contentTime = content.getPostTime();
        ArrayList<String> contentsList = content.getContent();
        ArrayList<String> imagesList = content.getImages();

        // 뷰홀더 클래스의 전역 변수 설정
        holder.titleInPost = contentTitle;
        holder.categoryOfPost = content.getCategory();
        holder.userIdInPost = content.getUserId();
        holder.profileImgInPost = content.getProfileImg();
        holder.nicknameInPost = contentNickname;
        holder.timeInPost = contentTime;
        holder.postIdInPost = content.getPostId();
        holder.pushToken = content.getPushToken();
        holder.contentInPost = contentsList;
        holder.imagesInPost = imagesList;
        holder.expertIdOfPost = content.getExpertId();
        holder.locationOfUser = content.getLocation();

        // 목록에 나타나는 글의 제목, 작성자, 작성 시간 표시
        if (contentTitle.length() > 15)
            holder.title.setText(contentTitle.substring(0, 16));
        else
            holder.title.setText(contentTitle);
    }

    @Override
    public int getItemCount() {
        return postContentList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}