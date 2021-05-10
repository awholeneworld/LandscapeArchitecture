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


public class MyInfoPostAdapter extends RecyclerView.Adapter<MyInfoPostAdapter.ViewHolder> {

    private ArrayList<PostMyInfoContent> dataSet;

    public MyInfoPostAdapter(ArrayList<PostMyInfoContent> dataSet) {
        this.dataSet = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mimageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mimageView = itemView.findViewById(R.id.image);
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
        holder.mimageView.setImageResource(dataSet.get(position).img);
    }

    @Override // DataSet 크기 계산
    public int getItemCount() {
        return dataSet.size();
    }
}

