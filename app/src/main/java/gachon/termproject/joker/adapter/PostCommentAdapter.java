package gachon.termproject.joker.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import gachon.termproject.joker.FirebaseDeleter;
import gachon.termproject.joker.Content.PostCommentContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.activity.SeeProfileActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.CommentViewHolder>
{
    private Context context;
    ViewGroup parent;
    private FirebaseDeleter firebaseDeleter;
    ArrayList<PostCommentContent> postCommentList;
    private DatabaseReference databaseReference;


    public PostCommentAdapter(Context context, ArrayList<PostCommentContent> postCommentList, DatabaseReference databaseReference)
    {
        this.context = context;
        this.postCommentList = postCommentList;
        this.databaseReference = databaseReference;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder  {
        ImageView profileImg;
        TextView nickname;
        TextView date;
        TextView content;
        String commentId;
        String categoryOfComment;
        String userIdInComment;
        String nicknameInComment;
        String profileImgInComment;
        String timeInComment;
        String comment;
        String intro;
        String pushToken;
        ArrayList<String> location;
        Button btn;

        CommentViewHolder(View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.comment_img);
            nickname = itemView.findViewById(R.id.comment_nickname);
            date = itemView.findViewById(R.id.comment_writetime);
            content = itemView.findViewById(R.id.comment_content);
            btn = itemView.findViewById(R.id.comment_movevert);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    PopupMenu popup= new PopupMenu(parent.getContext(), view);//v는 클릭된 뷰를 의미

                    //inflating menu from xml resource
                    if (UserInfo.getUserId().equals(userIdInComment))
                        popup.inflate(R.menu.my_post_menu);
                    else
                        popup.inflate(R.menu.others_post_menu);

                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.show_profile:
                                    Intent intent = new Intent(context, SeeProfileActivity.class);
                                    intent.putExtra("userId", userIdInComment);
                                    intent.putExtra("nickname", nicknameInComment);
                                    intent.putExtra("profileImg", profileImgInComment);
                                    intent.putExtra("intro", intro);
                                    intent.putExtra("pushToken", pushToken);
                                    intent.putStringArrayListExtra("location", location);
                                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                    break;
                                case R.id.decelerate:
                                    Toast.makeText(context.getApplicationContext(), nicknameInComment + "(이)가 신고되었습니다.", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.delete:
                                    firebaseDeleter.commentDelete(databaseReference, commentId);
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });
        }
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_see_post, parent,false);

        return new CommentViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        PostCommentContent commentContent = postCommentList.get(position);

        String commentProfileImg = commentContent.getProfileImg();
        String commentNickname = commentContent.getNickname();
        String commentTime = commentContent.getCommentTime();
        String comment = commentContent.getContent();
        String commentId = commentContent.getCommentId();

        holder.categoryOfComment = commentContent.getCategory();
        holder.userIdInComment = commentContent.getUserId();
        holder.profileImgInComment = commentProfileImg;
        holder.nicknameInComment = commentNickname;
        holder.timeInComment = commentTime;
        holder.commentId = commentId;
        holder.comment = comment;
        holder.pushToken = commentContent.getPushToken();
        holder.intro = commentContent.getIntro();
        holder.location = commentContent.getLocation();

        // 댓글의 작성자, 작성 시간, 내용 표시
        holder.nickname.setText(commentNickname);
        holder.date.setText(commentTime);
        holder.content.setText(comment);

        // 댓글의 작성자 프로필 사진 표시
        holder.profileImg.setBackground(new ShapeDrawable(new OvalShape()));
        holder.profileImg.setClipToOutline(true);
        if (!commentProfileImg.equals("None"))
            Glide.with(context).load(commentProfileImg).into(holder.profileImg);
    }

    @Override
    public int getItemCount() {
        return postCommentList.size();
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