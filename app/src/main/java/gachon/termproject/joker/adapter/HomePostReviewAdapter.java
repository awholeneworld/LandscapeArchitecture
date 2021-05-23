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

import gachon.termproject.joker.Content.PostContent;
import gachon.termproject.joker.FirebaseHelper;
import gachon.termproject.joker.OnPostListener;
import gachon.termproject.joker.R;
import gachon.termproject.joker.activity.SeePostActivity;

public class HomePostReviewAdapter extends RecyclerView.Adapter<HomePostReviewAdapter.ViewHolder>
{
    private Context context;
    private FirebaseHelper firebaseHelper;
    ArrayList<PostContent> postContentList;

    public HomePostReviewAdapter(Context context, ArrayList<PostContent> postContentList)
    {
        this.context = context;
        this.postContentList = postContentList;
    }

    public void setOnPostListener(OnPostListener onPostListener){
        firebaseHelper.setOnPostListener(onPostListener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView title;
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
            title = itemView.findViewById(R.id.post_name);
            image = itemView.findViewById(R.id.review_image);

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_expert_review_list, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostContent content = postContentList.get(position);
        String contentTitle = content.getTitle();
        ArrayList<String> imagesList = content.getImages();



        // 뷰홀더 클래스의 전역 변수 설정
        holder.titleInPost = contentTitle;

        Glide.with(context).load(imagesList.get(0)).override(1000).thumbnail(0.1f).centerCrop() .into(holder.image);

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