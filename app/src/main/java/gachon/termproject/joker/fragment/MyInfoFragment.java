package gachon.termproject.joker.fragment;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import gachon.termproject.joker.R;
import gachon.termproject.joker.UserInfo;
import gachon.termproject.joker.activity.SettingActivity;

public class MyInfoFragment extends Fragment {
    private View view;
    private MyInfoTabPostFragment post;
    private MyInfoTabCommentFragment comment;
    private ViewGroup portfolioLayout;
    private FragmentManager fm;
    private TabLayout tabs;
    static String locationStr;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myinfo, container, false);

        //action bar menu
        setHasOptionsMenu(true);

        // 포트폴리오 창 설정
        portfolioLayout = view.findViewById(R.id.portfolioLayout);

        if (UserInfo.isPublic) {
            portfolioLayout.setVisibility(View.GONE);
        } else {
            portfolioLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "포트폴리오 창 이동", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), MyInfoPortfolioFragment.class));
                }
            });
        }

        // 프사 설정
        ImageView profileImg = view.findViewById(R.id.profileImage);
        //profileImg.setBackground(new ShapeDrawable(new OvalShape()));
        if (!UserInfo.profileImg.equals("None"))
            Glide.with(getActivity()).load(UserInfo.profileImg).into(profileImg);

        // 닉네임 설정
        TextView nickname = view.findViewById(R.id.myInfoNickname);
        nickname.setText(UserInfo.nickname);

        // 지역 설정
        locationStr = "";
        for (String item : UserInfo.location) {
            locationStr += item + " ";
        }
        TextView location = view.findViewById(R.id.myInfoLocation);
        location.setText(locationStr);

        // 한줄 소개 설정 -> 설정 구현되면 마저 작성할 것임
        TextView intro = view.findViewById(R.id.myInfoMessage);
        intro.setText(UserInfo.introduction);

        // 마이인포 탭 첫 화면
        fm = getChildFragmentManager();
        if (post == null) {
            post = new MyInfoTabPostFragment();
            fm.beginTransaction().add(R.id.myinfo_frame, post).commit();
        }

        // 탭 설정
        tabs = view.findViewById(R.id.myinfo_tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tabs.getSelectedTabPosition()) {
                    case 0:
                        if (comment != null) fm.beginTransaction().hide(comment).commit();
                        fm.beginTransaction().show(post).commit();
                        break;
                    case 1:
                        if (comment == null) {
                            comment = new MyInfoTabCommentFragment();
                            fm.beginTransaction().add(R.id.myinfo_frame, comment).commit();
                        }
                        fm.beginTransaction().hide(post).commit();
                        fm.beginTransaction().show(comment).commit();
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

    // action bar menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_setting_app_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch(item.getItemId()) {
            case R.id.setting:
                getActivity().startActivity(new Intent(getContext(), SettingActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}