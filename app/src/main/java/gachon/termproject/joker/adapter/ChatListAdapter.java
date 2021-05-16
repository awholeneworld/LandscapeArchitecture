package gachon.termproject.joker.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.activity.ChatActivity;
import gachon.termproject.joker.container.ChatMessageContent;
import gachon.termproject.joker.container.ExpertListContent;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ChatMessageContent> chatList;

    public ChatListAdapter(Context context, ArrayList<ChatMessageContent> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView roomName;
        TextView lastMessage;
        ImageView profileImg;
        String opponentUid;
        String opponentNickname;
        String opponentProfileImg;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        ViewHolder(View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.roomName);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            profileImg = itemView.findViewById(R.id.profileImage);
            profileImg.setBackground(new ShapeDrawable(new OvalShape()));
            profileImg.setClipToOutline(true);
            opponentUid = null;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("userId", opponentUid);
                    intent.putExtra("nickname", opponentNickname);
                    intent.putExtra("profileImg", opponentProfileImg);
                    context.startActivity(intent);
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessageContent content = chatList.get(position);

        for (String user : content.users.keySet())
            if (!user.equals(UserInfo.userId)) {
                holder.opponentUid = user;
                holder.opponentNickname = content.users.get(user).nickname;
                holder.opponentProfileImg = content.users.get(user).profileUrl;
                holder.roomName.setText(holder.opponentNickname);

                if (!holder.opponentProfileImg.equals("None"))
                    Glide.with(context).load(holder.opponentProfileImg).override(1000).thumbnail(0.1f).into(holder.profileImg);

                Map<String, ChatMessageContent.Message> messages = new TreeMap<>(Collections.reverseOrder());
                messages.putAll(content.messages);

                String lastMessageKey = (String) messages.keySet().toArray()[0];
                holder.lastMessage.setText(content.messages.get(lastMessageKey).message);
            }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
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