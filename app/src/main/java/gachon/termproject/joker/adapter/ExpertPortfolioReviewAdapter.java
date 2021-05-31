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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import gachon.termproject.joker.R;
import gachon.termproject.joker.Content.PostContent;
import gachon.termproject.joker.activity.SeePostActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ExpertPortfolioReviewAdapter extends RecyclerView.Adapter<ExpertPortfolioReviewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PostContent> contentList;

    public ExpertPortfolioReviewAdapter(Context context, String selectedExpertId, TextView numberView) {
        this.context = context;
        contentList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Posts/review").orderByChild("expertId").equalTo(selectedExpertId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                contentList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    PostContent content = item.getValue(PostContent.class);
                    contentList.add(0, content);
                }
                numberView.setText(String.valueOf(contentList.size()));
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        ImageView image;
        String categoryOfPost;
        String userIdInPost;
        String profileImgInPost;
        String titleInPost;
        String nicknameInPost;
        String timeInPost;
        String postIdInPost;
        String pushToken;
        String expertIdOfPost;
        String intro;
        ArrayList<String> contentInPost;
        ArrayList<String> imagesInPost;
        ArrayList<String> location;
        PostContent postContent;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.review_image);
            content = itemView.findViewById(R.id.review_text);

            image.setOnClickListener(new View.OnClickListener() {
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
                    intent.putExtra("pushToken", pushToken);
                    intent.putExtra("expertId", expertIdOfPost);
                    intent.putExtra("intro", intro);
                    intent.putStringArrayListExtra("content", contentInPost);
                    intent.putStringArrayListExtra("images", imagesInPost);
                    intent.putStringArrayListExtra("location", location);
                    intent.putExtra("postContent", postContent);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public ExpertPortfolioReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view_expert_review, parent, false);
        return new ExpertPortfolioReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpertPortfolioReviewAdapter.ViewHolder holder, int position) {
        PostContent content = contentList.get(position);

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
        holder.pushToken = content.getPushToken();
        holder.intro = content.getIntro();
        holder.location = content.getLocation();
        holder.postContent = content;

        if (imagesList != null)
            Glide.with(context).load(imagesList.get(0)).override(1000).thumbnail(0.1f).into(holder.image);
        if (contentsList.get(0).length() > 30)
            holder.content.setText(contentsList.get(0).substring(0, 30));
        else
            holder.content.setText(contentsList.get(0));
    }

    @Override // DataSet 크기 계산
    public int getItemCount() {
        return contentList.size();
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
