package gachon.termproject.joker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;

import gachon.termproject.joker.R;

public class MatchingExpertViewFragment extends Fragment {
    private FragmentManager fm;
    private TabLayout tabs;
    private View view;
    private MatchingExpertViewNeededFragment matchNeeded;
    private MatchingExpertViewAwaitingFragment awaiting;
    private MatchingExpertViewCompleteFragment complete;
    private MatchingExpertListFragment expertList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_matching_expert_view, container, false);

        fm = getChildFragmentManager();
        tabs = view.findViewById(R.id.tabs);

        if (matchNeeded == null) {
            matchNeeded = new MatchingExpertViewNeededFragment();
            fm.beginTransaction().add(R.id.matching_expert_frame, matchNeeded).commit();
        }

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tabs.getSelectedTabPosition()) {
                    case 0:
                        if (awaiting != null) fm.beginTransaction().hide(awaiting).commit();
                        if (complete != null) fm.beginTransaction().hide(complete).commit();
                        if (expertList != null) fm.beginTransaction().hide(expertList).commit();
                        fm.beginTransaction().show(matchNeeded).commit();
                        break;
                    case 1:
                        if (awaiting == null) {
                            awaiting = new MatchingExpertViewAwaitingFragment();
                            fm.beginTransaction().add(R.id.matching_expert_frame, awaiting).commit();
                        }
                        if (complete != null) fm.beginTransaction().hide(complete).commit();
                        if (expertList != null) fm.beginTransaction().hide(expertList).commit();
                        fm.beginTransaction().hide(matchNeeded).commit();
                        fm.beginTransaction().show(awaiting).commit();
                        break;
                    case 2:
                        if (complete == null) {
                            complete = new MatchingExpertViewCompleteFragment();
                            fm.beginTransaction().add(R.id.matching_expert_frame, complete).commit();
                        }
                        if (awaiting != null) fm.beginTransaction().hide(awaiting).commit();
                        if (expertList != null) fm.beginTransaction().hide(expertList).commit();
                        fm.beginTransaction().hide(matchNeeded).commit();
                        fm.beginTransaction().show(complete).commit();
                        break;
                    case 3:
                        if (expertList == null) {
                            expertList = new MatchingExpertListFragment();
                            fm.beginTransaction().add(R.id.matching_expert_frame, expertList).commit();
                        }
                        if (awaiting != null) fm.beginTransaction().hide(awaiting).commit();
                        if (complete != null) fm.beginTransaction().hide(complete).commit();
                        fm.beginTransaction().hide(matchNeeded).commit();
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