package gachon.termproject.joker.fragment;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
    private Button portfolioButton;
    private FragmentManager fm;
    private TabLayout tabs;
    public static ImageView profileImg;
    public static TextView nickname;
    public static TextView location;
    public static TextView intro;
    static String locationStr;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myinfo, container, false);

        // 액션바 옵션메뉴 설정
        setHasOptionsMenu(true);

        // 레이아웃 가져오기
        portfolioButton = view.findViewById(R.id.portfolioButton);
        profileImg = view.findViewById(R.id.profileImage);
        nickname = view.findViewById(R.id.myInfoNickname);
        location = view.findViewById(R.id.myInfoLocation);
        intro = view.findViewById(R.id.myInfoMessage);
        tabs = view.findViewById(R.id.myinfo_tabs);

        // 프래그먼트 매니저 설정
        fm = getChildFragmentManager();

        // 포트폴리오 버튼 설정
        if (UserInfo.isPublic)
            portfolioButton.setVisibility(View.GONE);

        // 프사 설정
        profileImg.setBackground(new ShapeDrawable(new OvalShape()));
        profileImg.setClipToOutline(true);
        if (!UserInfo.profileImg.equals("None"))
            Glide.with(getActivity()).load(UserInfo.profileImg).into(profileImg);

        // 닉네임 설정
        nickname.setText(UserInfo.nickname);

        // 지역 설정
        locationStr = "";
        for (String item : UserInfo.location) {
            locationStr += item + " ";
        }

        location.setText(locationStr);

        // 한줄 소개 설정
        intro.setText(UserInfo.introduction);

        // 포트폴리오 버튼 누르면
        portfolioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "포트폴리오 창 이동", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MyInfoPortfolioFragment.class));
            }
        });

        // 마이인포 탭 첫 화면
        if (post == null) {
            post = new MyInfoTabPostFragment();
            fm.beginTransaction().add(R.id.myinfo_frame, post).commit();
        }

        // 탭 설정
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

    // 액션바 옵션 메뉴
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
                startActivityForResult(new Intent(getContext(), SettingActivity.class), 1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}