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

public class MatchingExpertFragment extends Fragment {
    private FragmentManager fm;
    private View view;
    private MatchingExpertListFragment matchingExpertListFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_matching_expert, container, false);
        fm = getChildFragmentManager();



        return view;
    }
}