package gachon.termproject.joker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gachon.termproject.joker.R;

public class MyInfoCommentTabAdapter extends RecyclerView.Adapter<MyInfoCommentTabAdapter.ViewHolder> {
    private ArrayList<String> dataSet;

    public MyInfoCommentTabAdapter() { this.dataSet = dataSet; }

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_myinfo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
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
