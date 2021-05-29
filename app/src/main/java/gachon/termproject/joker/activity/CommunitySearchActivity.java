package gachon.termproject.joker.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import gachon.termproject.joker.Content.PostContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.adapter.PostAdapter;

public class CommunitySearchActivity extends AppCompatActivity {

    private EditText query;
    private FirebaseDatabase firebaseDatabase;
    private static RecyclerView contents;
    ImageView img;
    TextView firstText;
    TextView nothingText;
    private DatabaseReference databaseReference;
    ArrayList<PostContent> postContentList;
    PostContent postContent;
    PostAdapter postAdapter;


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
        postAdapter = new PostAdapter(this, postContentList);

        contents.setLayoutManager(new LinearLayoutManager(this));
        contents.setHasFixedSize(true);
        contents.setAdapter(postAdapter);

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
            String url = "Posts/free";
            postContentList.clear();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference(url);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String tempUrl = url;
                        tempUrl += "/" + snapshot.getKey();
                        DatabaseReference tempDatabaseReference;
                        tempDatabaseReference = firebaseDatabase.getReference(tempUrl);
                        //Log.e("a", snapshot.getValue().toString());

                        tempDatabaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot1) {

                                for (DataSnapshot ssnapshot : dataSnapshot1.getChildren()) {
                                    if (ssnapshot.getKey().equals("title") && ssnapshot.getValue().toString().contains(question)) {
                                        //자기거만 넣음
                                        System.out.println("yaya " + 3);
                                        contents.setVisibility(View.VISIBLE);
                                        img.setVisibility(View.GONE);
                                        firstText.setVisibility(View.GONE);
                                        nothingText.setVisibility(View.GONE);

                                        Log.e(question + " Equals", snapshot.getKey());
                                        postContent = snapshot.getValue(PostContent.class);
                                        postContentList.add(0, postContent);
                                    }
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            }
                        });
                    }
                    if (postContentList.isEmpty()) {
                        contents.setVisibility(View.GONE);
                        img.setVisibility(View.VISIBLE);
                        firstText.setVisibility(View.GONE);
                        nothingText.setVisibility(View.VISIBLE);
                        System.out.println("yaya " + 4);
                    }

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                }
            });
            postAdapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }


}
