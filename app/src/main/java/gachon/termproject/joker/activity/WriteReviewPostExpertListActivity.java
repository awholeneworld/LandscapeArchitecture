package gachon.termproject.joker.activity;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import gachon.termproject.joker.UserInfo;

import gachon.termproject.joker.R;
import gachon.termproject.joker.adapter.WriteReviewPostExpertListAdapter;
import gachon.termproject.joker.Content.ExpertListContent;

public class WriteReviewPostExpertListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review_post_expert_list);

        RecyclerView content = findViewById(R.id.content);
        ArrayList<ExpertListContent> expertList = new ArrayList<>();
        WriteReviewPostExpertListAdapter expertListAdapter = new WriteReviewPostExpertListAdapter(getApplicationContext(), expertList);

        content.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        content.setHasFixedSize(true);
        content.setAdapter(expertListAdapter);

        FirebaseFirestore.getInstance().collection("users").limit(50).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> list = querySnapshot.getDocuments();
                    for (int i = 0; i < list.size(); i++) {
                        DocumentSnapshot snapshot = list.get(i);
                        Boolean isPublic = snapshot.getBoolean("isPublic");
                        String userId = snapshot.getId();
                        if (!isPublic && !userId.equals(UserInfo.userId)) {
                            String nickname = snapshot.getString("nickname");
                            String profileImg = snapshot.getString("profileImg");
                            String portfolioImg = snapshot.getString("portfolioImg");
                            String portfolioWeb = snapshot.getString("portfolioWeb");
                            expertList.add(new ExpertListContent(userId, nickname, profileImg, portfolioImg, portfolioWeb, new ArrayList<>()));
                        }
                    }
                    expertListAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}