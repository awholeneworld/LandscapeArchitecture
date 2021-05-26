package gachon.termproject.joker.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuInflater;
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

import gachon.termproject.joker.FirebaseHelper;
import gachon.termproject.joker.OnPostListener;
import gachon.termproject.joker.Content.PostCommentContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;

public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.CommentViewHolder>
{
    private Context context;
    ViewGroup parent;
    private FirebaseHelper firebaseHelper;
    ArrayList<PostCommentContent> postCommentList;
    private DatabaseReference databaseReference;


    public PostCommentAdapter(Context context, ArrayList<PostCommentContent> postCommentList, DatabaseReference databaseReference)
    {
        this.context = context;
        this.postCommentList = postCommentList;
        this.databaseReference = databaseReference;
    }

    public void setOnPostListener(OnPostListener onPostListener){
        firebaseHelper.setOnPostListener(onPostListener);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder  {
        ImageView profileImg;
        TextView nickname;
        TextView date;
        TextView content;
        String categoryOfComment;
        String userIdInComment;
        String nicknameInComment;
        String timeInComment;
        String comment;
        Button btn;

        CommentViewHolder(View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.comment_img);
            nickname = itemView.findViewById(R.id.comment_nickname);
            date = itemView.findViewById(R.id.comment_writetime);
            content = itemView.findViewById(R.id.comment_content);
            btn = itemView.findViewById(R.id.comment_movevert);
        }
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_1_comment_see_post, parent,false);

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
        holder.nicknameInComment = commentNickname;
        holder.timeInComment = commentTime;
        holder.comment = comment;

        // 댓글의 작성자, 작성 시간, 내용 표시
        holder.nickname.setText(commentNickname);
        holder.date.setText(commentTime);
        holder.content.setText(comment);

        // 댓글의 작성자 프로필 사진 표시
        holder.profileImg.setBackground(new ShapeDrawable(new OvalShape()));
        holder.profileImg.setClipToOutline(true);
        if (!commentProfileImg.equals("None"))
            Glide.with(context).load(commentProfileImg).into(holder.profileImg);

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup= new PopupMenu(parent.getContext(), view);//v는 클릭된 뷰를 의미

                //inflating menu from xml resource
                if (UserInfo.userId.equals(holder.userIdInComment))
                    popup.inflate(R.menu.my_post_menu);
                else
                    popup.inflate(R.menu.others_post_menu);

                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.show_profile:
                                //프로필보기
                                Toast.makeText(context.getApplicationContext(),"프로필보기",Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.decelerate:
                                //신고
                                Toast.makeText(context.getApplicationContext(),"신고되었습니다.", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.delete:
                                firebaseHelper.commentDelete(databaseReference, commentId);
                                Toast.makeText(context.getApplicationContext(),"삭제되었습니다.", Toast.LENGTH_SHORT).show();
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