package gachon.termproject.joker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public static Activity mainActivity;
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Home home;
    private Community community;
    private Matching matching;
    private Chat chat;
    private MyInfo myinfo;
    Menu menu;

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

        home = new Home();
        community = new Community();
        matching = new Matching();
        chat = new Chat();
        myinfo = new MyInfo();

        setFrag(0);
    }

    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch (n) {
            case 0:
                ft.replace(R.id.main_frame, home);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case 1:
                ft.replace(R.id.main_frame, community);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case 2:
                ft.replace(R.id.main_frame, matching);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case 3:
                ft.replace(R.id.main_frame, chat);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case 4:
                ft.replace(R.id.main_frame, myinfo);
                ft.addToBackStack(null);
                ft.commit();
                break;
        }

    }

}