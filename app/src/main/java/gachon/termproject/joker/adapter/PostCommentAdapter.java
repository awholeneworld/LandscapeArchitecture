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

import gachon.termproject.joker.FirebaseHelper;
import gachon.termproject.joker.OnPostListener;
import gachon.termproject.joker.container.PostCommentContent;
import gachon.termproject.joker.R;

public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.CommentViewHolder>
{
    private Context context;
    private FirebaseHelper firebaseHelper;
    ArrayList<PostCommentContent> postCommentList;

    public PostCommentAdapter(Context context, ArrayList<PostCommentContent> postCommentList)
    {
        this.context = context;
        this.postCommentList = postCommentList;
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

        CommentViewHolder(View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.comment_img);
            nickname = itemView.findViewById(R.id.comment_nickname);
            // date = itemView.findViewById(R.id.date);
            content = itemView.findViewById(R.id.comment_content);

            // 땡떙떙 버튼 클릭시
            itemView.findViewById(R.id.comment_movevert).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_1_listview, viewGroup,false);
        CommentViewHolder viewHolder = new CommentViewHolder(view);

        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder viewHolder, int position) {
        PostCommentContent commentContent = postCommentList.get(position);

        String commentProfileImg = commentContent.getProfileImg();
        String commentNickname = commentContent.getNickname();
        String commentTime = commentContent.getCommentTime();
        String comment = commentContent.getContent();

        viewHolder.categoryOfComment = commentContent.getCategory();
        viewHolder.userIdInComment = commentContent.getUserId();
        viewHolder.nicknameInComment = commentNickname;
        viewHolder.timeInComment = commentTime;
        viewHolder.comment = comment;

        // 댓글의 작성자, 작성 시간, 내용 표시
        viewHolder.nickname.setText(commentNickname);
        // viewHolder.date.setText(commentTime);
        viewHolder.content.setText(comment);

        // 댓글의 작성자 프로필 사진 표시
        viewHolder.profileImg.setBackground(new ShapeDrawable(new OvalShape()));
        viewHolder.profileImg.setClipToOutline(true);
        if (!commentProfileImg.equals("None"))
            Glide.with(context).load(commentProfileImg).into(viewHolder.profileImg);
    }

    @Override
    public int getItemCount() {
        return postCommentList.size();
    }
}