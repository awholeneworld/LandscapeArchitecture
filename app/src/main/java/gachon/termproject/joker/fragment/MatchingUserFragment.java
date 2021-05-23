package gachon.termproject.joker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import gachon.termproject.joker.R;
import gachon.termproject.joker.activity.CommunitySearchActivity;
import gachon.termproject.joker.adapter.MatchingUserPagerAdapter;

public class MatchingUserFragment extends Fragment {
    private TabLayout tabs;
    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_matching, container, false);
        /*
//        Button location_update = view.findViewById(R.id.select_location); //지역선택버튼 만들면 id = select_location 으로 해주세연
//        location_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //matching_user_view_see로 이동,
//            }
//        });
        */
        tabs = view.findViewById(R.id.tabs);

        tabs.addTab(tabs.newTab().setText("매칭요청"));
        tabs.addTab(tabs.newTab().setText("매칭완료"));
        tabs.addTab(tabs.newTab().setText("전문가 목록"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        //어답터설정
        final ViewPager viewPager = view.findViewById(R.id.matching_frame);
        final MatchingUserPagerAdapter myPagerAdapter = new MatchingUserPagerAdapter(getChildFragmentManager(), 3);
        viewPager.setAdapter(myPagerAdapter);

        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        return view;
    }

    //action bar menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_search_app_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().startActivity(new Intent(getContext(), CommunitySearchActivity.class));
        return super.onOptionsItemSelected(item);
    }
}