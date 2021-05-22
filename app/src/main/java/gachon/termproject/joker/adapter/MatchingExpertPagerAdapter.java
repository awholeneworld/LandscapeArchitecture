package gachon.termproject.joker.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import gachon.termproject.joker.fragment.MatchingExpertListFragment;
import gachon.termproject.joker.fragment.MatchingExpertViewAwaitingFragment;
import gachon.termproject.joker.fragment.MatchingExpertViewCompleteFragment;
import gachon.termproject.joker.fragment.MatchingExpertViewNeededFragment;

public class MatchingExpertPagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    public MatchingExpertPagerAdapter(androidx.fragment.app.FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment tab1 = new MatchingExpertViewNeededFragment();
                return tab1;
            case 1:
                Fragment  tab2 = new MatchingExpertViewAwaitingFragment();
                return tab2;
            case 2:
                Fragment  tab3 = new MatchingExpertViewCompleteFragment();
                return tab3;
            case 3:
                Fragment  tab4 = new MatchingExpertListFragment();
                return tab4;
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
