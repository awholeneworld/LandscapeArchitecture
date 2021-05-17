package gachon.termproject.joker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import gachon.termproject.joker.R;

public class MainHomeFragment extends Fragment {
    private View view;
    private RelativeLayout expert;
    private TextView today_expert_name;

    private RecyclerView freecmulist;
    private RecyclerView tiplist;
    private RecyclerView reviewlist;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        expert = view.findViewById(R.id.home_today_expert);
        today_expert_name = view.findViewById(R.id.home_expert_name);


        return view;
    }

}
