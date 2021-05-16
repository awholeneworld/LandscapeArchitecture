package gachon.termproject.joker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;

import gachon.termproject.joker.R;

public class CommunityFragment extends Fragment {
    private View view;
    private FragmentManager fm;
    private CommunityFreeFragment free;
    private CommunityReviewFragment review;
    private CommunityTipFragment tip;
    TabLayout tabs;
    Button selectCommunityMode;
    boolean i = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_community, container, false);

        setHasOptionsMenu(true); //action bar menu

        // 앨범, 리스트 변환 버튼 부분은 내가 해야될 것 같아서 삭제했음
        tabs = view.findViewById(R.id.tabs);
        fm = getChildFragmentManager();

        if (free == null) {
            free = new CommunityFreeFragment();
            fm.beginTransaction().add(R.id.community_frame, free).commit();
        }

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tabs.getSelectedTabPosition()) {
                    case 0 :
                        if (review != null) fm.beginTransaction().hide(review).commit();
                        if (tip != null) fm.beginTransaction().hide(tip).commit();
                        fm.beginTransaction().show(free).commit();
                        break;
                    case 1 :
                        if (review == null) {
                            review = new CommunityReviewFragment();
                            fm.beginTransaction().add(R.id.community_frame, review).commit();
                        }
                        if (tip != null) fm.beginTransaction().hide(tip).commit();
                        fm.beginTransaction().hide(free).commit();
                        fm.beginTransaction().show(review).commit();
                        break;
                    case 2 :
                        if (tip == null) {
                            tip = new CommunityTipFragment();
                            fm.beginTransaction().add(R.id.community_frame, tip).commit();
                        }
                        if (review != null) fm.beginTransaction().hide(review).commit();
                        fm.beginTransaction().hide(free).commit();
                        fm.beginTransaction().show(tip).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return view;
    }


    //action bar menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_search_app_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}