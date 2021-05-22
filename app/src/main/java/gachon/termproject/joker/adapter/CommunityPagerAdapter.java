package gachon.termproject.joker.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import gachon.termproject.joker.fragment.CommunityFreeFragment;
import gachon.termproject.joker.fragment.CommunityReviewFragment;
import gachon.termproject.joker.fragment.CommunityTipFragment;

public class CommunityPagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    public CommunityPagerAdapter(androidx.fragment.app.FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment tab1 = new CommunityFreeFragment();
                return tab1;
            case 1:
                Fragment  tab2 = new CommunityReviewFragment();
                return tab2;
            case 2:
                Fragment  tab3 = new CommunityTipFragment();
                return tab3;
            default:
                return null;
        }
        //return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
