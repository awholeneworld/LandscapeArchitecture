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

public class MatchingFrame extends Fragment {
    private FragmentManager fm;
    private View view;
    private ExpertList expertList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frame_matching, container, false);
        fm = getChildFragmentManager();

        // 전문가 리스트 보기 테스트
        Button testExpertList = view.findViewById(R.id.show_expertList);
        testExpertList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expertList == null) {
                    expertList = new ExpertList();
                    fm.beginTransaction().add(R.id.expertList_frame, expertList).commit();
                }
                fm.beginTransaction().show(expertList).commit();
            }
        });

        return view;
    }
}