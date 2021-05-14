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

    public ChatListAdapter(Context context) {
        this.context = context;
        chatList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Chat").orderByChild("users/" + UserInfo.userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    ChatMessageContent chatMessageContent = item.getValue(ChatMessageContent.class);
                    if (chatMessageContent.users.containsKey(UserInfo.userId))
                        chatList.add(0, chatMessageContent);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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


        /*
        // 비동기식이라 업데이트 못따라감 수정 필요
        FirebaseFirestore.getInstance().collection("users").document(holder.opponentUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                holder.opponentNickname = documentSnapshot.getString("nickname");
                holder.opponentProfileImg = documentSnapshot.getString("profileUrl");
                holder.roomName.setText(holder.opponentNickname);
                if (!holder.opponentProfileImg.equals("None"))
                    Glide.with(context).load(holder.opponentProfileImg).override(1000).thumbnail(0.1f).into(holder.profileImg);

                Map<String, ChatMessageContent.Message> messages = new TreeMap<>(Collections.reverseOrder());
                messages.putAll(content.messages);

                String lastMessageKey = (String) messages.keySet().toArray()[0];
                holder.lastMessage.setText(content.messages.get(lastMessageKey).message);
            }
        });
         */
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}