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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import gachon.termproject.joker.Content.RequestFromExpertContent;
import gachon.termproject.joker.FirebaseHelper;
import gachon.termproject.joker.OnPostListener;
import gachon.termproject.joker.R;
import gachon.termproject.joker.fragment.ExpertPortfolioFragment;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MatchingPostRequestAdapter extends RecyclerView.Adapter<MatchingPostRequestAdapter.ViewHolder> {
    private Context context;
    private FirebaseHelper firebaseHelper;
    private ArrayList<RequestFromExpertContent> requestsList;
    private String postId;

    public MatchingPostRequestAdapter(Context context, ArrayList<RequestFromExpertContent> requestsList, String postId)
    {
        this.context = context;
        this.requestsList = requestsList;
        this.postId = postId;
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
        String expertNickname;
        String expertProfileImg;
        String expertPortfolioImg;
        String expertPortfolioWeb;
        ArrayList<String> expertLocation;
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Matching/userRequests/" + postId);

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
                    intent.putExtra("nickname", expertNickname);
                    intent.putExtra("profileImg", expertProfileImg);
                    intent.putExtra("portfolioImg", expertPortfolioImg);
                    intent.putExtra("portfolioWeb", expertPortfolioWeb);
                    intent.putStringArrayListExtra("location", expertLocation);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            acceptMatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptMatch.setEnabled(false);
                    dbRef.child("isMatched").setValue(true);
                    dbRef.child("requests").orderByChild("expertNickname").equalTo(expertNickname).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                            snapshot.getRef().child("isMatched").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    acceptMatch.setVisibility(View.INVISIBLE);
                                    cancelMatch.setVisibility(View.VISIBLE);
                                    acceptMatch.setEnabled(true);
                                }
                            });
                        }

                        @Override
                        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            });

            cancelMatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelMatch.setEnabled(false);
                    dbRef.child("isMatched").setValue(false);
                    dbRef.child("requests").orderByChild("expertNickname").equalTo(expertNickname).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                            snapshot.getRef().child("isMatched").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    cancelMatch.setVisibility(View.INVISIBLE);
                                    acceptMatch.setVisibility(View.VISIBLE);
                                    cancelMatch.setEnabled(true);
                                }
                            });
                        }

                        @Override
                        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            });
        }
    }

    @NonNull
    @Override
    public MatchingPostRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request_see_post_user_matching, parent,false);

        return new MatchingPostRequestAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull MatchingPostRequestAdapter.ViewHolder holder, int position) {
        RequestFromExpertContent request = requestsList.get(position);

        String profileImg = request.getExpertProfileImg();
        String nickname = request.getExpertNickname();

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
