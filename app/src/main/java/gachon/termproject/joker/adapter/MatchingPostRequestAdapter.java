package gachon.termproject.joker.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import gachon.termproject.joker.Content.RequestFromExpertContent;
import gachon.termproject.joker.FirebaseHelper;
import gachon.termproject.joker.OnPostListener;
import gachon.termproject.joker.R;
import gachon.termproject.joker.fragment.ExpertPortfolioFragment;

public class MatchingPostRequestAdapter extends RecyclerView.Adapter<MatchingPostRequestAdapter.ViewHolder> {
    private Context context;
    private FirebaseHelper firebaseHelper;
    private ArrayList<RequestFromExpertContent> requestsList;

    public MatchingPostRequestAdapter(Context context, ArrayList<RequestFromExpertContent> requestsList)
    {
        this.context = context;
        this.requestsList = requestsList;
    }

    public void setOnPostListener(OnPostListener onPostListener){
        firebaseHelper.setOnPostListener(onPostListener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView profileImg;
        TextView nickname;
        Button viewPortfolio;
        Button acceptMatch;
        Button cancelMatch;
        String expertUserId;
        String expertNickname;
        String expertProfileImg;
        String expertPortfolioImg;
        String expertPortfolioWeb;
        ArrayList<String> expertLocation;

        ViewHolder(View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.requestImg);
            nickname = itemView.findViewById(R.id.requestNickname);
            viewPortfolio = itemView.findViewById(R.id.matching_request_item_pofol);
            acceptMatch = itemView.findViewById(R.id.matching_request_item_accept);
            cancelMatch = itemView.findViewById(R.id.matching_request_item_cancel);
            cancelMatch.setVisibility(View.INVISIBLE);

            viewPortfolio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ExpertPortfolioFragment.class);
                    intent.putExtra("userId", expertUserId);
                    intent.putExtra("nickname", expertNickname);
                    intent.putExtra("profileImg", expertProfileImg);
                    intent.putExtra("portfolioImg", expertPortfolioImg);
                    intent.putExtra("portfolioWeb", expertPortfolioWeb);
                    intent.putStringArrayListExtra("location", expertLocation);
                    context.startActivity(intent);
                }
            });

            acceptMatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptMatch.setVisibility(View.INVISIBLE);
                    cancelMatch.setVisibility(View.VISIBLE);
                }
            });

            cancelMatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelMatch.setVisibility(View.INVISIBLE);
                    acceptMatch.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @NonNull
    @Override
    public MatchingPostRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request_user_view_matching, parent,false);

        return new MatchingPostRequestAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull MatchingPostRequestAdapter.ViewHolder holder, int position) {
        RequestFromExpertContent request = requestsList.get(position);

        String profileImg = request.getExpertProfileImg();
        String nickname = request.getExpertNickname();

        holder.expertUserId = request.getExpertUserId();
        holder.expertNickname = nickname;
        holder.expertProfileImg = request.getExpertProfileImg();
        holder.expertPortfolioImg = request.getExpertPortfolioImg();
        holder.expertPortfolioWeb = request.getExpertPortfolioImg();
        holder.expertLocation = request.getExpertLocation();

        // 신청인 프로필 사진 표시
        holder.profileImg.setBackground(new ShapeDrawable(new OvalShape()));
        holder.profileImg.setClipToOutline(true);
        if (!profileImg.equals("None"))
            Glide.with(context).load(profileImg).into(holder.profileImg);

        // 신청인 표시
        holder.nickname.setText(request.getExpertNickname());

        if (request.getIsMatched()) {
            holder.acceptMatch.setVisibility(View.INVISIBLE);
            holder.cancelMatch.setVisibility(View.VISIBLE);
        } else {
            holder.cancelMatch.setVisibility(View.INVISIBLE);
            holder.acceptMatch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return requestsList.size();
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
