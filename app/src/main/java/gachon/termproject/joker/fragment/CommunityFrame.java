package gachon.termproject.joker.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import gachon.termproject.joker.FreeCommunity;
import gachon.termproject.joker.R;
import gachon.termproject.joker.ReviewCommunity;
import gachon.termproject.joker.TipCommunity;

import static android.app.Activity.RESULT_OK;

public class  CommunityFrame extends Fragment {
    private View view;
    private FragmentManager fm;
    private FreeCommunity free;
    private ReviewCommunity review;
    private TipCommunity tip;
    TabLayout tabs;
    Button selectCommunityMode;
    boolean i = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community_frame, container, false);

        // 지민이가 새로 다시 만들고 깃에 업데이트하면 돼~
        // 단, 앨범, 리스트 변환 버튼 부분은 내가 해야될 것 같아서 삭제했음
        tabs = view.findViewById(R.id.tabs);
        fm = getChildFragmentManager();

        if (free == null) {
            free = new FreeCommunity();
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
                            review = new ReviewCommunity();
                            fm.beginTransaction().add(R.id.community_frame, review).commit();
                        }
                        fm.beginTransaction().show(review).commit();
                        fm.beginTransaction().hide(free).commit();
                        if (tip != null) fm.beginTransaction().hide(tip).commit();
                        break;
                    case 2 :
                        if (tip == null) {
                            tip = new TipCommunity();
                            fm.beginTransaction().add(R.id.community_frame, tip).commit();
                        }
                        fm.beginTransaction().show(tip).commit();
                        fm.beginTransaction().hide(free).commit();
                        if (review != null) fm.beginTransaction().hide(review).commit();
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