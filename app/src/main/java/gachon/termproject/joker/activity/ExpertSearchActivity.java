package gachon.termproject.joker.activity;

import android.os.Build;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import gachon.termproject.joker.Content.ExpertListContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.adapter.ExpertListAdapter;

public class ExpertSearchActivity extends AppCompatActivity {
    private EditText queryView;
    private RecyclerView contents;
    private ImageView img;
    private TextView firstText;
    private TextView nothingText;
    private ExpertListAdapter expertListAdapter;
    private ArrayList<ExpertListContent> searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_expert);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?

        contents = findViewById(R.id.searchContent);
        img = findViewById(R.id.search_img);
        firstText = findViewById(R.id.search_first_text);
        nothingText = findViewById(R.id.search_nothing_text);

        contents.setVisibility(View.GONE);
        img.setVisibility(View.VISIBLE);
        firstText.setVisibility(View.VISIBLE);
        nothingText.setVisibility(View.GONE);

        searchList = new ArrayList<>();
        expertListAdapter = new ExpertListAdapter(this, searchList);

        contents.setLayoutManager(new LinearLayoutManager(this));
        contents.setHasFixedSize(true);
        contents.setAdapter(expertListAdapter);

        queryView = findViewById(R.id.search_edittext);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search, menu);
        return true;
    }

    //검색 버튼을 눌렀을 때!!!
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        searchList.clear();
        String query = queryView.getText().toString().trim();

        if (query.isEmpty()) {
            contents.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);
            firstText.setVisibility(View.GONE);
            nothingText.setVisibility(View.VISIBLE);
        } else {
            FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<DocumentSnapshot> list = querySnapshot.getDocuments();

                        for (int i = 0; i < list.size(); i++) {
                            DocumentSnapshot snapshot = list.get(i);
                            String userId = snapshot.getId();
                            boolean isPublic = snapshot.getBoolean("isPublic");

                            if (!userId.equals(UserInfo.userId) && !isPublic) {
                                if (snapshot.getString("nickname").contains(query)){
                                    String nickname = snapshot.getString("nickname");
                                    String introduction = snapshot.getString("introduction");
                                    String profileImg = snapshot.getString("profileImg");
                                    String portfolioImg = snapshot.getString("portfolioImg");
                                    String portfolioWeb = snapshot.getString("portfolioWeb");
                                    String pushToken = snapshot.getString("pushToken");
                                    ArrayList<String> location = (ArrayList<String>) snapshot.get("location");
                                    searchList.add(new ExpertListContent(userId, nickname, profileImg, portfolioImg, portfolioWeb, pushToken, introduction, location));
                                }
                            }
                        }

                        if (searchList.isEmpty()) {
                            contents.setVisibility(View.GONE);
                            img.setVisibility(View.VISIBLE);
                            firstText.setVisibility(View.GONE);
                            nothingText.setVisibility(View.VISIBLE);
                        } else {
                            contents.setVisibility(View.VISIBLE);
                            img.setVisibility(View.GONE);
                            firstText.setVisibility(View.GONE);
                            nothingText.setVisibility(View.GONE);
                            expertListAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }
}
