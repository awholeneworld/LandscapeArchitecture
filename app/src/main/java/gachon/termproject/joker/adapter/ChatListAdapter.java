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
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.activity.ChatActivity;
import gachon.termproject.joker.Content.ChatMessageContent;

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
        String opponentIntro;
        String opponentPushToken;
        ArrayList<String> opponentLocation;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        ViewHolder(View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.roomName);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            profileImg = itemView.findViewById(R.id.profileImage);
            profileImg.setBackground(new ShapeDrawable(new OvalShape()));
            profileImg.setClipToOutline(true);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("userId", opponentUid);
                    intent.putExtra("nickname", opponentNickname);
                    intent.putExtra("profileImg", opponentProfileImg);
                    intent.putExtra("intro", opponentIntro);
                    intent.putExtra("pushToken", opponentPushToken);
                    intent.putStringArrayListExtra("location", opponentLocation);
                    context.startActivity(intent);
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_chat, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessageContent content = chatList.get(position);

        for (String user : content.users.keySet())
            if (!user.equals(UserInfo.getUserId())) {
                holder.opponentUid = user;
                holder.opponentNickname = content.users.get(user).nickname;
                holder.opponentProfileImg = content.users.get(user).profileImg;
                holder.opponentIntro = content.users.get(user).introduction;
                holder.opponentPushToken = content.users.get(user).pushToken;
                holder.opponentLocation = content.users.get(user).location;
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