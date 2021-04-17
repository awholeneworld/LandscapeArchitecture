package gachon.termproject.joker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class  Community extends Fragment {
    private View view;
    private SwipeRefreshLayout refresher;
    private RecyclerView contents;
    private FirebaseUser user;
    private FloatingActionButton button;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<TitleContent> titleContentList = new ArrayList<>();
    ValueEventListener postsListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community, container, false);
        refresher = view.findViewById(R.id.refresh_layout);
        contents = view.findViewById(R.id.content_community);
        button = view.findViewById(R.id.fab);

        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Posts/example/" + user.getUid());

        PostAdapter postAdapter = new PostAdapter(getActivity(), titleContentList);
        contents.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        contents.setHasFixedSize(true);
        contents.setAdapter(postAdapter);

        postsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                titleContentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TitleContent titleContent = snapshot.getValue(TitleContent.class);
                    titleContentList.add(titleContent);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addListenerForSingleValueEvent(postsListener);

        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WritePostActivity.class));
            }
        });

        return view;
    }
}
