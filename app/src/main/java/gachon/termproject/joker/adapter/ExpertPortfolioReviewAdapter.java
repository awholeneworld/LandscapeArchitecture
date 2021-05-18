package gachon.termproject.joker.adapter;

import android.content.Context;
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
import gachon.termproject.joker.container.PostContent;

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.review_image);
            textView = itemView.findViewById(R.id.review_text);
        }
    }

    @NonNull
    @Override
    public ExpertPortfolioReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view_portfolio, parent, false);
        return new ExpertPortfolioReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpertPortfolioReviewAdapter.ViewHolder holder, int position) {
        PostContent content = contentList.get(position);

        ArrayList<String> imagesList = content.getImages();
        ArrayList<String> contentsList = content.getContent();

        if (imagesList != null)
            Glide.with(context).load(imagesList.get(0)).override(1000).thumbnail(0.1f).into(holder.imageView);
        if (contentsList.get(0).length() > 30)
            holder.textView.setText(contentsList.get(0).substring(0, 30));
        else
            holder.textView.setText(contentsList.get(0));
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
