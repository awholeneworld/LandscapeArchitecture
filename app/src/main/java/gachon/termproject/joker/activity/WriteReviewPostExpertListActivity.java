package gachon.termproject.joker.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Build;
import android.os.Bundle;

import gachon.termproject.joker.R;
import gachon.termproject.joker.fragment.WriteReviewPostExpertListFragment;

public class WriteReviewPostExpertListActivity extends AppCompatActivity {
    private WriteReviewPostExpertListFragment writeReviewPostExpertListActivity;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review_post_expert_list);

        //toolbar를 activity bar로 지정!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 삭제
        actionBar.setDisplayHomeAsUpEnabled(true); //자동 뒤로가기?

        FragmentManager fm = getSupportFragmentManager(); // 프래그먼트 간의 이동을 도와주는 것
        if (writeReviewPostExpertListActivity == null) {
            writeReviewPostExpertListActivity = new WriteReviewPostExpertListFragment();
            fm.beginTransaction().add(R.id.write_review_post_expert_list_frame, writeReviewPostExpertListActivity).commit();
        }
        if (writeReviewPostExpertListActivity != null) fm.beginTransaction().show(writeReviewPostExpertListActivity).commit();
    }
}
