package gachon.termproject.joker.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import gachon.termproject.joker.Content.MatchingPostContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.adapter.MatchingPostAdapter;

public class UserMatchingCommunitySearchActivity extends AppCompatActivity {

    private EditText query;
    private FirebaseDatabase firebaseDatabase;
    private static RecyclerView contents;
    ImageView img;
    TextView firstText;
    TextView nothingText;
    private DatabaseReference databaseReference;
    ArrayList<MatchingPostContent> postContentList;
    MatchingPostContent postContent;
    MatchingPostAdapter matchingpostAdapter;
    ValueEventListener postsListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_community);

        contents = findViewById(R.id.searchContent);
        img = findViewById(R.id.search_img);
        firstText = findViewById(R.id.search_first_text);
        nothingText = findViewById(R.id.search_nothing_text);

        contents.setVisibility(View.GONE);
        img.setVisibility(View.VISIBLE);
        firstText.setVisibility(View.VISIBLE);
        nothingText.setVisibility(View.GONE);

        System.out.println("yaya " + 1);


        postContentList = new ArrayList<>();
        matchingpostAdapter = new MatchingPostAdapter(this, postContentList, "needed");

        contents.setLayoutManager(new LinearLayoutManager(this));
        contents.setHasFixedSize(true);
        contents.setAdapter(matchingpostAdapter);

        query = findViewById(R.id.search_edittext);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search, menu);
        return true;
    }

    //검색 버튼을 눌렀을 때!!!
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String question = query.getText().toString().trim();

        System.out.println("yaya " + 2);

        if (question.isEmpty()) {
            contents.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);
            firstText.setVisibility(View.GONE);
            nothingText.setVisibility(View.VISIBLE);
        } else {
            postContentList.clear();

            postsListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    postContentList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        postContent = snapshot.getValue(MatchingPostContent.class);
                        if (!postContent.getIsMatched())
                            System.out.println("yaya " + !postContent.getIsMatched());

                        postContentList.add(0, postContent);
                    }
                    matchingpostAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            databaseReference.orderByChild("userId").equalTo(UserInfo.getUserId()).addListenerForSingleValueEvent(postsListener);

            if (postContentList.isEmpty()) {
                contents.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                firstText.setVisibility(View.GONE);
                nothingText.setVisibility(View.VISIBLE);
                System.out.println("yaya " + 4);
            }
            else{
                System.out.println("yaya " + 3);
                contents.setVisibility(View.VISIBLE);
                img.setVisibility(View.GONE);
                firstText.setVisibility(View.GONE);
                nothingText.setVisibility(View.GONE);
            }

            matchingpostAdapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }


}
