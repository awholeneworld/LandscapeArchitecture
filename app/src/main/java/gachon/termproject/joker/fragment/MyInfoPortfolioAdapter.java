package gachon.termproject.joker.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gachon.termproject.joker.R;
import gachon.termproject.joker.container.PortfolioMyInfoContent;

public class MyInfoPortfolioAdapter  extends RecyclerView.Adapter<MyInfoPortfolioAdapter.ViewHolder> {

    private ArrayList<PortfolioMyInfoContent> dataSet;

    public MyInfoPortfolioAdapter(ArrayList<PortfolioMyInfoContent> dataSet) {
        this.dataSet = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mimageView;
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mimageView = itemView.findViewById(R.id.review_image);
            mTextView = itemView.findViewById(R.id.review_text);
        }
    }

    @NonNull
    @Override
    public MyInfoPortfolioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myinfo_portfolio, parent, false);
        return new MyInfoPortfolioAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyInfoPortfolioAdapter.ViewHolder holder, int position) {
        holder.mimageView.setImageResource(dataSet.get(position).image);
        holder.mTextView.setText(dataSet.get(position).reviewStr); ///////// 다시 확인
    }

    @Override // DataSet 크기 계산
    public int getItemCount() {
        return dataSet.size();
    }
}
