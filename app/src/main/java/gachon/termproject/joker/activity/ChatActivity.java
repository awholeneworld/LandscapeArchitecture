package gachon.termproject.joker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.adapter.ChatAreaAdapter;
import gachon.termproject.joker.container.ChatMessageContent;

public class ChatActivity extends AppCompatActivity {
    public static String chatRoomId = null;
    public static String opponentNickname;
    public static String opponentProfileImg;
    public static RecyclerView chatArea;
    private ImageView sendButton;
    private EditText messageArea;
    private String opponentUserId;
    private boolean send = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_chat);

        Intent intent = getIntent();
        opponentUserId = intent.getStringExtra("userId");
        opponentNickname = intent.getStringExtra("nickname");
        opponentProfileImg = intent.getStringExtra("profileImg");

        chatArea = findViewById(R.id.chatArea);
        messageArea = findViewById(R.id.messageArea);
        sendButton = findViewById(R.id.sendButton);

        checkChatRoom();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageArea.getText().toString().equals("")) {
                    if (chatRoomId == null) { // 채팅방 생성
                        ChatMessageContent chatMessageContent = new ChatMessageContent();
                        ChatMessageContent.User user = new ChatMessageContent.User();
                        ChatMessageContent.User opponent = new ChatMessageContent.User();
                        // 나의 정보
                        user.nickname = UserInfo.nickname;
                        user.profileUrl = UserInfo.profileImg;
                        chatMessageContent.users.put(UserInfo.userId, user);

                        // 상대방 정보
                        opponent.nickname = opponentNickname;
                        opponent.profileUrl = opponentProfileImg;
                        chatMessageContent.users.put(opponentUserId, opponent);

                        sendButton.setEnabled(false);

                        FirebaseDatabase.getInstance().getReference().child("Chat").push().setValue(chatMessageContent).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                send = true;
                                checkChatRoom();
                            }
                        });
                    } else {
                        sendMsgToDB();
                    }
                }
            }
        });
    }

    private void checkChatRoom() {
        FirebaseDatabase.getInstance().getReference().child("Chat").orderByChild("users/" + UserInfo.userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatMessageContent chatMessageContent = dataSnapshot.getValue(ChatMessageContent.class);
                    if (chatMessageContent.users.containsKey(UserInfo.userId) && chatMessageContent.users.containsKey(opponentUserId)) {
                        chatRoomId = dataSnapshot.getKey();
                        if (send) {
                            send = false;
                            sendMsgToDB();
                        }
                        sendButton.setEnabled(true);
                        chatArea.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        chatArea.setAdapter(new ChatAreaAdapter());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMsgToDB() {
        ChatMessageContent.Message message = new ChatMessageContent.Message();
        message.userId = UserInfo.userId;
        message.message = messageArea.getText().toString();
        message.timestamp = ServerValue.TIMESTAMP;
        FirebaseDatabase.getInstance().getReference().child("Chat").child(chatRoomId).child("messages").push().setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                messageArea.setText("");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        chatRoomId = null;
    }
}
