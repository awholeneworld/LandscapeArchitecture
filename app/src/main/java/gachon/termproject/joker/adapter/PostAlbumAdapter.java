package gachon.termproject.joker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gachon.termproject.joker.R;
import gachon.termproject.joker.container.PostMyInfoContent;

public class PostAlbumAdapter extends RecyclerView.Adapter<PostAlbumAdapter.ViewHolder> {

    private ArrayList<PostAlbumContent> dataSet;

    public PostAlbumAdapter(ArrayList<PostAlbumContent> dataSet) {
        this.dataSet = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content_album_community, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(dataSet.get(position).img);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}