package gachon.termproject.joker.activity;

import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import gachon.termproject.joker.Content.ExpertListContent;
import gachon.termproject.joker.Content.PostContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.adapter.ExpertListAdapter;
import gachon.termproject.joker.adapter.PostAdapter;

public class ExpertSearchActivity extends AppCompatActivity {

    private EditText query;
    private CollectionReference collectionReference;
    private static RecyclerView contents;
    ImageView img;
    TextView firstText;
    TextView nothingText;
    private DatabaseReference databaseReference;
    PostContent postContent;
    private ExpertListAdapter expertListAdapter;
    private ArrayList<ExpertListContent> expertList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_expert);

        contents = findViewById(R.id.searchContent);
        img = findViewById(R.id.search_img);
        firstText = findViewById(R.id.search_first_text);
        nothingText = findViewById(R.id.search_nothing_text);

        contents.setVisibility(View.GONE);
        img.setVisibility(View.VISIBLE);
        firstText.setVisibility(View.VISIBLE);
        nothingText.setVisibility(View.GONE);

        System.out.println("yaya " + 1);


        expertList = new ArrayList<>();
        expertListAdapter = new ExpertListAdapter(this, expertList);

        contents.setLayoutManager(new LinearLayoutManager(this));
        contents.setHasFixedSize(true);
        contents.setAdapter(expertListAdapter);

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
            expertList.clear();

            FirebaseFirestore fStore = FirebaseFirestore.getInstance();
            collectionReference = fStore.collection("users");
            collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<DocumentSnapshot> list = querySnapshot.getDocuments();
                        boolean next = false;
                        ArrayList<String> idList = new ArrayList<String>();
                        idList.add("test"); //null에러를 막기위함

                        for (int i = 0; i < list.size(); i++) {
                            DocumentSnapshot snapshot = list.get(i);
                            boolean publicMan = snapshot.getBoolean("isPublic");
                            if (!publicMan) { //일단 전문가여야함
                                String name = snapshot.getString("nickname");

                                if(name.contains(question)){
                                    System.out.println("yaya" + name);

                                    System.out.println("yayaya" + question);

                                    String userId = snapshot.getId();
                                    String nickname = snapshot.getString("nickname");
                                    String profileImg = snapshot.getString("profileImg");
                                    String portfolioImg = snapshot.getString("portfolioImg");
                                    String portfolioWeb = snapshot.getString("portfolioWeb");
                                    String pushToken = snapshot.getString("pushToken");
                                    ArrayList<String> location = (ArrayList<String>) snapshot.get("location");
                                    expertList.add(new ExpertListContent(userId, nickname, profileImg, portfolioImg, portfolioWeb, pushToken, location));
                                    System.out.println("yaya " + 3);
                                    contents.setVisibility(View.VISIBLE);
                                    img.setVisibility(View.GONE);
                                    firstText.setVisibility(View.GONE);
                                    nothingText.setVisibility(View.GONE);
                                }

                                expertListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
            if (expertList.isEmpty()) {
                contents.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                firstText.setVisibility(View.GONE);
                nothingText.setVisibility(View.VISIBLE);
                System.out.println("yaya " + 4);
            }

            expertListAdapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }


}
