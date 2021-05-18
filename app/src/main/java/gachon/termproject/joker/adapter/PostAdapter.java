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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import gachon.termproject.joker.OnPostListener;
import gachon.termproject.joker.container.PostContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.activity.SeePostActivity;
import gachon.termproject.joker.FirebaseHelper;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>
{
    private Context context;
    private FirebaseHelper firebaseHelper;
    ArrayList<PostContent> postContentList;
    
    public PostAdapter(Context context, ArrayList<PostContent> postContentList)
    {
        this.context = context;
        this.postContentList = postContentList;
    }

    public void setOnPostListener(OnPostListener onPostListener){
        firebaseHelper.setOnPostListener(onPostListener);
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
        ArrayList<String> contentInPost;
        ArrayList<String> imagesInPost;
        ArrayList<String> locationPost;

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
                    intent.putStringArrayListExtra("content", contentInPost);
                    intent.putStringArrayListExtra("images", imagesInPost);
                    intent.putStringArrayListExtra("location", locationPost);
                    context.startActivity(intent);
                }
            });
        }
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community, parent,false);
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
        holder.categoryOfPost = content.getCategory();
        holder.userIdInPost = content.getUserId();
        holder.profileImgInPost = content.getProfileImg();
        holder.titleInPost = contentTitle;
        holder.nicknameInPost = contentNickname;
        holder.timeInPost = contentTime;
        holder.postIdInPost = content.getPostId();
        holder.contentInPost = contentsList;
        holder.imagesInPost = imagesList;
        holder.expertIdOfPost = content.getExpertId();
        holder.locationPost = content.getLocation();

        // 목록에 나타나는 글의 제목, 작성자, 작성 시간 표시
        if (contentTitle.length() > 15)
            holder.title.setText(contentTitle.substring(0, 16));
        else
            holder.title.setText(contentTitle);

        holder.nickname.setText(contentNickname);
        holder.date.setText(contentTime);

        // 목록에 나타나는 글의 내용 표시
        // 이미지 있을 시 첫번째 것 표시. 없을 시 표시 안함.
        int inputLetters = 0;
        int inputImage = 0;
        String contents = "";
        for (int i = 0; i < contentsList.size(); i++) {
            String writings = contentsList.get(i);

            if (writings.contains("\n")) {
                contents += writings.substring(0, writings.indexOf("\n")) + "...";
                inputLetters = 16;
            } else if (inputLetters <= 15 && writings.length() > 0){
                int writingsLength = writings.length();

                if (inputLetters + writingsLength > 15)
                    contents += (" " + writings.substring(0, 15 - inputLetters) + "...");
                else
                    contents += (" " + writings);

                inputLetters += writingsLength;
            } else if (inputImage == 0 && writings.length() == 0){
                Glide.with(context).load(imagesList.get(0)).override(1000).thumbnail(0.1f).into(holder.image);
                inputImage++;
            }
        }

        holder.content.setText(contents);
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