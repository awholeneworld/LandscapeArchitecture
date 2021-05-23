package gachon.termproject.joker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import gachon.termproject.joker.R;
import gachon.termproject.joker.adapter.MatchingExpertPagerAdapter;

public class MatchingExpertFragment extends Fragment {
    private TabLayout tabs;
    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_matching, container, false);

        tabs = view.findViewById(R.id.tabs);

        tabs.addTab(tabs.newTab().setText("매칭요청"));
        tabs.addTab(tabs.newTab().setText("매칭중"));
        tabs.addTab(tabs.newTab().setText("매칭완료"));
        tabs.addTab(tabs.newTab().setText("전문가 목록"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        //어답터설정
        final ViewPager viewPager = view.findViewById(R.id.matching_frame);
        final MatchingExpertPagerAdapter myPagerAdapter = new MatchingExpertPagerAdapter(getChildFragmentManager(), 4);
        viewPager.setAdapter(myPagerAdapter);

        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        return view;
    }
}