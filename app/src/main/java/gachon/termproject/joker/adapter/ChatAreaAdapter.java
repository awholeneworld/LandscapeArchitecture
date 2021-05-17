package gachon.termproject.joker.adapter;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.container.ChatMessageContent;

import static gachon.termproject.joker.activity.ChatActivity.chatArea;
import static gachon.termproject.joker.activity.ChatActivity.chatRoomId;
import static gachon.termproject.joker.activity.ChatActivity.opponentNickname;
import static gachon.termproject.joker.activity.ChatActivity.opponentProfileImg;

public class ChatAreaAdapter extends RecyclerView.Adapter<ChatAreaAdapter.ViewHolder> {
    private List<ChatMessageContent.Message> messageList;
    private SimpleDateFormat simpleDateFormat;

    public ChatAreaAdapter(){
        messageList = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("yyy.MM.dd HH:mm");
        getMessageList();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView message;   //메시지 내용
        public TextView nickname;
        public TextView time;
        public ImageView profileImg;
        public LinearLayout messagebox;
        public LinearLayout sender;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.messagebox_msg);
            nickname = itemView.findViewById(R.id.messagebox_nickname);
            time = itemView.findViewById(R.id.messagebox_timestamp);
            profileImg = itemView.findViewById(R.id.messagebox_profileImg);
            profileImg.setBackground(new ShapeDrawable(new OvalShape()));
            profileImg.setClipToOutline(true);

            messagebox = itemView.findViewById(R.id.messagebox);
            sender = itemView.findViewById(R.id.messagebox_sender);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_messagebox, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //나의 uid 이면
        if (messageList.get(position).userId.equals(UserInfo.userId)) {
            //나의 말풍선 오른쪽으로
            holder.sender.setVisibility(View.INVISIBLE);
            holder.message.setText(messageList.get(position).message);
            holder.message.setBackgroundResource(R.drawable.rightbubble); // 이부분 수정
            holder.messagebox.setGravity(Gravity.RIGHT);
        } else {
            //상대방 말풍선 왼쪽
            holder.sender.setVisibility(View.VISIBLE);
            if (!opponentProfileImg.equals("None"))
                Glide.with(holder.itemView.getContext()).load(opponentProfileImg).into(holder.profileImg);
            holder.nickname.setText(opponentNickname);
            holder.message.setText(messageList.get(position).message);
            holder.message.setBackgroundResource(R.drawable.leftbubble);
            holder.messagebox.setGravity(Gravity.LEFT);
        }

        holder.time.setText(getDateTime(position));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    //채팅 내용 읽어들임
    private void getMessageList() {
        FirebaseDatabase.getInstance().getReference().child("Chat").child(chatRoomId).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    messageList.add(dataSnapshot.getValue(ChatMessageContent.Message.class));
                }
                notifyDataSetChanged();
                chatArea.scrollToPosition(messageList.size() - 1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public String getDateTime(int position) {
        long unixTime = (long) messageList.get(position).timestamp;
        Date date = new Date(unixTime);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String time = simpleDateFormat.format(date);
        return time;
    }
}
