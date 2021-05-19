package gachon.termproject.joker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;

import gachon.termproject.joker.R;

public class MatchingUserViewFragment extends Fragment {
    private FragmentManager fm;
    private TabLayout tabs;
    private View view;
    private MatchingUserViewRequestFragment onProgress;
    private MatchingUserViewCompleteFragment complete;
    private MatchingExpertListFragment expertList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_matching_user_view, container, false);
        /*
//        Button location_update = view.findViewById(R.id.select_location); //지역선택버튼 만들면 id = select_location 으로 해주세연
//        location_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //matching_user_view_see로 이동,
//            }
//        });
        */
        fm = getChildFragmentManager();
        tabs = view.findViewById(R.id.tabs);

        if (onProgress == null) {
            onProgress = new MatchingUserViewRequestFragment();
            fm.beginTransaction().add(R.id.matching_user_frame, onProgress).commit();
        }

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tabs.getSelectedTabPosition()) {
                    case 0 :
                        if (complete != null) fm.beginTransaction().hide(complete).commit();
                        if (expertList != null) fm.beginTransaction().hide(expertList).commit();
                        fm.beginTransaction().show(onProgress).commit();
                        break;
                    case 1 :
                        if (complete == null) {
                            complete = new MatchingUserViewCompleteFragment();
                            fm.beginTransaction().add(R.id.matching_user_frame, complete).commit();
                        }
                        if (expertList != null) fm.beginTransaction().hide(expertList).commit();
                        fm.beginTransaction().hide(onProgress).commit();
                        fm.beginTransaction().show(complete).commit();
                        break;
                    case 2 :
                        if (expertList == null) {
                            expertList = new MatchingExpertListFragment();
                            fm.beginTransaction().add(R.id.matching_user_frame, expertList).commit();
                        }
                        if (complete != null) fm.beginTransaction().hide(complete).commit();
                        fm.beginTransaction().hide(onProgress).commit();
                        fm.beginTransaction().show(expertList).commit();
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
}