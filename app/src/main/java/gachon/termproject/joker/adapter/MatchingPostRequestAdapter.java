package gachon.termproject.joker.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.app.AlertDialog;
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
import gachon.termproject.joker.activity.ChatActivity;
import gachon.termproject.joker.activity.ExpertPortfolioActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MatchingPostRequestAdapter extends RecyclerView.Adapter<MatchingPostRequestAdapter.ViewHolder> {
    private Context context;
    private FirebaseHelper firebaseHelper;
    private ArrayList<RequestFromExpertContent> requestsList;
    private String postId;
    private Activity activity;

    public MatchingPostRequestAdapter(Activity activity, Context context, ArrayList<RequestFromExpertContent> requestsList, String postId)
    {
        this.context = context;
        this.requestsList = requestsList;
        this.postId = postId;
        this.activity = activity;
    }

    public void setOnPostListener(OnPostListener onPostListener){
        firebaseHelper.setOnPostListener(onPostListener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView profileImg;
        TextView nickname;
        Button viewPortfolio;
        Button chatbtn;
        Button matching_btn;
        String expertNickname;
        String expertProfileImg;
        String expertUserId;
        String expertPortfolioImg;
        String expertPortfolioWeb;
        boolean expertIsMatched;
        ArrayList<String> expertLocation;
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Matching/userRequests/" + postId);

        ViewHolder(View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.requestImg);
            nickname = itemView.findViewById(R.id.requestNickname);
            viewPortfolio = itemView.findViewById(R.id.matching_request_item_pofol);
            chatbtn = itemView.findViewById(R.id.matching_request_item_chat);
            matching_btn = itemView.findViewById(R.id.matching_request_item_accept_cancel);

            viewPortfolio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ExpertPortfolioActivity.class);
                    intent.putExtra("userId", expertUserId);
                    intent.putExtra("nickname", expertNickname);
                    intent.putExtra("profileImg", expertProfileImg);
                    intent.putExtra("portfolioImg", expertPortfolioImg);
                    intent.putExtra("portfolioWeb", expertPortfolioWeb);
                    intent.putStringArrayListExtra("location", expertLocation);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });


            chatbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("userId", expertUserId);
                    intent.putExtra("nickname", expertNickname);
                    intent.putExtra("profileImg", expertProfileImg);
                    context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
                }
            });


            matching_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dlg = new AlertDialog.Builder(activity);
                    dlg.setTitle(expertNickname + "님의 매칭을 수락하시겠습니까?"); //제목
                    dlg.setMessage("수락할 경우 다른 전문가의 매칭은 수락할 수 없습니다."); // 메시지

                    dlg.setPositiveButton("수락", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                            dbRef.child("isMatched").setValue(true);
                            dbRef.child("requests").orderByChild("expertNickname").equalTo(expertNickname).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                    snapshot.getRef().child("isMatched").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            activity.finish();
                                        }
                                    });
                                }
                                @Override
                                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) { }

                                @Override
                                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {                                }

                                @Override
                                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {                                }
                            });
                        }
                    });

                    dlg.setNegativeButton("취소", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dlg.show();
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
        holder.expertPortfolioWeb = request.getExpertPortfolioWeb();
        holder.expertLocation = request.getExpertLocation();
        holder.expertUserId = request.getExpertUserId();
        holder.expertIsMatched = request.getIsMatched();

        // 신청인 프로필 사진 표시
        holder.profileImg.setBackground(new ShapeDrawable(new OvalShape()));
        holder.profileImg.setClipToOutline(true);
        if (!profileImg.equals("None"))
            Glide.with(context).load(profileImg).into(holder.profileImg);

        // 신청인 표시
        holder.nickname.setText(request.getExpertNickname());

        //매칭된 상태라면 신청버튼 없애기
        if(holder.expertIsMatched){
            holder.matching_btn.setVisibility(View.GONE);
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
