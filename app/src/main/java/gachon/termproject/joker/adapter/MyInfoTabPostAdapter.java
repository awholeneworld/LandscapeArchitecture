package gachon.termproject.joker.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import gachon.termproject.joker.R;
import gachon.termproject.joker.activity.SeePostActivity;
import gachon.termproject.joker.Content.PostContent;

public class MyInfoTabPostAdapter extends RecyclerView.Adapter<MyInfoTabPostAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PostContent> myInfoPostList;

    public MyInfoTabPostAdapter(Context context, ArrayList<PostContent> myInfoPostList) {
        this.context = context;
        this.myInfoPostList = myInfoPostList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        String categoryOfPost;
        String userIdInPost;
        String profileImgInPost;
        String titleInPost;
        String nicknameInPost;
        String timeInPost;
        String postIdInPost;
        String expertIdOfPost;
        String pushToken;
        String intro;
        ArrayList<String> contentInPost;
        ArrayList<String> imagesInPost;
        ArrayList<String> locationOfUser;
        PostContent postContent;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
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
                    intent.putExtra("intro", intro);
                    intent.putStringArrayListExtra("content", contentInPost);
                    intent.putStringArrayListExtra("images", imagesInPost);
                    intent.putStringArrayListExtra("location", locationOfUser);
                    intent.putExtra("postContent", postContent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myinfo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostContent content = myInfoPostList.get(position);

        String contentTitle = content.getTitle();
        String contentNickname = content.getNickname();
        String contentTime = content.getPostTime();
        ArrayList<String> contentsList = content.getContent();
        ArrayList<String> imagesList = content.getImages();

        // 뷰홀더 클래스의 전역 변수 설정
        holder.categoryOfPost = content.getCategory();
        holder.userIdInPost = content.getUserId();
        holder.profileImgInPost = content.getProfileImg();
        holder.titleInPost = contentTitle;
        holder.nicknameInPost = contentNickname;
        holder.timeInPost = contentTime;
        holder.postIdInPost = content.getPostId();
        holder.expertIdOfPost = content.getExpertId();
        holder.pushToken = content.getPushToken();
        holder.contentInPost = contentsList;
        holder.imagesInPost = imagesList;
        holder.intro = content.getIntro();
        holder.locationOfUser = content.getLocation();
        holder.postContent = content;

        if (imagesList != null)
            Glide.with(context).load(imagesList.get(0)).override(1000).thumbnail(0.1f).into(holder.imageView);
    }

    @Override
    public int getItemCount() { return myInfoPostList.size(); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
