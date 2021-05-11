package gachon.termproject.joker.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import gachon.termproject.joker.R;
import gachon.termproject.joker.container.CommentMyInfoContent;

public class MyInfoCommentAdapter extends RecyclerView.Adapter<MyInfoCommentAdapter.ViewHolder> {

    private ArrayList<CommentMyInfoContent> dataSet;

    public MyInfoCommentAdapter(ArrayList<CommentMyInfoContent> dataSet) { this.dataSet = dataSet; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView comment;
        public TextView postTitle;
        String commentInComment;
        String postTitleInComment;

        public ViewHolder(View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment);
            postTitle = itemView.findViewById(R.id.postTitle);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myinfo_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentMyInfoContent content = dataSet.get(position);
        holder.commentInComment = content.getComment();
        holder.postTitleInComment = content.getPostTitle();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
