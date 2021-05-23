package gachon.termproject.joker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.adapter.ChatListAdapter;
import gachon.termproject.joker.Content.ChatMessageContent;

public class ChatFragment extends Fragment {
    private View view;
    private ImageView backgroundImg;
    private TextView backgroundTxt;
    private RecyclerView recyclerView;
    private ArrayList<ChatMessageContent> chatList;
    private ChatListAdapter chatListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);

        backgroundImg = view.findViewById(R.id.empty_chat_img);
        backgroundTxt = view.findViewById(R.id.empty_chat_txt);
        recyclerView = view.findViewById(R.id.roomList);

        backgroundImg.setVisibility(View.INVISIBLE);
        backgroundTxt.setVisibility(View.INVISIBLE);

        chatList = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(getActivity(), chatList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(chatListAdapter);

        FirebaseDatabase.getInstance().getReference().child("Chat").orderByChild("users/" + UserInfo.userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    ChatMessageContent chatMessageContent = item.getValue(ChatMessageContent.class);
                    if (chatMessageContent.users.containsKey(UserInfo.userId))
                        chatList.add(0, chatMessageContent);
                }
                if (chatList.size() == 0) {
                    backgroundImg.setVisibility(View.VISIBLE);
                    backgroundTxt.setVisibility(View.VISIBLE);
                }
                chatListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return view;
    }
}
