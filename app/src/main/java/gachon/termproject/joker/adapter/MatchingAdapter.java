package gachon.termproject.joker.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import gachon.termproject.joker.FirebaseHelper;
import gachon.termproject.joker.OnPostListener;
import gachon.termproject.joker.PostContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.activity.SeePostActivity;
import gachon.termproject.joker.fragment.MatchingContent;

public class MatchingAdapter extends RecyclerView.Adapter<MatchingAdapter.ViewHolder>{

        private Context context;
        private FirebaseHelper firebaseHelper;
        ArrayList<MatchingContent> matchingContentList;

    public MatchingAdapter(Context context, ArrayList<MatchingContent> matchingContentList) {
            this.context = context;
            this.matchingContentList = matchingContentList;
        }


        public class ViewHolder extends RecyclerView.ViewHolder  {
            TextView nickname;
            ImageView image;
            //ArrayList<String> imagesInPost;
            //ArrayList<Integer> orderInPost;

            ViewHolder(View itemView) {
                super(itemView);
                nickname = itemView.findViewById(R.id.writer);
                image = itemView.findViewById(R.id.image);

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content_matching, parent,false);
        MatchingAdapter.ViewHolder viewHolder = new MatchingAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        MatchingContent content = matchingContentList.get(position);


        String contentNickname = content.getNickname();
        ArrayList<String> imagesList = content.getImages();


        Date contentTime = new Date();

        MatchingContent matchingContent = matchingContentList.get(position);
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
