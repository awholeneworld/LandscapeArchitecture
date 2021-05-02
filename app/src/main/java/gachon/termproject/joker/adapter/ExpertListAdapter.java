package gachon.termproject.joker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

import gachon.termproject.joker.FirebaseHelper;
import gachon.termproject.joker.R;
import gachon.termproject.joker.container.ExpertListContent;

public class ExpertListAdapter extends RecyclerView.Adapter<ExpertListAdapter.ViewHolder>{

        private Context context;
        private FirebaseHelper firebaseHelper;
        ArrayList<ExpertListContent> matchingContentList;

    public ExpertListAdapter(Context context, ArrayList<ExpertListContent> matchingContentList) {
            this.context = context;
            this.matchingContentList = matchingContentList;
        }


        public class ViewHolder extends RecyclerView.ViewHolder  {
            TextView nickname;
           // ImageView image; //사진 추가하면 주석해제
            //ArrayList<String> imagesInPost;
            //ArrayList<Integer> orderInPost;

            ViewHolder(View itemView) {
                super(itemView);
                nickname = itemView.findViewById(R.id.writer);
                //image = itemView.findViewById(R.id.image);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*나중에 전문가 누르면 전문가 포폴뜨게하는 곳
                        Intent intent = new Intent(context, SeePostActivity.class);
                        intent.putExtra("userId", userIdInPost);
                        intent.putExtra("title", titleInPost);
                        intent.putExtra("postId", postIdInPost);
                        intent.putStringArrayListExtra("content", contentInPost);
                        intent.putStringArrayListExtra("images", imagesInPost);
                        intent.putIntegerArrayListExtra("order", orderInPost);
                        context.startActivity(intent);
                        */

                    }
                });
            }

        }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content_expert_list_matching, parent,false);
        ExpertListAdapter.ViewHolder viewHolder = new ExpertListAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ExpertListContent content = matchingContentList.get(position);


        String contentNickname = content.getNickname();
        //ArrayList<String> imagesList = content.getImages();

        ExpertListContent matchingContent = matchingContentList.get(position);
        viewHolder.nickname.setText(matchingContent.getNickname());
        //viewHolder.image.setImageDrawable(matchingContent.getImages());
        //viewHolder.nicknameInPost = contentNickname;
        //viewHolder.imagesInPost = imagesList;

    }

        @Override
        public int getItemCount() {
        return matchingContentList.size();
    }
}
