package gachon.termproject.joker.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.activity.SeePostActivity;
import gachon.termproject.joker.container.PostContent;


public class MyInfoPostAdapter extends RecyclerView.Adapter<MyInfoPostAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PostContent> myInfoPostList;
    private int finishCount = 0;

    public MyInfoPostAdapter(Context context) {
        this.context = context;
        myInfoPostList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Posts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                snapshot.getRef().orderByChild("userId").equalTo(UserInfo.userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        for (DataSnapshot item : snapshot.getChildren()) {
                            PostContent myInfoPostContent = item.getValue(PostContent.class);
                            myInfoPostList.add(0, myInfoPostContent);
                        }
                        finishCount++;
                        if (finishCount == 3) {
                            notifyDataSetChanged();
                            finishCount = 0;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
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
        ArrayList<String> contentInPost;
        ArrayList<String> imagesInPost;
        ArrayList<Integer> orderInPost;

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
                    intent.putStringArrayListExtra("content", contentInPost);
                    intent.putStringArrayListExtra("images", imagesInPost);
                    intent.putIntegerArrayListExtra("order", orderInPost);
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myinfo_post, parent, false);
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
        holder.contentInPost = contentsList;
        holder.imagesInPost = imagesList;

        if (imagesList != null)
            Glide.with(context).load(imagesList.get(0)).override(1000).thumbnail(0.1f).into(holder.imageView);
        else
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() { return myInfoPostList.size(); }
}

