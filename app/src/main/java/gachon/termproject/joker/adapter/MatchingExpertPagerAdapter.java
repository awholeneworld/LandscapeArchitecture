package gachon.termproject.joker.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import gachon.termproject.joker.fragment.MatchingExpertTabCompleteFragment;
import gachon.termproject.joker.fragment.MatchingTabExpertListFragment;
import gachon.termproject.joker.fragment.MatchingExpertTabProgressFragment;
import gachon.termproject.joker.fragment.MatchingExpertTabRequestFragment;

public class MatchingExpertPagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;
    public static Fragment tab1;
    public static Fragment tab2;
    public static Fragment tab3;
    public static Fragment tab4;

    public MatchingExpertPagerAdapter(@NonNull @NotNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.mNumOfTabs = behavior;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (tab1 == null) tab1 = new MatchingExpertTabRequestFragment();
                return tab1;

            case 1:
                if (tab2 == null)  tab2 = new MatchingExpertTabProgressFragment();
                return tab2;

            case 2:
                if (tab3 == null)  tab3 = new MatchingExpertTabCompleteFragment();
                return tab3;

            case 3:
                if (tab4 == null) tab4 = new MatchingTabExpertListFragment();
                return tab4;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
