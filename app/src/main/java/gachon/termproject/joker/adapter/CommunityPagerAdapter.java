package gachon.termproject.joker.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import gachon.termproject.joker.fragment.CommunityFreeFragment;
import gachon.termproject.joker.fragment.CommunityReviewFragment;
import gachon.termproject.joker.fragment.CommunityTipFragment;

public class CommunityPagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    public CommunityPagerAdapter(@NonNull @NotNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.mNumOfTabs = behavior;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment tab1 = new CommunityFreeFragment();
                return tab1;

            case 1:
                Fragment tab2 = new CommunityReviewFragment();
                return tab2;

            case 2:
                Fragment tab3 = new CommunityTipFragment();
                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
