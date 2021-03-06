package gachon.termproject.joker.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import gachon.termproject.joker.Content.RequestFromExpertContent;
import gachon.termproject.joker.FirebaseDeleter;
import gachon.termproject.joker.R;
import gachon.termproject.joker.adapter.MatchingPostRequestAdapter;
import gachon.termproject.joker.fragment.MatchingUserTabCompleteFragment;
import gachon.termproject.joker.fragment.MatchingUserTabRequestFragment;

public class MatchingUserSeePostActivity extends AppCompatActivity {
    private LinearLayout container;
    private static DatabaseReference databaseReference;
    private static MatchingPostRequestAdapter adapter;
    private static ArrayList<RequestFromExpertContent> requestsList;
    private static TextView requestsNumView;
    private static TextView matchingView;
    private static String postId;
    private static int requestsNum = 0;
    private String locationStr;
    private boolean isMatched;
    private ArrayList<String> images;
    Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_user_see_post);

        //toolbar??? activity bar??? ??????!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); //?????? ?????????????
        actionBar.setDisplayShowTitleEnabled(false); //?????? ?????? ??????

        // ????????? ????????? ????????????
        intent = getIntent();
        postId = intent.getStringExtra("postId");
        String profileImg = intent.getStringExtra("profileImg");
        ArrayList<String> content = intent.getStringArrayListExtra("content");
        images = intent.getStringArrayListExtra("images");
        isMatched = intent.getBooleanExtra("isMatched", false);
        locationStr = intent.getStringExtra("locationInPost");

        // ???????????? ????????????
        ImageView profile = findViewById(R.id.postProfile);
        TextView title = findViewById(R.id.title);
        TextView nickname = findViewById(R.id.postNickname);
        TextView time = findViewById(R.id.postTime);
        TextView location =  findViewById(R.id.see_post_location_name);
        requestsNumView = findViewById(R.id.see_post_request_total_num);
        matchingView = findViewById(R.id.see_post_matching_line);
        RecyclerView requests = findViewById(R.id.matching_see_post_request_listview);
        container = findViewById(R.id.see_post_content);

        // ??????, ?????????, ????????????, ?????? ??????
        title.setText(intent.getStringExtra("title"));
        nickname.setText(intent.getStringExtra("nickname"));
        time.setText(intent.getStringExtra("time"));
        location.setText(locationStr);

        // ????????? ?????? ?????? (oimage ????????????)
        profile.setBackground(new ShapeDrawable(new OvalShape()));
        profile.setClipToOutline(true);
        if (!profileImg.equals("None"))
            Glide.with(this).load(profileImg).into(profile);

        // ??? ?????? ?????? - TextView ?????? ??? layout_width, layout_height, gravity, ?????? ??? ??????
        TextView text_content = new TextView(MatchingUserSeePostActivity.this);
        text_content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        text_content.setText(content.get(0));
        text_content.setTextSize(dpToPx(7));
        text_content.setTextColor(Color.BLACK);
        container.addView(text_content);

        requestsList = new ArrayList<>();
        adapter = new MatchingPostRequestAdapter(this, getApplicationContext(), requestsList, postId);
        databaseReference = FirebaseDatabase.getInstance().getReference("Matching/userRequests/" + postId + "/requests");

        requests.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        requests.setHasFixedSize(true);
        requests.setAdapter(adapter);

        // ????????? ????????? ??????
        if (images != null) {
            LinearLayout imageContainer = findViewById(R.id.see_post_imagecontainer);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpToPx(150), dpToPx(150));
            layoutParams.setMargins(dpToPx(10),0, dpToPx(10), 0);

            // imageView ?????????
            for (int i = 0; i < images.size(); i++) {
                if(images.get(0).compareTo("") == 0) break;

                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setLayoutParams(layoutParams);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                Glide.with(getApplicationContext()).load(images.get(i)).into(imageView);
                imageContainer.addView(imageView);
            }
        }

        // ?????? ???????????? ?????? ?????? ?????? & ?????? ????????? ????????? ????????? ????????????
        if (!isMatched)
            beforeMatched();
        else
            afterMatched();
    }

    //?????? ?????? ??????
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            //????????? ??? ???????????? - ??????
            case R.id.delete:
                FirebaseDeleter.postDelete(this, "Matching", "userRequests", postId, images);
                if (!isMatched)
                    MatchingUserTabRequestFragment.databaseReference.addValueEventListener(MatchingUserTabRequestFragment.postsListener);
                else
                    MatchingUserTabCompleteFragment.databaseReference.addValueEventListener(MatchingUserTabCompleteFragment.postsListener);

                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_post_menu, menu);

        // To display icon on overflow menu
        if (menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public static void beforeMatched() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requestsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RequestFromExpertContent content = snapshot.getValue(RequestFromExpertContent.class);
                    content.setUserId(snapshot.getKey());
                    requestsList.add(content);
                }
                requestsNum = requestsList.size();
                requestsNumView.setText(requestsNum + "???");
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public static void afterMatched() {
        matchingView.setText("????????????");
        requestsNumView.setVisibility(View.GONE);
        requestsList.clear();

        FirebaseDatabase.getInstance().getReference("Matching/userRequests/" + postId + "/requests").orderByChild("isMatched").equalTo(true).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if (snapshot.child("isMatched").getValue().equals(true)) {
                    RequestFromExpertContent content = snapshot.getValue(RequestFromExpertContent.class);
                    content.setUserId(snapshot.getKey());
                    requestsList.add(content);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {                                }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {                                }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {                                }
        });
    }

    public static int dpToPx(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}