package gachon.termproject.joker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import gachon.termproject.joker.Content.NotificationContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.adapter.ChatAreaAdapter;
import gachon.termproject.joker.Content.ChatMessageContent;

public class ChatActivity extends AppCompatActivity {
    public static String chatRoomId = null;
    public static String opponentNickname;
    public static String opponentProfileImg;
    public static RecyclerView chatArea;
    private TextView actionBarName;
    private ImageView sendButton;
    private EditText messageArea;
    private String opponentUserId;
    private String message;
    private boolean send = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?

        Intent intent = getIntent();
        opponentUserId = intent.getStringExtra("userId");
        opponentNickname = intent.getStringExtra("nickname");
        opponentProfileImg = intent.getStringExtra("profileImg");

        actionBarName = findViewById(R.id.opponentNickname);
        chatArea = findViewById(R.id.chatArea);
        messageArea = findViewById(R.id.messageArea);
        sendButton = findViewById(R.id.sendButton);

        actionBarName.setText(opponentNickname);
        checkChatRoom();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageArea.getText().toString().equals("")) {
                    message = messageArea.getText().toString();
                    messageArea.setText("");
                    if (chatRoomId == null) { // 채팅방 생성
                        ChatMessageContent chatMessageContent = new ChatMessageContent();
                        ChatMessageContent.User user = new ChatMessageContent.User();
                        ChatMessageContent.User opponent = new ChatMessageContent.User();
                        // 나의 정보
                        user.nickname = UserInfo.nickname;
                        user.profileImg = UserInfo.profileImg;
                        chatMessageContent.users.put(UserInfo.userId, user);

                        // 상대방 정보
                        opponent.nickname = opponentNickname;
                        opponent.profileImg = opponentProfileImg;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatting_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch(curId){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.show_profile:
                Toast.makeText(this, "프로필 보기", Toast.LENGTH_SHORT).show();
                break;
            case R.id.decelerate:
                Toast.makeText(this, opponentNickname + "(이)가 신고되었습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        chatRoomId = null;
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
        ChatMessageContent.Message messageToSend = new ChatMessageContent.Message();
        messageToSend.userId = UserInfo.userId;
        messageToSend.message = message;
        messageToSend.timestamp = ServerValue.TIMESTAMP;
        FirebaseDatabase.getInstance().getReference().child("Chat").child(chatRoomId).child("messages").push().setValue(messageToSend).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getApplicationContext(), "메시지 전송 실패", Toast.LENGTH_SHORT).show();
            }
        });
        if (UserInfo.pushToken == null) passPushTokenToServer();
        else sendGcm();
    }

    private void passPushTokenToServer() {
        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<InstallationTokenResult> task) {
                FirebaseFirestore.getInstance().collection("users").document(UserInfo.userId).update("pushToken", task.getResult().getToken()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        sendGcm();
                    }
                });
            }
        });
    }

    public void sendGcm() {
        Gson gson = new Gson();

        NotificationContent notificationContent = new NotificationContent();
        notificationContent.to = UserInfo.userId;
        notificationContent.notification.title = UserInfo.nickname;
        notificationContent.notification.text = message;
        notificationContent.data.title = UserInfo.nickname;
        notificationContent.data.text = message;


        RequestBody requestBody =RequestBody.create(MediaType.parse("application/json; charset=utf8"), gson.toJson(notificationContent));
        Request request = new Request.Builder().header("Content-Type", "application/json")
                .addHeader("Authorization", "key=AAAAm5WD8Bo:APA91bFr1BYENkzDe9KpX7JCk50IPp3ZtVc8LKSUvMmCxAZVadIB76K1OveBIm027j7ZH3naHZ65tuc9KeTNBqyWLOh6Ox0EyeRtBx2IdpVkl0n8ihZUMLY-I32WWAdObT-Mq-k2SUxV")
                .url("https://gcm-http.googleapis.com/gcm/send")
                .post(requestBody).build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }
}
