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

import gachon.termproject.joker.R;

public class MatchingFragment extends Fragment {
    private FragmentManager fm;
    private View view;
    private MatchingExpertListFragment matchingExpertListFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_matching, container, false);
        fm = getChildFragmentManager();

        // 전문가 리스트 보기 테스트
        Button testExpertList = view.findViewById(R.id.show_expertList);
        testExpertList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (matchingExpertListFragment == null) {
                    matchingExpertListFragment = new MatchingExpertListFragment();
                    fm.beginTransaction().add(R.id.expertList_frame, matchingExpertListFragment).commit();
                }
                fm.beginTransaction().show(matchingExpertListFragment).commit();
            }
        });

        return view;
    }
}