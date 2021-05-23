package gachon.termproject.joker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import gachon.termproject.joker.R;
import gachon.termproject.joker.activity.CommunitySearchActivity;
import gachon.termproject.joker.activity.WritePostActivity;
import gachon.termproject.joker.activity.WriteReviewPostExpertListActivity;
import gachon.termproject.joker.adapter.CommunityPagerAdapter;

public class CommunityFragment extends Fragment {
    private View view;
    private FloatingActionButton button;

    TabLayout tabs;
    Button selectCommunityMode;
    boolean i = true;
    private RelativeLayout loaderLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_community, container, false);

        // 액션바 메뉴
        setHasOptionsMenu(true);

        // 앨범, 리스트 변환 버튼 부분은 내가 해야될 것 같아서 삭제했음
        // 레이아웃 가져오기
        button = view.findViewById(R.id.fab);
        tabs = view.findViewById(R.id.tabs);

        // 어댑터설정
        final ViewPager viewPager = view.findViewById(R.id.community_frame);
        final CommunityPagerAdapter myPagerAdapter = new CommunityPagerAdapter(getChildFragmentManager(), 3);
        viewPager.setAdapter(myPagerAdapter);

        // 탭 설정
        tabs.addTab(tabs.newTab().setText("자유"));
        tabs.addTab(tabs.newTab().setText("후기"));
        tabs.addTab(tabs.newTab().setText("조경 팁"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        intent = new Intent(getActivity(), WritePostActivity.class);
                        intent.putExtra("category", "free");
                        startActivity(intent);
                        break;

                    case 1:
                        startActivity(new Intent(getActivity(), WriteReviewPostExpertListActivity.class));
                        break;

                    case 2:
                        intent = new Intent(getActivity(), WritePostActivity.class);
                        intent.putExtra("category", "tip");
                        startActivity(intent);
                        break;

                    default:
                        break;
                }
            }
        });

        return view;
    }

    // 액션바 옵션메뉴
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_search_app_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().startActivity(new Intent(getContext(), CommunitySearchActivity.class));
        return super.onOptionsItemSelected(item);
    }

}