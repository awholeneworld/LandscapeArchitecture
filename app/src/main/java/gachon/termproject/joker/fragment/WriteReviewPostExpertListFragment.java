package gachon.termproject.joker.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import gachon.termproject.joker.UserInfo;

import gachon.termproject.joker.R;
import gachon.termproject.joker.adapter.WriteReviewPostExpertListAdapter;
import gachon.termproject.joker.Content.ExpertListContent;

public class WriteReviewPostExpertListFragment extends Fragment {

    View view;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_write_review_post_expert_list, container, false);

        RecyclerView content = view.findViewById(R.id.content);
        ArrayList<ExpertListContent> expertList = new ArrayList<>();
        WriteReviewPostExpertListAdapter expertListAdapter = new WriteReviewPostExpertListAdapter(getContext(), expertList);

        content.setLayoutManager(new LinearLayoutManager(getContext()));
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
                            String introduction = snapshot.getString("introduction");
                            String profileImg = snapshot.getString("profileImg");
                            String portfolioImg = snapshot.getString("portfolioImg");
                            String portfolioWeb = snapshot.getString("portfolioWeb");
                            String pushToken = snapshot.getString("pushToken");
                            expertList.add(new ExpertListContent(userId, nickname, profileImg, portfolioImg, portfolioWeb, pushToken, introduction, new ArrayList<>()));
                        }
                    }
                    expertListAdapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }

}