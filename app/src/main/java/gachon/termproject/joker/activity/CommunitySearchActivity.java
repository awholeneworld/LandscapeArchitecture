package gachon.termproject.joker.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

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
    private DatabaseReference databaseReference;
    ArrayList<PostContent> postContentList;
    PostContent postContent;
    PostAdapter postAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_community);

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
        String question = query.getText().toString();

        String url = "Posts/free";
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
                                    Log.e(question + " Equals", snapshot.getKey());
                                    //postContent = snapshot.getValue(PostContent.class);
                                    //postContentList.add(0, postContent); 무려 심지어 넣어놓음

                                }
                            }

                        }


                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        return super.onOptionsItemSelected(item);
    }


}
