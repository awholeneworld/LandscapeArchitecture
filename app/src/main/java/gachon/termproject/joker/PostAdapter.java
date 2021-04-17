package gachon.termproject.joker;

import android.content.Context;
import android.content.Intent;
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
    ArrayList<TitleContent> titleContentList;
    
    public PostAdapter(Context context, ArrayList<TitleContent> titleContentList)
    {
        this.context = context;
        this.titleContentList = titleContentList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SeePostActivity.class);
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
        TitleContent content = titleContentList.get(position);
        viewHolder.title.setText(content.getTitle());
    }

    @Override
    public int getItemCount() {
        return titleContentList.size();
    }

    
}