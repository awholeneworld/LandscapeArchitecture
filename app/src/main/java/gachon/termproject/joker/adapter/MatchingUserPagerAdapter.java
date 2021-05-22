package gachon.termproject.joker.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import gachon.termproject.joker.fragment.MatchingExpertListFragment;
import gachon.termproject.joker.fragment.MatchingUserViewCompleteFragment;
import gachon.termproject.joker.fragment.MatchingUserViewRequestFragment;

public class MatchingUserPagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    public MatchingUserPagerAdapter(androidx.fragment.app.FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment tab1 = new MatchingUserViewRequestFragment();
                return tab1;
            case 1:
                Fragment  tab2 = new MatchingUserViewCompleteFragment();
                return tab2;
            case 2:
                Fragment  tab3 = new MatchingExpertListFragment();
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
