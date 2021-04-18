package gachon.termproject.joker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>
{
    private Context context;
    ArrayList<PostContent> postContentList;
    
    public PostAdapter(Context context, ArrayList<PostContent> postContentList)
    {
        this.context = context;
        this.postContentList = postContentList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView title;
        String titleInPost;
        ArrayList<String> contentInPost;
        ArrayList<String> imagesInPost;
        ArrayList<Integer> orderInPost;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SeePostActivity.class);
                    intent.putExtra("title", titleInPost);
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_content_community, viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        PostContent content = postContentList.get(position);
        String contentTitle = content.getTitle();
        viewHolder.title.setText(contentTitle);
        viewHolder.titleInPost = contentTitle;
        viewHolder.contentInPost = content.getContent();
        viewHolder.imagesInPost = content.getImages();
        viewHolder.orderInPost = content.getOrder();
    }

    @Override
    public int getItemCount() {
        return postContentList.size();
    }
}