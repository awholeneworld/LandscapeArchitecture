package gachon.termproject.joker.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Comparator;

import gachon.termproject.joker.Content.PostContent;
import gachon.termproject.joker.R;
import gachon.termproject.joker.fragment.ChatFragment;
import gachon.termproject.joker.fragment.CommunityFragment;
import gachon.termproject.joker.fragment.HomeFragment;
import gachon.termproject.joker.fragment.MatchingExpertFragment;
import gachon.termproject.joker.fragment.MatchingUserFragment;
import gachon.termproject.joker.fragment.MyInfoFragment;
import gachon.termproject.joker.UserInfo;

public class MainActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private TextView action_bar_title;
    private BottomNavigationView bottomNavigationView;
    private HomeFragment home;
    private CommunityFragment community;
    private MatchingUserFragment matchingUser;
    private MatchingExpertFragment matchingExpert;
    private ChatFragment chat;
    private MyInfoFragment myInfo;
    private int backPressed = 0;
    public static ArrayList<String> userPostsIdList;
    public static ArrayList<PostContent> userPostsList;
    public static ArrayList<String> userCommentsIdList;
    public static ArrayList<PostContent> postsOfCommentsList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ????????? ????????? ????????? ???????????? ?????? ???????????? ??? ??????????????? ??? ??? ?????? ?????????
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    FirebaseFirestore.getInstance().collection("users").document(UserInfo.getUserId()).update("pushToken", task.getResult());
                }
            }
        });

        //toolbar~~~~~~~~~~toolbar??? activity bar??? ??????!
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //?????? ?????? ??????
        action_bar_title = findViewById(R.id.main_toolbar_textview);
        //toolbar~~~~~~~~~end

        // ?????? ??????????????? ??? ??????
        bottomNavigationView = findViewById(R.id.bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        setFrag(0);
                        break;
                    case R.id.community:
                        setFrag(1);
                        break;
                    case R.id.matching:
                        setFrag(2);
                        break;
                    case R.id.chat:
                        setFrag(3);
                        break;
                    case R.id.myInfo:
                        setFrag(4);
                        break;
                }
                return true;
            }
        });

        // ????????? ????????? ????????? ????????? ????????????
        Intent intent = getIntent();
        userPostsIdList = intent.getStringArrayListExtra("userPostsIdList");
        userCommentsIdList = intent.getStringArrayListExtra("userCommentsIdList");
        userPostsList = intent.getParcelableArrayListExtra("userPostsList");
        postsOfCommentsList = intent.getParcelableArrayListExtra("postsOfCommentsList");
        if (userPostsIdList == null) userPostsIdList = new ArrayList<>();
        if (userCommentsIdList == null) userCommentsIdList = new ArrayList<>();
        if (userPostsList == null) userPostsList = new ArrayList<>();
        else userPostsList.sort(new Comparator<PostContent>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public int compare(PostContent o1, PostContent o2) {
                long o1Id = Long.parseUnsignedLong(o1.getPostId());
                long o2Id = Long.parseUnsignedLong(o2.getPostId());

                if (o1Id < o2Id) return 1;
                else return -1;
            }
        });
        if (postsOfCommentsList == null) postsOfCommentsList = new ArrayList<>();
        else postsOfCommentsList.sort(new Comparator<PostContent>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public int compare(PostContent o1, PostContent o2) {
                long o1Id = Long.parseUnsignedLong(o1.getPostId());
                long o2Id = Long.parseUnsignedLong(o2.getPostId());

                if (o1Id < o2Id) return 1;
                else return -1;
            }
        });


        // ??????????????? ?????????????????? ?????? ?????? ?????? ????????? ??????
        FragmentManager fm = getSupportFragmentManager();
        if (UserInfo.getIsPublic()) { //user??????
            if (matchingUser == null) {
                matchingUser = new MatchingUserFragment();
                fm.beginTransaction().add(R.id.main_frame, matchingUser).commit();
            }
        }
        else {
            if (matchingExpert == null) {
                matchingExpert = new MatchingExpertFragment();
                fm.beginTransaction().add(R.id.main_frame, matchingExpert).commit();
            }
        }

        setFrag(0); // ????????? ??? ???????????? ??? ????????? ????????? ??????
    }

    //back arrow button ????????? ??? ????????? home??????
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager(); // ??????????????? ?????? ????????? ???????????? ???

        switch (item.getItemId()) {
            case android.R.id.home:
                if (home != null) fm.beginTransaction().show(home).commit();
                if (community != null) fm.beginTransaction().hide(community).commit();
                if (matchingUser != null) fm.beginTransaction().hide(matchingUser).commit();
                if (matchingExpert != null) fm.beginTransaction().hide(matchingExpert).commit();
                if (chat != null) fm.beginTransaction().hide(chat).commit();
                if (myInfo != null) fm.beginTransaction().hide(myInfo).commit();

                action_bar_title.setText("JOKER");
                actionBar.setDisplayHomeAsUpEnabled(false); //???????????? ?????? ????????????
                bottomNavigationView.setSelectedItemId(R.id.home);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ???????????? ?????? ?????? ??? ????????? ??????
    // ?????? ?????? ??? ??? ??????
    @Override
    public void onBackPressed() {
        if (backPressed == 0) {
            setFrag(0);
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
            backPressed++;
        }
        else finish();
    }

    // ?????? ???????????????????????? ?????? ?????? ?????? ??????
    private void setFrag(int n) {
        FragmentManager fm = getSupportFragmentManager(); // ??????????????? ?????? ????????? ???????????? ???

        // ?????? ????????????????????? ???????????? ?????? ????????? ?????? ??????????????? ?????????????????? ??????
        /*
        replace??? ?????? ?????????????????? ???????????? ????????? ??????????????? ????????? ??? ????????? ?????? ?????? ?????????.
        ????????? ??? ?????????????????? ??? ?????? ????????? ????????? ???????????? ????????? ?????????
        ??????????????? ?????? ????????? ????????? ?????????, ???????????? ???????????? ????????????.
         */

        //fragment ????????? ???, Action Bar ???????????? ?????? ???????????????

        switch (n) {
            case 0:
                if (home == null) {
                    home = new HomeFragment();
                    fm.beginTransaction().add(R.id.main_frame, home).commit();
                }
                if (home != null) fm.beginTransaction().show(home).commit();
                if (community != null) fm.beginTransaction().hide(community).commit();
                if (matchingUser != null) fm.beginTransaction().hide(matchingUser).commit();
                if (matchingExpert != null) fm.beginTransaction().hide(matchingExpert).commit();
                if (chat != null) fm.beginTransaction().hide(chat).commit();
                if (myInfo != null) fm.beginTransaction().hide(myInfo).commit();

                action_bar_title.setText("JOKER");
                actionBar.setDisplayHomeAsUpEnabled(false); //???????????? ?????? ????????????

                break;

            case 1:
                if (community == null) {
                    community = new CommunityFragment();
                    fm.beginTransaction().add(R.id.main_frame, community).commit();
                }
                fm.beginTransaction().hide(home).commit();
                if (community != null) fm.beginTransaction().show(community).commit();
                if (matchingUser != null) fm.beginTransaction().hide(matchingUser).commit();
                if (matchingExpert != null) fm.beginTransaction().hide(matchingExpert).commit();
                if (chat != null) fm.beginTransaction().hide(chat).commit();
                if (myInfo != null) fm.beginTransaction().hide(myInfo).commit();

                action_bar_title.setText("????????????");
                actionBar.setDisplayHomeAsUpEnabled(true); //???????????? ?????? ????????? ????????? ?????????

                break;

            case 2:
                fm.beginTransaction().hide(home).commit();
                if (community != null) fm.beginTransaction().hide(community).commit();
                if (matchingUser != null) fm.beginTransaction().show(matchingUser).commit();
                if (matchingExpert != null) fm.beginTransaction().show(matchingExpert).commit();
                if (chat != null) fm.beginTransaction().hide(chat).commit();
                if (myInfo != null) fm.beginTransaction().hide(myInfo).commit();

                action_bar_title.setText("????????? ??????");
                actionBar.setDisplayHomeAsUpEnabled(true); //???????????? ?????? ????????? ????????? ?????????

                break;

            case 3:
                if (chat == null) {
                    chat = new ChatFragment();
                    fm.beginTransaction().add(R.id.main_frame, chat).commit();
                }
                fm.beginTransaction().hide(home).commit();
                if (community != null) fm.beginTransaction().hide(community).commit();
                if (matchingUser != null) fm.beginTransaction().hide(matchingUser).commit();
                if (matchingExpert != null) fm.beginTransaction().hide(matchingExpert).commit();
                if (chat != null) fm.beginTransaction().show(chat).commit();
                if (myInfo != null) fm.beginTransaction().hide(myInfo).commit();

                action_bar_title.setText("?????????");
                actionBar.setDisplayHomeAsUpEnabled(true); //???????????? ?????? ????????? ????????? ?????????

                break;

            case 4:
                if (myInfo == null) {
                    myInfo = new MyInfoFragment();
                    fm.beginTransaction().add(R.id.main_frame, myInfo).commit();
                }
                fm.beginTransaction().hide(home).commit();
                if (community != null) fm.beginTransaction().hide(community).commit();
                if (matchingUser != null) fm.beginTransaction().hide(matchingUser).commit();
                if (matchingExpert != null) fm.beginTransaction().hide(matchingExpert).commit();
                if (chat != null) fm.beginTransaction().hide(chat).commit();
                if (myInfo != null) fm.beginTransaction().show(myInfo).commit();

                action_bar_title.setText("??? ??????");
                actionBar.setDisplayHomeAsUpEnabled(true); //???????????? ?????? ????????? ????????? ?????????

                break;
        }
    }
}