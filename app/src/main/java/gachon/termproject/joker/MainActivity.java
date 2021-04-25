package gachon.termproject.joker;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fm;
    private BottomNavigationView bottomNavigationView;
    private HomeFrame home;
    private CommunityListStyle community;
    private MatchingFrame matching;
    private ChatFrame chat;
    private MyInfoFrame myinfo;
    Menu menu;
    private int backPressed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //toolbar~~~~~~~~~~toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?
        //toolbar~~~~~~~~~end

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

                    case R.id.myinfo:
                        setFrag(4);
                        break;
                }

                return true;
            }
        });

        setFrag(0);
    }

    private void setFrag(int n) {
        fm = getSupportFragmentManager();

        switch (n) {
            case 0:
                if (home == null) {
                    home = new HomeFrame();
                    fm.beginTransaction().add(R.id.main_frame, home).commit();
                }
                if (home != null) fm.beginTransaction().show(home).commit();
                if (community != null) fm.beginTransaction().hide(community).commit();
                if (matching != null) fm.beginTransaction().hide(matching).commit();
                if (chat != null) fm.beginTransaction().hide(chat).commit();
                if (myinfo != null) fm.beginTransaction().hide(myinfo).commit();
                break;

            case 1:
                if (community == null) {
                    // Community Tab 완성하면 CommunityFrame으로 수정해야됨!!
                    community = new CommunityListStyle();                    //
                    //
                    fm.beginTransaction().add(R.id.main_frame, community).commit();
                }
                if (home != null) fm.beginTransaction().hide(home).commit();
                if (community != null) fm.beginTransaction().show(community).commit();
                if (matching != null) fm.beginTransaction().hide(matching).commit();
                if (chat != null) fm.beginTransaction().hide(chat).commit();
                if (myinfo != null) fm.beginTransaction().hide(myinfo).commit();
                break;

            case 2:
                if (matching == null) {
                    matching = new MatchingFrame();
                    fm.beginTransaction().add(R.id.main_frame, matching).commit();
                }
                if (home != null) fm.beginTransaction().hide(home).commit();
                if (community != null) fm.beginTransaction().hide(community).commit();
                if (matching != null) fm.beginTransaction().show(matching).commit();
                if (chat != null) fm.beginTransaction().hide(chat).commit();
                if (myinfo != null) fm.beginTransaction().hide(myinfo).commit();
                break;

            case 3:
                if (chat == null) {
                    chat = new ChatFrame();
                    fm.beginTransaction().add(R.id.main_frame, chat).commit();
                }
                if (home != null) fm.beginTransaction().hide(home).commit();
                if (community != null) fm.beginTransaction().hide(community).commit();
                if (matching != null) fm.beginTransaction().hide(matching).commit();
                if (chat != null) fm.beginTransaction().show(chat).commit();
                if (myinfo != null) fm.beginTransaction().hide(myinfo).commit();
                break;

            case 4:
                if (myinfo == null) {
                    myinfo = new MyInfoFrame();
                    fm.beginTransaction().add(R.id.main_frame, myinfo).addToBackStack(null).commit();
                }
                if (home != null) fm.beginTransaction().hide(home).commit();
                if (community != null) fm.beginTransaction().hide(community).commit();
                if (matching != null) fm.beginTransaction().hide(matching).commit();
                if (chat != null) fm.beginTransaction().hide(chat).commit();
                if (myinfo != null) fm.beginTransaction().show(myinfo).commit();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (backPressed == 0) {
            setFrag(0);
            bottomNavigationView.setSelectedItemId(R.id.home);
            backPressed++;
        }
        else finish();
    }
}